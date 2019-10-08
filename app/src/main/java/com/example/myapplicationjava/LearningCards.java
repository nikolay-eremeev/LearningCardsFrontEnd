package com.example.myapplicationjava;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class LearningCards {

    private ArrayList<Card> mData;
    private Card mCurrentCard;
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
            //TODO log e.getMessage());
        }

        this.pickWordToLearn();

    }

    void pickWordToLearn() {

        Random rand = new Random();

        if (mData.size() == 0) {
            //TODO handle the case when mData has no entries
        }
        else {
            int randIndex = rand.nextInt(mData.size());
            mCurrentCard = mData.get(randIndex);
            mCurrentWordNr = rand.nextInt(2);
        }

    }

    String getWord() {
        return mCurrentCard.getWord(mCurrentWordNr);
    }

    String getTranslation() {
        return mCurrentCard.getWord(1 - mCurrentWordNr);
    }

    boolean isTranslationRight(String userText) {

        String userTextTmp = userText.replaceAll("[^a-zA-Z а-яА-Я]", "").toLowerCase();
        String translation = this.getTranslation().replaceAll("[^a-zA-Z а-яА-Я]", "").toLowerCase();

        return userTextTmp.equals(translation);
    }
}
