package com.example.fasttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button fastingToggleButton;
    private TextView fastingStatusTextView;

    private boolean isFasting;
    private SharedPreferences sharedPreferences;
    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadIsFasting();
        initUi();
    }

    private void loadIsFasting() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        isFasting = sharedPreferences.getBoolean(getString(R.string.is_fasting), false);
    }

    private void initUi() {
        fastingStatusTextView = findViewById(R.id.fastingStatusTextView);
        fastingToggleButton = findViewById(R.id.fastingToggleButton);
        if (isFasting) {
            fastingStatusTextView.setText(R.string.you_re_fasting);
            fastingToggleButton.setText(R.string.stop_fasting);
        } else {
            fastingStatusTextView.setText(R.string.you_re_not_fasting);
            fastingToggleButton.setText(R.string.start_fasting);
        }
        fastingToggleButton.setOnClickListener(v -> {
            Log.i(LOG_TAG, "onClick()");
            if (isFasting) {
                isFasting = false;
                fastingStatusTextView.setText(R.string.you_re_not_fasting);
                fastingToggleButton.setText(R.string.start_fasting);
            } else {
                isFasting = true;
                fastingStatusTextView.setText(R.string.you_re_fasting);
                fastingToggleButton.setText(R.string.stop_fasting);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.is_fasting), isFasting);
        editor.apply();
    }
}
