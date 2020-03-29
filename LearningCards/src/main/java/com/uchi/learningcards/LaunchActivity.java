package com.uchi.learningcards;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LaunchActivity.class.getSimpleName();
    static private LearningCards myLearningCards;
    boolean isTtsOn = false;
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
                if (status == TextToSpeech.SUCCESS) {
                    int result = myTTS.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e(TAG, "This Language is not supported");
                    }
                } else
                    Log.e(TAG, "Initialization Failed!");
            }
        });

        myTTS.setLanguage(Locale.ENGLISH);
        resultTextView = findViewById(R.id.textViewResult);
        rememberTextView = findViewById(R.id.textViewRemember);
        userInputEditText = findViewById(R.id.editTextUserInput);
        Switch ttsSwitch = findViewById(R.id.switch1);
        findViewById(R.id.buttonClean).setOnClickListener(this);
        findViewById(R.id.buttonNext).setOnClickListener(this);
        ttsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isTtsOn = isChecked;
            }
        });


        new Thread() {
            public void run() {
                if (myLearningCards == null) {

                    JSONObject jsonObject = null;
                    try {
                        InputStream inputStream = getResources().openRawResource(R.raw.host_info);
                        JSONParser jsonParser = new JSONParser();
                        jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                        inputStream.close();
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }

                    assert jsonObject != null;
                    String host = Objects.requireNonNull(jsonObject.get("hostname")).toString();
                    int port = ((Long) Objects.requireNonNull(jsonObject.get("port"))).intValue();
                    try {
                        myLearningCards = new LearningCards(host, port);
                        userInputEditText.setHint(myLearningCards.getWord());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                if (myLearningCards == null) {
                    Toast.makeText(LaunchActivity.this, "Learning Cards are not initialized, please check connection", Toast.LENGTH_LONG).show();
                    break;
                }

                String userText = userInputEditText.getText().toString();
                userInputEditText.getText().clear();

                int resultColor;
                String resultTitle;

                if (isTtsOn) {
                    if (myLearningCards.getTranslationLanguage().equals("English"))
                        myTTS.setLanguage(Locale.US);
                    else if (myLearningCards.getTranslationLanguage().equals("Russian"))
                        myTTS.setLanguage(new Locale("ru"));

                    myTTS.speak(myLearningCards.getTranslations().get(0), QUEUE_ADD, null, "id");

                    if (myLearningCards.getWordLanguage().equals("English"))
                        myTTS.setLanguage(Locale.US);
                    else if (myLearningCards.getWordLanguage().equals("Russian"))
                        myTTS.setLanguage(new Locale("ru"));

                    myTTS.speak(myLearningCards.getWord(), QUEUE_ADD, null, "id");
                }

                rememberTextView.setText(getString(R.string.translation_to_show, myLearningCards.getWord(), String.join("; ", myLearningCards.getTranslations()), myLearningCards.getScore()));

                boolean isAnswerRight = myLearningCards.pushAnswer(userText);

                if (isAnswerRight) {

                    resultColor = ContextCompat.getColor(this, R.color.colorPrimary);
                    resultTitle = getResources().getString(R.string.title_right_answer);
                } else {
                    resultColor = ContextCompat.getColor(this, R.color.colorAccent);
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
    }

    @Override
    protected void onDestroy() {
        if (myTTS != null) {
            myTTS.stop();
            myTTS.shutdown();
            Log.d(TAG, "TTS Destroyed");
        }
        super.onDestroy();
    }

}
