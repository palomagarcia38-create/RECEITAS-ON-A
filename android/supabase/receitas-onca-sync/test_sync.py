import json,urllib.request,urllib.error,uuid
BASE="https://psipfadpuciwjdyiuxqj.supabase.co/functions/v1/receitas-onca-sync"
def call(action,method="GET",body=None,token="",mime="application/json"):
    data=body if isinstance(body,bytes) else json.dumps(body).encode() if body is not None else None
    headers={"Content-Type":mime}
    if token: headers["x-onca-session"]=token
    req=urllib.request.Request(BASE+"?action="+action,data=data,headers=headers,method=method)
    try:
        with urllib.request.urlopen(req,timeout=45) as r:return r.status,json.load(r)
    except urllib.error.HTTPError as e:return e.code,json.load(e)
assert call("recipes")[0]==401
assert call("join","POST",{"code":"wrong"})[0]==401
s,a=call("join","POST",{"code":"ONCA-0011223344556677","name":"Teste A"});assert s==200,(s,a)
s,b=call("join","POST",{"code":"ONCA-0011223344556677","name":"Teste B"});assert s==200,(s,b)
one,two=a["token"],b["token"]
assert one!=two
with urllib.request.urlopen("https://raw.githubusercontent.com/palomagarcia38-create/RECEITAS-ON-A/main/android/app/src/main/assets/portrait.jpg") as r:image=r.read()
s,result=call("media","POST",image,one,"image/jpeg");assert s==200,(s,result)
path=result["path"]
recipe={"name":"Receita temporária de teste","category":"Doces","origin":"user","time":"","yield":"","ingredients":"1 ingrediente","steps":"1 etapa","notes":"","photo":path,"step_photos":[path],"favorite":0}
rid=str(uuid.uuid4())
try:
    s,result=call("recipes","PUT",{"id":rid,"version":0,"body":recipe,"deleted":False},one);assert s==200 and result["version"]==1,(s,result)
    s,result=call("recipes",token=two);assert s==200,(s,result)
    row=next(x for x in result["recipes"] if x["id"]==rid);assert row["body"]["name"]==recipe["name"]
    with urllib.request.urlopen(row["photoUrl"]) as r:assert r.read()==image
    with urllib.request.urlopen(row["stepUrls"][0]) as r:assert r.read()==image
    recipe["notes"]="Alterada no segundo aparelho"
    s,result=call("recipes","PUT",{"id":rid,"version":1,"body":recipe,"deleted":False},two);assert s==200 and result["version"]==2,(s,result)
    assert call("recipes","PUT",{"id":rid,"version":1,"body":recipe,"deleted":False},one)[0]==409
    wrong=dict(recipe);wrong["photo"]="00000000-0000-4000-8000-000000000000/foto.jpg"
    assert call("recipes","PUT",{"id":str(uuid.uuid4()),"version":0,"body":wrong,"deleted":False},one)[0]==403
    s,result=call("recipes","PUT",{"id":rid,"version":2,"body":recipe,"deleted":True},one);assert s==200 and result["version"]==3,(s,result)
    s,result=call("recipes",token=two);row=next(x for x in result["recipes"] if x["id"]==rid);assert row["deleted"] and row["version"]==3
    assert call("recipes",token="f"*64)[0]==401
    print("PASS: autenticação, duas sessões, receita, foto, foto de etapa, edição, conflito, isolamento e exclusão")
finally:
    s,result=call("remove-media","DELETE",{"path":path},one)
    if s!=200: print("Foto de teste aguarda limpeza administrativa")
