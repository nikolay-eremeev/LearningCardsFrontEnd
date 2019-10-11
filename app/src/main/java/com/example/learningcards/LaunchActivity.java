package com.example.learningcards;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener {

    static private LearningCards myLearningCards;
    private TextView resultTextView;
    private TextView rememberTextView;
    private EditText userInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        resultTextView = findViewById(R.id.textViewResult);
        rememberTextView = findViewById(R.id.textViewRemember);
        userInputEditText = findViewById(R.id.editTextUserInput);
        findViewById(R.id.buttonClean).setOnClickListener(this);
        findViewById(R.id.buttonNext).setOnClickListener(this);

        if (myLearningCards == null)
            myLearningCards = new LearningCards(this);

        userInputEditText.setHint(myLearningCards.getWord());
        rememberTextView.setText(myLearningCards.getRememberString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                String userText = userInputEditText.getText().toString();
                userInputEditText.getText().clear();

                int resultColor;
                String resultTitle;
                boolean isAnswerRight = myLearningCards.isTranslationRight(userText);
                myLearningCards.answer(isAnswerRight);
                rememberTextView.setText(myLearningCards.getRememberString());

                if (isAnswerRight) {
                    resultColor = getResources().getColor(R.color.colorPrimary);
                    resultTitle = getResources().getString(R.string.title_right_answer);
                } else {
                    resultColor = getResources().getColor(R.color.colorAccent);
                    resultTitle = getResources().getString(R.string.title_wrong_answer);
                }

                resultTextView.setText(resultTitle);
                resultTextView.setTextColor(resultColor);
                rememberTextView.setTextColor(resultColor);

                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    resultTextView.setText("");
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                userInputEditText.setHint(myLearningCards.getWord());
                break;

            case R.id.buttonClean:
                userInputEditText.getText().clear();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO save user's LearningCards
    }
}
