package com.uchi.learningcards;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

class LearningCards {
    private static final String TAG = LearningCards.class.getSimpleName();
    private ArrayList<Card> cardsArrayList = new ArrayList<>();
    private Card activeCard;
    private int activeCardNumber;

    LearningCards(String hostname, int port) throws IOException {
        getData(hostname, port);
        this.pickWordToLearn();
    }

    private void getData(String hostname, int port) throws IOException {
        String[] inputArr;

            InetSocketAddress endPoint = new InetSocketAddress(hostname, port);
            Socket socket = new Socket();
            socket.connect(endPoint, 10000);

            InputStream input = socket.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
            String inputString = buffer.readLine();
            inputArr = inputString.split(":::");
            buffer.close();
            for (String str : inputArr) {
                cardsArrayList.add(new Card(str.split("\t")));
            }
    }

    private void pickWordToLearn() {
        activeCardNumber = (new Random()).nextInt(cardsArrayList.size());
        activeCard = cardsArrayList.get(activeCardNumber);
    }

    public String getWord() {
        return activeCard.getWord();
    }

    public ArrayList<String> getTranslations() {
        return activeCard.getTranslations();
    }

    public String getWordLanguage() {
        return activeCard.getWordLanguage();
    }

    public String getTranslationLanguage() {
        return activeCard.getTranslationLanguage();
    }

    public int getScore() {
        return activeCard.getScore();
    }

    public void setScore(int score) {
        activeCard.setScore(score);
    }

    private String formatString(String str) {
        return str.replaceAll("[^a-zA-Zа-яА-Я]", "").toLowerCase();
    }

    public boolean pushAnswer(String userAnswer) {
        String userTextTmp = formatString(userAnswer);
        ArrayList<String> translations = this.getTranslations();
        boolean isAnswerRight = false;
        for (String translation : translations) {
            translation = formatString(translation);
            if (userTextTmp.equals(translation)) {
                isAnswerRight = true;
                break;
            }
        }
        activeCard.pushAnswer(isAnswerRight);
        cardsArrayList.set(activeCardNumber, activeCard);
        this.pickWordToLearn();
        return isAnswerRight;
    }
}


