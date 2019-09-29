package com.example.myapplicationjava;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Dictionary {

    private List<Learner> data;
    private Learner mCurrentLearn;
    private File mFile;
    private int ind;

    Dictionary() {
        data = new ArrayList();
        mFile = new File(Environment.getDownloadCacheDirectory().getPath() + "/dctnr.txt");
        Scanner input;

        try {
            input = new Scanner(mFile);
            while (input.hasNextLine()) {
                data.add(new Learner(input.nextLine().split("\t")));
            }
        } catch (FileNotFoundException e) {
            //do something with e, or handle this case
        }

        this.pickToLearn();

    }

    void pickToLearn() {
        Random rand = new Random();
        String[] p = {"","","","",""};

        if (data.size() == 0)
            mCurrentLearn = new Learner(p);
        else {
            int randIndex = rand.nextInt(data.size());
            mCurrentLearn = data.get(randIndex);
            ind = rand.nextInt(2);
        }

    }

    String getWord() {
        return mCurrentLearn.getWord(ind);
    }

    String getTranslation(String toTranslate) {
        return mCurrentLearn.getWord(1 - ind);
    }
}
