package com.example.myapplicationjava;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvResult;
    private TextView tvRemember;

    private EditText etUserInput;

    private LearningCards myLearningCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        tvResult = findViewById(R.id.textViewResult);
        tvRemember = findViewById(R.id.textViewRemember);
        etUserInput = findViewById(R.id.editTextUserInput);

        myLearningCards = new LearningCards(this.getBaseContext());
        etUserInput.setHint(myLearningCards.getWord());

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonNext:
                String userText = etUserInput.getText().toString();
                etUserInput.getText().clear();
                tvRemember.setText(
                        myLearningCards.getWord()
                                + "\n" + myLearningCards.getTranslation()
                                + "\n\n" + myLearningCards.getScore()
                                + "\n" + myLearningCards.getRate()
                );

                if (myLearningCards.isTranslationRight(userText)) {
                    myLearningCards.answerRight();
                    tvResult.setText(getResources().getString(R.string.title_right_answer));
                    tvResult.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tvRemember.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    myLearningCards.answerWrong();
                    tvResult.setText(getResources().getString(R.string.title_wrong_answer));
                    tvResult.setTextColor(getResources().getColor(R.color.colorAccent));
                    tvRemember.setTextColor(getResources().getColor(R.color.colorAccent));
                }

                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvResult.setText("");
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                pickWord();
                break;

            case R.id.buttonClean:
                etUserInput.getText().clear();
                break;
        }

    }

    public void pickWord() {
        myLearningCards.pickWordToLearn();
        etUserInput.setHint(myLearningCards.getWord());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO save user's LearningCards
    }
}
