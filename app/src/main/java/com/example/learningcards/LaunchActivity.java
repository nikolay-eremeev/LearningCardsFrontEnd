package com.example.learningcards;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LaunchActivity.class.getSimpleName();
    static private ILearningCards myLearningCards;
    private TextView resultTextView;
    private TextView rememberTextView;
    private EditText userInputEditText;
    private Switch ttsSwitch;
    private TextToSpeech myTTS;
    boolean isTtsOn = false;

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
                    } else {

                    }
                } else
                    Log.e(TAG, "Initilization Failed!");
            }
        });

        myTTS.setLanguage(Locale.ENGLISH);

        resultTextView = findViewById(R.id.textViewResult);
        rememberTextView = findViewById(R.id.textViewRemember);
        userInputEditText = findViewById(R.id.editTextUserInput);
        ttsSwitch = findViewById(R.id.switch1);
        findViewById(R.id.buttonClean).setOnClickListener(this);
        findViewById(R.id.buttonNext).setOnClickListener(this);
        ttsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isTtsOn = true;
                } else {
                    isTtsOn = false;
                }
            }
        });

        new Thread() {
            public void run() {
                if (myLearningCards == null) {

                    Writer writer = new StringWriter();
                    char[] buffer = new char[1024];
                    int n;

                    try {
                        InputStream is = getResources().openRawResource(R.raw.credentials);
                        Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                        while ((n = reader.read(buffer)) != -1) {
                            writer.write(buffer, 0, n);
                        }
                        is.close();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    JSONObject obj;
                    try {
                        obj = new JSONObject(writer.toString());
                        String host = obj.getString("hostname");
                        int port = obj.getInt("port");
                        myLearningCards = new LearningCards(host, port);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                userInputEditText.setHint(myLearningCards.getWord());

            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:

                if (myLearningCards != null) {
                    String userText = userInputEditText.getText().toString();
                    userInputEditText.getText().clear();

                    int resultColor;
                    String resultTitle;

                    if (isTtsOn) {


                        if (myLearningCards.getTranslationLanguage().equals("English"))
                            myTTS.setLanguage(Locale.US);
                        else if (myLearningCards.getTranslationLanguage().equals("Russian"))
                            myTTS.setLanguage(new Locale("ru"));

                        myTTS.speak(myLearningCards.getTranslation(), QUEUE_ADD, null, "id");

                        if (myLearningCards.getWordLanguage().equals("English"))
                            myTTS.setLanguage(Locale.US);
                        else if (myLearningCards.getWordLanguage().equals("Russian"))
                            myTTS.setLanguage(new Locale("ru"));

                        myTTS.speak(myLearningCards.getWord(), QUEUE_ADD, null, "id");
                    }
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
                } else {
                    Log.e(TAG, "LearningCards are not initialized");
                }
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
