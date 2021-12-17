package Slang;

/**
 * Slang
 * Created by Hieu Tran Trung
 * Date 12/17/2021 - 8:55 PM
 * Description: ...
 */
public class Slang {
    private String word;
    private String definition;

    public Slang(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public String getKey() {
        return word;
    }

    public void setKey(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return word + "`" + definition;
    }

    public static Slang fromString(String str) {
        String[] part = str.split("`");

        if (part.length != 2) {
            return null;
        }

        return new Slang(part[0], part[1]);
    }
}
