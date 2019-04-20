package com.example.shimantoahmed.learnvocabulary.model;

public class SmallWord {
    private String word;
    private String type;
    private String def;

    public SmallWord(String word, String type, String def) {
        this.word = word;
        this.type = type;
        this.def = def;
    }

    @Override
    public String toString() {
        return "SmallWord{" +
                "word='" + word + '\'' +
                ", type='" + type + '\'' +
                ", def='" + def + '\'' +
                '}';
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }
}
