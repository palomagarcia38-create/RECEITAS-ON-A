package br.com.mariacarmela.receitas;
import java.util.*;
public class IngredientMatcherTest {
 static void require(boolean ok,String message){if(!ok)throw new AssertionError(message);}
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
 System.out.println("PASS: busca por múltiplos ingredientes, quantidades, acentos, duplicados, compostos e correspondência por palavra");
 }
}
