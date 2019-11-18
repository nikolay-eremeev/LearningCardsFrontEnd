package com.example.learningcards;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener {

    static private ILearningCards myLearningCards;
    private TextView resultTextView;
    private TextView rememberTextView;
    private EditText userInputEditText;
    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = myTTS.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    } else {

                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        });
        myTTS.setLanguage(Locale.ENGLISH);

        resultTextView = findViewById(R.id.textViewResult);
        rememberTextView = findViewById(R.id.textViewRemember);
        userInputEditText = findViewById(R.id.editTextUserInput);
        findViewById(R.id.buttonClean).setOnClickListener(this);
        findViewById(R.id.buttonNext).setOnClickListener(this);

        if (myLearningCards == null)
            myLearningCards = new LearningCards();

        userInputEditText.setHint(myLearningCards.getWord());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                String userText = userInputEditText.getText().toString();
                userInputEditText.getText().clear();

                int resultColor;
                String resultTitle;

                if (myLearningCards.getTranslationLanguage().equals("English"))
                    myTTS.setLanguage(Locale.US);
                else
                    myTTS.setLanguage(new Locale("ru"));

                myTTS.speak(myLearningCards.getTranslation(), QUEUE_ADD, null, "id");

                if (myLearningCards.getWordLanguage().equals("English"))
                    myTTS.setLanguage(Locale.US);
                else
                    myTTS.setLanguage(new Locale("ru"));

                myTTS.speak(myLearningCards.getWord(), QUEUE_ADD, null, "id");

                rememberTextView.setText(myLearningCards.getWord() + "\n\n" + myLearningCards.getTranslation() + "\n\n" + myLearningCards.getScore());

                boolean isAnswerRight = myLearningCards.pushAnswer(userText);

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
    protected void onStop() {
        super.onStop();
        //TODO save user's LearningCardsImpl
    }


}
