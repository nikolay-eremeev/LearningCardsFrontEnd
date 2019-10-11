package com.example.learningcards;

public class LearningCards implements LearningCardsInterface {

    LearningCardsInterface delegate;

    LearningCards(String dct) {
        delegate = new LearningCardsImpl(dct);
    }

    @Override
    public String getWord() {
        return delegate.getWord();
    }

    @Override
    public String getTranslation() {
        return delegate.getTranslation();
    }

    @Override
    public double getScore() {
        return delegate.getScore();
    }

    public boolean isTranslationRight(String userText) {
        return delegate.isTranslationRight(userText);
    }

    @Override
    public void answer(boolean isAnswerRight) {
        delegate.answer(isAnswerRight);
    }

    @Override
    public String getRememberString() {
        return delegate.getRememberString();
    }

}
