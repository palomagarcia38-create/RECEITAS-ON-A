package br.com.mariacarmela.receitas;
import java.text.Normalizer;
import java.util.*;
final class IngredientMatcher {
 static final Set<String> UNITS=new HashSet<>(Arrays.asList("xicara","xicaras","colher","colheres","grama","gramas","g","kg","ml","litro","litros","l","unidade","unidades","de","da","do","das","dos","em","a","o","os","as"));
 static final Set<String> HEADINGS=new HashSet<>(Arrays.asList("massa","cobertura","recheio","calda","ingredientes","para a massa","para a cobertura","para o recheio"));
 static String normalize(String s){return Normalizer.normalize(s,Normalizer.Form.NFD).replaceAll("\\p{M}","").toLowerCase(Locale.ROOT);}
 static String canonical(String s){String value=normalize(s).replaceAll("\\((cha|sopa|cafe)\\)"," ").replace("a gosto"," ").replaceAll("[^\\p{L} ]"," ");List<String> words=new ArrayList<>();for(String word:value.split("\\s+")){if(word.isEmpty()||UNITS.contains(word))continue;if(word.equals("ovos"))word="ovo";else if(word.length()>3&&word.endsWith("s")&&!word.endsWith("is"))word=word.substring(0,word.length()-1);words.add(word);}return String.join(" ",words);}
 static List<String> available(String raw){LinkedHashSet<String> result=new LinkedHashSet<>();for(String item:raw.split("[,;\\n\\r]+|\\s+e\\s+")){String key=canonical(item);if(!key.isEmpty())result.add(key);if(result.size()>=100)break;}return new ArrayList<>(result);}
 static boolean matches(String recipe,String available){if(available.equals("leite")&&(recipe.contains("leite coco")||recipe.contains("leite condensado")||recipe.contains("leite po")))return false;String full=" "+recipe+" ";if(full.contains(" "+available+" "))return true;return false;}
 static final class Result {final List<String> found=new ArrayList<>(),missing=new ArrayList<>();int required,matched;boolean matchesAll(){return matched==required;}int total(){return found.size()+missing.size();}}
 static Result score(String recipe,List<String> pantry){Result result=new Result();result.required=pantry.size();Set<String> matchedItems=new HashSet<>();LinkedHashSet<String> seen=new LinkedHashSet<>();for(String line:recipe.split("\\r?\\n")){String title=normalize(line.trim().replace(":",""));if(title.isEmpty()||HEADINGS.contains(title))continue;String key=canonical(line);if(key.isEmpty()||!seen.add(key))continue;boolean present=false;for(String item:pantry)if(matches(key,item)){present=true;matchedItems.add(item);}if(present)result.found.add(line.trim());else result.missing.add(line.trim());}result.matched=matchedItems.size();return result;}
}
