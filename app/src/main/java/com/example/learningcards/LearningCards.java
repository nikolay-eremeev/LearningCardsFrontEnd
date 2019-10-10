package com.example.learningcards;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class LearningCards {

    private static final String TAG = "LearningCards";
    private ArrayList<Card> cardsArrayList;
    private Card activeCard;
    private int activeCardNumber;
    private int activeWordNumber;
    private String rememberString;

    LearningCards(Activity context) {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }

        cardsArrayList = new ArrayList<>();
        String dctPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + context.getString(R.string.dct_file_name);

        try {
            Scanner input = new Scanner(new File(dctPath));
            while (input.hasNextLine()) {
                cardsArrayList.add(new Card(input.nextLine().split("\t")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.pickWordToLearn();

    }

    private void pickWordToLearn() {

        Random rand = new Random();

        if (cardsArrayList.size() == 0) {
            Log.w(TAG, "There are no cards, please check input file");
        } else {
            activeCardNumber = rand.nextInt(cardsArrayList.size());
            activeCard = cardsArrayList.get(activeCardNumber);
            activeWordNumber = rand.nextInt(2);
        }

    }

    String getWord() {
        return activeCard.getWord(activeWordNumber);
    }

    String getTranslation() {
        return activeCard.getWord(1 - activeWordNumber);
    }

    double getScore() {
        return activeCard.getScore();
    }

    double getRate() {
        return activeCard.getRate();
    }

    boolean isTranslationRight(String userText) {
        String userTextTmp = formatString(userText);
        String translation = formatString(this.getTranslation());

        return userTextTmp.equals(translation);
    }

    /**
     * @param isAnswerRight true - answer is correct, false - answer is wrong
     */
    void answer(boolean isAnswerRight) {
        activeCard.answer(isAnswerRight);
        rememberString = getWord() + "\n" + getTranslation()
                + "\n\n" + getScore() + "\n" + getRate();
        cardsArrayList.set(activeCardNumber, activeCard);
        this.pickWordToLearn();
    }

    public String getRememberString() {
        return rememberString;
    }

    private String formatString(String str) {
        return str.replaceAll("[^a-zA-Zа-яА-Я]", "").toLowerCase();
    }

}
