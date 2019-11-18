package com.example.learningcards;

class Card {

    private String language1;
    private String language2;
    private String word1;
    private String word2;
    private int score;

    Card(String[] args) {
        this.language1 = args[0];
        this.language2 = args[1];
        this.word1 = args[2];
        this.word2 = args[3];
        if (args.length == 4) {
            this.score = 0;
        } else {
            this.score = Integer.parseInt(args[4]);
        }
    }

    int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    String getWord(int activeWordNumber) {
        switch (activeWordNumber) {
            case (0):
                return word1;
            case (1):
                return word2;
            default:
                //TODO log error - nt must be 0 or 1
                return "";
        }
    }

    void pushAnswer(boolean isAnswerRight) {
        if (isAnswerRight)
            score++;
        else
            score--;

    }

    public String getWordLanguage(int activeWordNumber) {
        switch (activeWordNumber) {
            case (0):
                return language1;
            case (1):
                return language2;
            default:
                //TODO log error - nt must be 0 or 1
                return "";
        }

    }

    public String getTranslationLanguage(int activeWordNumber) {
        switch (activeWordNumber) {
            case (0):
                return language1;
            case (1):
                return language2;
            default:
                //TODO log error - nt must be 0 or 1
                return "";
        }

    }
}
