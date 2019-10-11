package com.example.learningcards;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class LearningCardsImpl implements LearningCardsInterface {
    private static final String TAG = "LearningCardsImpl";
    private ArrayList<Card> cardsArrayList;
    private Card activeCard;
    private int activeCardNumber;
    private int activeWordNumber;
    private String rememberString;

    LearningCardsImpl(String dctPath) {
        cardsArrayList = new ArrayList<>();
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

    private String formatString(String str) {
        return str.replaceAll("[^a-zA-Zа-яА-Я]", "").toLowerCase();
    }

    @Override
    public String getWord() {
        return activeCard.getWord(activeWordNumber);
    }

    @Override
    public String getTranslation() {
        return activeCard.getWord(1 - activeWordNumber);
    }

    @Override
    public double getScore() {
        return activeCard.getScore();
    }

    @Override
    public boolean isTranslationRight(String userText) {
        String userTextTmp = formatString(userText);
        String translation = formatString(this.getTranslation());

        return userTextTmp.equals(translation);
    }

    @Override
    public void answer(boolean isAnswerRight) {
        activeCard.answer(isAnswerRight);
        rememberString = getWord() + "\n" + getTranslation()
                + "\n\n" + getScore() + "\n" + activeCard.getRate();
        cardsArrayList.set(activeCardNumber, activeCard);
        this.pickWordToLearn();
    }

    @Override
    public String getRememberString() {
        return rememberString;
    }
}
