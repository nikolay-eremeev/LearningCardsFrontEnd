package com.example.myapplicationjava;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Dictionary {

    private List<Learner> data;
    private Learner mCurrentLearn;
    private File mFile;

    Dictionary() {
        data = new ArrayList();
        mFile = new File("/sdcard/download/dctnr.txt");
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

    public void pickToLearn() {
        Random rand = new Random();
        String[] p = {"","","","",""};

        if (data.size() == 0)
            mCurrentLearn = new Learner(p);
        else {
            int randIndex = rand.nextInt(10);
            mCurrentLearn = data.get(randIndex);
        }

    }

    public String getEng() {
        return mCurrentLearn.getWord1();
    }

    public String getRus() {
        return mCurrentLearn.getWord2();
    }

    public String getTranslation(String toTranslate) {
        return mCurrentLearn.getTranslation(toTranslate);
    }
}
