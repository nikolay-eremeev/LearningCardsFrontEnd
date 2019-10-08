package com.example.myapplicationjava;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    
    private TextView mTextViewLearnWord;
    private TextView mTextViewResult;
    private TextView mTextViewRemember;

    private EditText mEditTextUserInput;
    private Button mButtonNext;
    private Button mButtonClean;
    private LearningCards mLearningCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted TODO handling
        }

        mLearningCards = new LearningCards(this.getBaseContext());

        mTextViewLearnWord = findViewById(R.id.textViewLearnWord);
        mTextViewLearnWord.setText(mLearningCards.getWord());

        mTextViewRemember = findViewById(R.id.textViewRemember);
        mTextViewRemember.setText(mLearningCards.getWord());

        mTextViewResult = findViewById(R.id.textViewResult);

        mButtonNext = findViewById(R.id.buttonNext);
        mButtonClean = findViewById(R.id.buttonClean);

        mEditTextUserInput = findViewById(R.id.editTextUserInput);

        mEditTextUserInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userText = mEditTextUserInput.getText().toString();
                if (userText.isEmpty()) {
                    mTextViewResult.setText(getResources().getString(R.string.empty));
                    mTextViewResult.setTextColor(getResources().getColor(R.color.colorText));
                } else if (mLearningCards.isTranslationRight(userText)) {
                    mTextViewResult.setText(getResources().getString(R.string.title_right_answer));
                    mTextViewResult.setTextColor(getResources().getColor(R.color.colorRightAnswer));
                    pickWord();
                } else {
                    mTextViewResult.setText(getResources().getString(R.string.title_wrong_answer));
                    mTextViewResult.setTextColor(getResources().getColor(R.color.colorWrongAnswer));
                }
            }
        });

        mButtonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mTextViewRemember.setText(mLearningCards.getWord() + "\n" + mLearningCards.getTranslation());
                mEditTextUserInput.getText().clear();
                pickWord();
            }
        });

        mButtonClean.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mEditTextUserInput.getText().clear();
            }
        });

    }

    public void pickWord() {
        mLearningCards.pickWordToLearn();
        mTextViewLearnWord.setText(mLearningCards.getWord());
    }

}
