package com.example.myapplicationjava;

public class Learner {

    public Learner (String [] args) {
        this.mLanguage1 = args[0];
        this.mLanguage2 = args[1];
        this.mWord1 = args[2];
        this.mWord2 = args[3];
        if (args.length == 4)
            this.mRate = 1.0;
        else
            this.mRate = Double.parseDouble(args[4]);
    }

    private String mLanguage1;
    private String mLanguage2;
    private String mWord1;
    private String mWord2;
    private double mRate;

    public double getRate() {
        return mRate;
    }

    public double getScore() {
        final double score = 1 / mRate;
        return score;
    }

    public String getWord1 () {
        return mWord1;
    }

    public String getWord2() {
        return mWord2;
    }

    public String getTranslation(String word) {
        if (word.equals(mWord2))
            return mWord1;
        else
            return mWord2;
    }

}
