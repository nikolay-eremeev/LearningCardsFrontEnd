package com.uchi.learningcards;

import java.util.ArrayList;
import java.util.Arrays;

class Card {

    private String word;
    private ArrayList<String> translations = new ArrayList<>();
    private String wordLanguage;
    private String translationLanguage;
    private int score;

    Card(String[] args) {
        this.wordLanguage = args[0];
        this.translationLanguage = args[1];
        this.word = args[2];
        translations.addAll(Arrays.asList(args).subList(3, args.length));
        this.score = 0;
    }

    int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = score;
    }

    String getWord() {
        return word;
    }

    String getWordLanguage() {
        return wordLanguage;
    }

    String getTranslationLanguage() {
        return translationLanguage;
    }

    ArrayList<String> getTranslations() {
        return translations;
    }

    void pushAnswer(boolean isAnswerRight) {
        if (isAnswerRight) {
            score++;
        } else {
            score--;
        }
    }

}
