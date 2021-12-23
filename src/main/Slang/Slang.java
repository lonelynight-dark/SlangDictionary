package main.Slang;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * com.GUI.Slang
 * Created by Hieu Tran Trung
 * Date 12/17/2021 - 8:55 PM
 * Description: ...
 */
public class Slang implements Serializable {
    private String word;
    private ArrayList<String> definitionList = new ArrayList<>();

    public Slang(String word) {
        this.word = word;
    }

    public Slang(String word, String[] definitionList) {
        this.word = word;
        for (String s : definitionList) {
            if (!s.isBlank())
                this.definitionList.add(s);
        }
    }

    public Slang(String word, String definition) {
        this.word = word;
        if (definition != null) {
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
        str.append("\n");

        return str.toString();
    }

    public static Slang fromString(String str) {
        String[] part = str.split("`");

        if (part.length != 2) {
            return null;
        }

        String[] list = part[1].split("\\|");
        return new Slang(part[0], list);
    }

    public void addDefinition(ArrayList<String> definitionList) {
        this.definitionList.addAll(definitionList);
    }

    public void addDefinition(String definition) {
        definitionList.add(definition);
    }
}
