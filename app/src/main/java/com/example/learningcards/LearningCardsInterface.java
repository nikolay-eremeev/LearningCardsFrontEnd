package com.example.learningcards;

public interface LearningCardsInterface {

    String getWord();

    String getTranslation();

    double getScore();

    boolean isTranslationRight(String userText);

    void answer(boolean isAnswerRight);

    String getRememberString();

}
