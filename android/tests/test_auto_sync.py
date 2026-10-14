import json,urllib.request,urllib.error,uuid
BASE="https://psipfadpuciwjdyiuxqj.supabase.co/functions/v1/receitas-onca-sync"
def call(action,method="GET",body=None,token=""):
    headers={"Content-Type":"application/json"}
    if token:headers["x-onca-session"]=token
    req=urllib.request.Request(BASE+"?action="+action,data=json.dumps(body).encode() if body is not None else None,headers=headers,method=method)
    try:
        with urllib.request.urlopen(req,timeout=45) as r:return r.status,json.load(r)
    except urllib.error.HTTPError as e:return e.code,json.load(e)
s,a=call("auto-join","POST",{"name":"Verificação automática A"});assert s==200,(s,a.get("error"))
s,b=call("auto-join","POST",{"name":"Verificação automática B"});assert s==200,(s,b.get("error"))
assert a["bookId"]==b["bookId"] and a["token"]!=b["token"]
s,data=call("recipes",token=a["token"]);assert s==200,(s,data.get("error"))
names={r["body"]["name"] for r in data["recipes"] if not r["deleted"]}
assert {"Bolo de fubá","Bombocado","Bolo de areia","Pudim de padaria","Torta de ricota","Maria-mole","Manjar branco"}.issubset(names),names
recipe={"name":"Verificação automática (não publicada)","category":"Doces","origin":"carmela","time":"","yield":"","ingredients":"1 ingrediente de teste","steps":"1 etapa de teste","notes":"Registro de teste oculto, criado já como excluído.","photo":"","step_photos":[],"favorite":0}
rid=str(uuid.uuid4())
s,data=call("recipes","PUT",{"id":rid,"version":0,"body":recipe,"deleted":True},a["token"]);assert s==200 and data["version"]==1,(s,data)
recipe["notes"]="Edição autorizada no segundo aparelho de teste."
s,data=call("recipes","PUT",{"id":rid,"version":1,"body":recipe,"deleted":True},b["token"]);assert s==200 and data["version"]==2,(s,data)
s,data=call("recipes",token=a["token"]);assert s==200
row=next(r for r in data["recipes"] if r["id"]==rid)
assert row["deleted"] and row["version"]==2 and row["body"]["notes"]==recipe["notes"]
assert call("recipes")[0]==401
print("PASS: entrada sem código, duas sessões automáticas, sete receitas e edição compartilhada sem publicar registros de teste")

for session in (a["token"],b["token"]):
    s,result=call("logout","POST",{},session)
    assert s==200
    assert call("recipes",token=session)[0]==401
print("PASS: sessões de teste encerradas")
