package com.example.learningcards;

public interface ILearningCards {

    String getWord();

    String getWordLanguage();

    String getTranslation();

    String getTranslationLanguage();

    int getScore();

    boolean pushAnswer(String userAnswer);

}
