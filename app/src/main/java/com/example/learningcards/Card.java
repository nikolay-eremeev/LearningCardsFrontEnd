package com.example.learningcards;

class Card {

    private String language1;
    private String language2;
    private String word1;
    private String word2;
    private double score;
    private double rate;

    Card(String[] args) {
        this.language1 = args[0];
        this.language2 = args[1];
        this.word1 = args[2];
        this.word2 = args[3];
        if (args.length == 4) {
            this.score = 1.0;
            this.rate = 1.0;
        } else {
            this.score = Double.parseDouble(args[4]);
            this.rate = 1.0 / score;
        }
    }

    double getRate() {
        return rate;
    }

    double getScore() {
        return score;
    }

    String getWord(int nr) {
        switch (nr) {
            case (0):
                return word1;
            case (1):
                return word2;
            default:
                //TODO log error - nt must be 0 or 1
                return "";
        }
    }

    void answer(boolean isAnswerRight) {
        if (isAnswerRight) {
            score *= 1.5;
        } else {
            score /= 2;
        }
        rate = 1.0 / score;
    }

}
