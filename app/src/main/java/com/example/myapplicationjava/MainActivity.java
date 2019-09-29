package com.example.myapplicationjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    
    private TextView mTextViewLearnWord;
    private TextView mTextViewResult;
    private EditText mEditTextUserInput;
    private Dictionary mDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDictionary = new Dictionary();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);
        }

        mTextViewLearnWord = findViewById(R.id.textView);
        mTextViewLearnWord.setText(mDictionary.getEng());

        mTextViewResult = findViewById(R.id.textView2);

        mEditTextUserInput = findViewById(R.id.editText);
        mEditTextUserInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userText = mEditTextUserInput.getText().toString();
                String questionedText = mTextViewLearnWord.getText().toString();
                if (userText.equals("")) {
                    mTextViewResult.setText("ENTER THE TRANSLATION");
                    mTextViewResult.setTextColor(Color.BLACK);
                } else if (mDictionary.getTranslation(questionedText).equals(userText)) {
                    mTextViewResult.setText("GOOD, NEXT WORD IS:");
                    mTextViewResult.setTextColor(Color.GREEN);
                    pickWord();
                } else {
                    mTextViewResult.setText("FALSE");
                    mTextViewResult.setTextColor(Color.RED);
                }
            }
        });

    }

    public void pickWord() {
        mDictionary.pickToLearn();
        mTextViewLearnWord.setText(mDictionary.getEng());
    }

}
