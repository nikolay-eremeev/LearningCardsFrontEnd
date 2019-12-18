package com.example.learningcards;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

class LearningCards implements ILearningCards {
    private static final String TAG = LearningCards.class.getSimpleName();
    private ArrayList<Card> cardsArrayList = new ArrayList<>();
    private Card activeCard;
    private int activeCardNumber;
    private int activeWordNumber;

    LearningCards(String hostname, int port) {
        getData(hostname, port);
        this.pickWordToLearn();
    }

    void getData(String hostname, int port) {
        String[] inputArr;
        try (Socket socket = new Socket(hostname, port)) {
            InputStream input = socket.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
            String inputString = buffer.readLine();
            inputArr = inputString.split(":::");
            buffer.close();
            for (String str : inputArr) {
                cardsArrayList.add(new Card(str.split("\t")));
            }

        } catch (UnknownHostException ex) {
            Log.e(TAG, "Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            Log.e(TAG,"I/O error: " + ex.getMessage());
        }

    }

    private void pickWordToLearn() {

        activeCardNumber = (new Random()).nextInt(cardsArrayList.size());
        activeCard = cardsArrayList.get(activeCardNumber);
        activeWordNumber = (new Random()).nextInt(2);

    }

    private String formatString(String str) {
        return str.replaceAll("[^a-zA-Zа-яА-Я]", "").toLowerCase();
    }

    @Override
    public String getWord() {
        return activeCard.getWord(activeWordNumber);
    }

    @Override
    public String getWordLanguage() {
        return activeCard.getWordLanguage(activeWordNumber);
    }

    @Override
    public String getTranslation() {
        return activeCard.getWord(1 - activeWordNumber);
    }

    @Override
    public String getTranslationLanguage() {
        return activeCard.getTranslationLanguage(1 - activeWordNumber);
    }

    @Override
    public int getScore() {
        return activeCard.getScore();
    }

    @Override
    public boolean pushAnswer(String userAnswer) {

        String userTextTmp = formatString(userAnswer);
        String translation = formatString(this.getTranslation());
        boolean isAnswerRight = userTextTmp.equals(translation);

        activeCard.pushAnswer(isAnswerRight);

        cardsArrayList.set(activeCardNumber, activeCard);
        this.pickWordToLearn();

        return isAnswerRight;
    }

}


