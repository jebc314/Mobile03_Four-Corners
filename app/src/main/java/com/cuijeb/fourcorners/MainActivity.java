package com.cuijeb.fourcorners;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mTL;
    private Button mTR;
    private Button mBR;
    private Button mBL;
    private SeekBar mSeekBar;
    private TextView alphabet;
    private TextView rateMessage;
    private EditText mRatingDescription;
    private Button mSubmit;

    private int letter;
    private String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private int rating;
    private String[] ratings;

    private SharedPreferences.Editor editor;

    private boolean oneToast = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTL = findViewById(R.id.top_left_button);
        mTR = findViewById(R.id.top_right_button);
        mBR = findViewById(R.id.bottom_right_button);
        mBL = findViewById(R.id.bottom_left_button);

        mTL.setOnClickListener(this);
        mTR.setOnClickListener(this);
        mBR.setOnClickListener(this);
        mBL.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("Button Count", Context.MODE_PRIVATE);
        String tlValue = sharedPreferences.getString("tlValue", "0");
        String trValue = sharedPreferences.getString("trValue", "0");
        String blValue = sharedPreferences.getString("blValue", "0");
        String brValue = sharedPreferences.getString("brValue", "0");
        String fontSize = sharedPreferences.getString("fontSize", "14");
        int fS = (int)Float.parseFloat(fontSize);

        mTL.setText(tlValue);
        mTR.setText(trValue);
        mBR.setText(brValue);
        mBL.setText(blValue);
        mTL.setTextSize(TypedValue.COMPLEX_UNIT_SP, fS);
        mTR.setTextSize(TypedValue.COMPLEX_UNIT_SP, fS);
        mBR.setTextSize(TypedValue.COMPLEX_UNIT_SP, fS);
        mBL.setTextSize(TypedValue.COMPLEX_UNIT_SP, fS);

        editor = sharedPreferences.edit();

        alphabet = findViewById(R.id.alphabet_textview);
        letter = 0;

        ratings = getResources().getStringArray(R.array.ratings);
        rating = 0;
        rateMessage = findViewById(R.id.rating_textview);

        mSeekBar = findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTL.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress);
                mTR.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress);
                mBL.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress);
                mBR.setTextSize(TypedValue.COMPLEX_UNIT_SP, progress);

                double percent = (double)progress / seekBar.getMax();

                letter = (int)(percent * letters.length() + 0.5);
                alphabet.setText(letters.substring(0, letter));

                rating = (int)(percent * ratings.length);
                if (percent < 1) {
                    rateMessage.setText(ratings[rating]);
                }else{
                    rateMessage.setText(ratings[rating-1]); //Novemeber 20th mr tra birthday :)
                }

                double red = 0, green = 0, blue = 0;
                double c = percent;
                if(c >= 0 && c <= (1/6.f)){
                    red = 255;
                    green = 1530 * c;
                    blue = 0;
                } else if( c > (1/6.f) && c <= (1/3.f) ){
                    red = 255 - (1530 * (c - 1/6f));
                    green = 255;
                    blue = 0;
                } else if( c > (1/3.f) && c <= (1/2.f)){
                    red = 0;
                    green = 255;
                    blue = 1530 * (c - 1/3f);
                } else if(c > (1/2f) && c <= (2/3f)) {
                    red = 0;
                    green = 255 - ((c - 0.5f) * 1530);
                    blue = 255;
                } else if( c > (2/3f) && c <= (5/6f) ){
                    red = (c - (2/3f)) * 1530;
                    green = 0;
                    blue = 255;
                } else if(c > (5/6f) && c <= 1 ){
                    red = 255;
                    green = 0;
                    blue = 255 - ((c - (5/6f)) * 1530);
                }

                mTL.setBackgroundColor(Color.rgb((int)red, (int)green, (int)blue));
                mTR.setBackgroundColor(Color.rgb((int)red, (int)green, (int)blue));
                mBR.setBackgroundColor(Color.rgb((int)red, (int)green, (int)blue));
                mBL.setBackgroundColor(Color.rgb((int)red, (int)green, (int)blue));

                if (percent < 0.2 && oneToast) {
                    Toast.makeText(getApplicationContext(), "SeekBar's value is VERY SMALL", Toast.LENGTH_SHORT).show();
                    oneToast = false;
                }else if (percent > 0.8 && oneToast) {
                    Toast.makeText(getApplicationContext(), "SeekBar's value is VERY BIG", Toast.LENGTH_SHORT).show();
                    oneToast = false;
                }else if (percent >= 0.2 && percent <= 0.8) {
                    oneToast = true;
                }
            }
        });

        mSeekBar.setProgress(fS);

        mRatingDescription = findViewById(R.id.rating_edittext);
        mSubmit = findViewById(R.id.submit_button);
        mSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String description = mRatingDescription.getText().toString();
                mRatingDescription.setText("");
                String rating = rateMessage.getText().toString();
                if (!rating.equals("Horrible"))
                    Toast.makeText(getApplicationContext(), "You rated an "+rating+". "+"\""+description+"\"", Toast.LENGTH_SHORT).show();
                else Toast.makeText(getApplicationContext(), "We do not allow horrible ratings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Button clickButton = (Button)v;
        int value = Integer.parseInt(clickButton.getText().toString());
        value++;
        clickButton.setText(""+value);
        buttonToast(clickButton.getId(), value);
    }

    public void buttonToast(int id, int count) {
        String buttonName = "";
        switch (id) {
            case R.id.top_left_button:
                buttonName = "Top Left Button";
                break;
            case R.id.top_right_button:
                buttonName = "Top Right Button";
                break;
            case R.id.bottom_left_button:
                buttonName = "Bottom Left Button";
                break;
            case R.id.bottom_right_button:
                buttonName = "Bottom Right Button";
                break;
        }

        Toast.makeText(getApplicationContext(),
                "The " + buttonName + " has been pressed " + count + " times.",
                Toast.LENGTH_SHORT)
                .show();
    }

    public void saveButtonClicks() {
        editor.putString("tlValue", mTL.getText().toString());
        editor.putString("trValue", mTR.getText().toString());
        editor.putString("blValue", mBL.getText().toString());
        editor.putString("brValue", mBR.getText().toString());
        String fontSize = ""+(int)(mSeekBar.getProgress());
        editor.putString("fontSize", fontSize);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveButtonClicks();
    }
}