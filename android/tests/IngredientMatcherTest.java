package br.com.mariacarmela.receitas;
import java.util.*;
public class IngredientMatcherTest {
 static void require(boolean ok,String message){if(!ok)throw new AssertionError(message);}
 static Set<String> filtered(Map<String,String> recipes,String query){Set<String> found=new LinkedHashSet<>();List<String> ingredients=IngredientMatcher.available(query);for(Map.Entry<String,String> recipe:recipes.entrySet())if(IngredientMatcher.score(recipe.getValue(),ingredients).matchesAll())found.add(recipe.getKey());return found;}
 public static void main(String[] args){
 List<String> pantry=IngredientMatcher.available("ovos, leite\nfarinha de trigo; AÇÚCAR, ovos");
 require(pantry.size()==4,"Lista, duplicados e acentos");
 IngredientMatcher.Result cake=IngredientMatcher.score("MASSA\n5 ovos\n1 xícara (chá) de leite\n2 xícaras (chá) de farinha de trigo\n1 xícara de açúcar refinado\n1 colher (sopa) de fermento em pó\nCOBERTURA\n1 xícara de leite",pantry);
 require(cake.found.size()==4&&cake.missing.size()==1,"Quantidades, grupos e duplicados");
 require(IngredientMatcher.score("1 pitada de sal",IngredientMatcher.available("salsinha")).found.isEmpty(),"Limites de palavras");
 require(IngredientMatcher.score("1 lata de milho",IngredientMatcher.available("ovo")).found.isEmpty(),"Receita sem ingredientes disponíveis");
 require(IngredientMatcher.score("2 ovos\n1 tomate",IngredientMatcher.available("ovo e tomates")).found.size()==2,"Singular/plural e separador e");
 require(IngredientMatcher.available(" , ; \n ").isEmpty(),"Busca vazia");
 require(IngredientMatcher.score("1 copo de leite de coco",IngredientMatcher.available("leite de coco")).found.size()==1,"Nomes compostos");
 require(IngredientMatcher.score("1 copo de leite de coco\n1 lata de leite condensado",IngredientMatcher.available("leite")).found.isEmpty(),"Leite comum não é leite de coco ou condensado");

 Map<String,String> examples=new LinkedHashMap<>();
 examples.put("Banana simples","2 bananas");
 examples.put("Banana com açúcar","2 bananas\n1 xícara de açúcar");
 examples.put("Banana com leite","2 bananas\n1 copo de leite");
 examples.put("Vitamina completa","2 bananas\n1 xícara de açúcar\n1 copo de leite");
 examples.put("Creme sem banana","1 xícara de açúcar\n1 copo de leite");
 require(filtered(examples,"banana").equals(new LinkedHashSet<>(Arrays.asList("Banana simples","Banana com açúcar","Banana com leite","Vitamina completa"))),"Banana exclui receitas sem banana");
 require(filtered(examples,"banana, açúcar").equals(new LinkedHashSet<>(Arrays.asList("Banana com açúcar","Vitamina completa"))),"Banana e açúcar são obrigatórios");
 require(filtered(examples,"banana, açúcar, leite").equals(Collections.singleton("Vitamina completa")),"Os três ingredientes são obrigatórios");
 require(filtered(examples,"BANANAS\nACUCAR;leite").equals(Collections.singleton("Vitamina completa")),"AND preserva acentos, plural e vários separadores");
 require(filtered(examples,"banana, banana, leite").equals(new LinkedHashSet<>(Arrays.asList("Banana com leite","Vitamina completa"))),"Duplicados não mudam o filtro");
 require(filtered(examples,"banana, chocolate").isEmpty(),"Ingrediente ausente elimina todas as receitas");
 require(!IngredientMatcher.score("1 copo de leite de coco",IngredientMatcher.available("leite, leite de coco")).matchesAll(),"Nome composto não substitui outro ingrediente obrigatório");
 require(IngredientMatcher.score("1 xícara de farinha de trigo",IngredientMatcher.available("farinha, farinha de trigo")).matchesAll(),"Duas buscas podem corresponder à mesma linha");

 System.out.println("PASS: filtro exige TODOS os ingredientes;  busca por múltiplos ingredientes, quantidades, acentos, duplicados, compostos e correspondência por palavra");
 }
}
