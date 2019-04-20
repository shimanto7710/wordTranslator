package com.example.shimantoahmed.learnvocabulary.model;

public class ActualWordList {

    String ActualWord;
    Word word;

    public ActualWordList(String actualWord, Word word) {
        ActualWord = actualWord;
        this.word = word;
    }

    @Override
    public String toString() {
        return "ActualWordList{" +
                "ActualWord='" + ActualWord + '\'' +
                ", word=" + word +
                '}';
    }

    public String getActualWord() {
        return ActualWord;
    }

    public void setActualWord(String actualWord) {
        ActualWord = actualWord;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
