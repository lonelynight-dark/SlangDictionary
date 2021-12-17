package Slang;

import java.util.ArrayList;

/**
 * Slang
 * Created by Hieu Tran Trung
 * Date 12/17/2021 - 8:55 PM
 * Description: ...
 */
public class Slang {
    private String word;
    private ArrayList<String> definitionList;

    public Slang(String word) {
        this.word = word;
    }

    public Slang(String word, String definition) {
        this.word = word;
        if (definition != null) {
            assert false;
            definitionList.add(definition);
        }
    }

    public String getWord() {
        return word;
    }

    public ArrayList<String> getDefinitionList() {
        return definitionList;
    }

    public void setDefinitionList(ArrayList<String> definitionList) {
        this.definitionList = definitionList;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(word + "`");
        for (int i = 0; i < definitionList.size(); i++) {
            str.append(definitionList.get(i));
            if (i != definitionList.size() - 1) str.append("|");
        }
        return str.toString();
    }

    public static Slang fromString(String str) {
        String[] part = str.split("`");

        if (part.length != 2) {
            return null;
        }
        Slang slang = new Slang(part[0]);

        String[] defi = part[1].split("\\|");
        for (String d : defi) slang.addDefinition(d);
        return slang;
    }

    public void addDefinition(String definition) {
        definitionList.add(definition);
    }
}
