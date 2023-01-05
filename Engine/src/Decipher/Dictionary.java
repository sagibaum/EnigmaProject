package Decipher;

import schem.out.CTEDictionary;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Dictionary {

    Set<String> dictionaryWords;
    String excludeChars;

    public Dictionary(CTEDictionary dictionary) {
        this.excludeChars = dictionary.getExcludeChars();
        StringTokenizer st = new StringTokenizer(dictionary.getWords().trim(), " ");
        dictionaryWords = new HashSet<String>();
        while (st.hasMoreTokens()) {
            dictionaryWords.add(deleteExcludedChars(st.nextToken()).toUpperCase());//throw exception if one of them isn't  int
        }

    }
// words.replaceAll(String.valueOf(excludedCharacters),"");
    private String deleteExcludedChars(String word){
        //String dictionaryWords =word;
        StringBuilder dictionaryWords = new StringBuilder(word);

        for (int i = 0; i < excludeChars.length(); i++) {
            if(word.contains(String.valueOf(excludeChars.charAt(i)))){
            dictionaryWords.deleteCharAt(dictionaryWords.indexOf(String.valueOf(excludeChars.charAt(i))));}
            //dictionaryWords.replace(String.valueOf(excludeChars.charAt(i)),"");}
        }
        return String.valueOf(dictionaryWords);
    }
    public Set<String> getDictionaryWords() {
        return dictionaryWords;
    }

    public void setDictionaryWords(Set<String> dictionaryWords) {
        this.dictionaryWords = dictionaryWords;
    }

    public String getExcludeChars() {
        return excludeChars;
    }

    public void setExcludeChars(String excludeChars) {
        this.excludeChars = excludeChars;
    }
}
