package com.example.shimantoahmed.learnvocabulary.model;

/**
 * Created by Shimanto Ahmed on 12/23/2017.
 */

public class Word {
    private int id;
    private String engWord;
    private String bangWord;
    private String bngSyn;
    private String engSyn;
    private String example;
    private String engPron;
    private String bangPron;
    private String type;
    private String definition;
    private String antonyms;


    public Word(int id, String engWord, String bangWord) {
        this.id = id;
        this.engWord = engWord;
        this.bangWord = bangWord;
    }

    public Word(int id, String engWord, String bangWord, String bngSyn, String engSyn, String example, String engPron, String bangPron, String type, String definition, String antonyms) {
        this.id = id;
        this.engWord = engWord;
        this.bangWord = bangWord;
        this.bngSyn = bngSyn;
        this.engSyn = engSyn;
        this.example = example;
        this.engPron = engPron;
        this.bangPron = bangPron;
        this.type = type;
        this.definition = definition;
        this.antonyms = antonyms;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", engWord='" + engWord + '\'' +
                ", bangWord='" + bangWord + '\'' +
                ", bngSyn='" + bngSyn + '\'' +
                ", engSyn='" + engSyn + '\'' +
                ", example='" + example + '\'' +
                ", engPron='" + engPron + '\'' +
                ", bangPron='" + bangPron + '\'' +
                ", type='" + type + '\'' +
                ", definition='" + definition + '\'' +
                ", antonyms='" + antonyms + '\'' +
                '}';
    }

    public String getEngSyn() {
        return engSyn;
    }

    public void setEngSyn(String engSyn) {
        this.engSyn = engSyn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(String antonyms) {
        this.antonyms = antonyms;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEngPron() {
        return engPron;
    }

    public void setEngPron(String engPron) {
        this.engPron = engPron;
    }

    public String getBangPron() {
        return bangPron;
    }

    public void setBangPron(String bangPron) {
        this.bangPron = bangPron;
    }

    public void setEngWord(String engWord) {
        this.engWord = engWord;
    }

    public void setBangWord(String bangWord) {
        this.bangWord = bangWord;
    }

    public void setBngSyn(String bngSyn) {
        this.bngSyn = bngSyn;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getEngWord() {
        return engWord;
    }

    public String getBangWord() {
        return bangWord;
    }

    public String getBngSyn() {
        return bngSyn;
    }

    public String getExample() {
        return example;
    }
}
