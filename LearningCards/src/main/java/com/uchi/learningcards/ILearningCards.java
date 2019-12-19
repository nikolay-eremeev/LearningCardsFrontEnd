package com.uchi.learningcards;

import java.util.ArrayList;

public interface ILearningCards {

    String getWord();

    ArrayList<String> getTranslations();

    String getWordLanguage();

    String getTranslationLanguage();

    int getScore();

    void setScore(int score);

    boolean pushAnswer(String userAnswer);

}
