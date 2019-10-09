package com.example.myapplicationjava;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class LearningCards {

    private static final String TAG = "LearningCards";
    private ArrayList<Card> mData;
    private Card mCurrentCard;
    private int mCurrentCardNr;
    private int mCurrentWordNr;

    LearningCards(Context context) {

        mData = new ArrayList<>();
        String dctPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + context.getString(R.string.dct_file_name);

        try {
            Scanner input = new Scanner(new File(dctPath));
            while (input.hasNextLine()) {
                mData.add(new Card(input.nextLine().split("\t")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.pickWordToLearn();

    }

    void pickWordToLearn() {

        Random rand = new Random();

        if (mData.size() == 0) {
            Log.w(TAG, "There are no cards, please check input file");
        } else {
            mCurrentCardNr = rand.nextInt(mData.size());
            mCurrentCard = mData.get(mCurrentCardNr);
            mCurrentWordNr = rand.nextInt(2);
        }

    }

    String getWord() {
        return mCurrentCard.getWord(mCurrentWordNr);
    }

    String getTranslation() {
        return mCurrentCard.getWord(1 - mCurrentWordNr);
    }

    double getScore() {
        return mCurrentCard.getScore();
    }

    double getRate() {
        return mCurrentCard.getRate();
    }

    boolean isTranslationRight(String userText) {

        String userTextTmp = formatString(userText);
        String translation = formatString(this.getTranslation());

        if (userTextTmp.equals(translation)) {
            mCurrentCard.answerRight();
            mData.set(mCurrentCardNr, mCurrentCard);
            return true;
        } else {
            mCurrentCard.answerWrong();
            mData.set(mCurrentCardNr, mCurrentCard);
            return false;
        }

    }

    void answerRight() {
        mCurrentCard.answerRight();
    }

    void answerWrong() {
        mCurrentCard.answerWrong();
    }

    private String formatString(String str) {
        return str.replaceAll("[^a-zA-Zа-яА-Я]", "").toLowerCase();
    }

}
