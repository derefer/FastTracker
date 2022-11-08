package com.blogspot.derefer.fasttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SaveFastDialogFragment.SaveFastDialogListener {
    private Button fastingToggleButton;
    private TextView fastingStatusTextView;
    private TextView fastingSinceTextView;

    private boolean isFasting;
    private long fastingSince;
    private SharedPreferences sharedPreferences;
    private static final String LOG_TAG = MainActivity.class.getName();
    final private Handler handler = new Handler();
    final private Runnable fastingSinceTimer = new Runnable() {
        @Override
        public void run() {
            long fastingDuration = (System.currentTimeMillis() - fastingSince) / 1000;
            long hours = 60 * 60;
            long fastingHours = fastingDuration / hours;
            long fastingMinutesAndSeconds = fastingDuration % hours;
            long fastingMinutes = fastingMinutesAndSeconds / 60;
            long fastingSeconds = fastingMinutesAndSeconds % 60;
            String fastingHoursString = (fastingHours < 10 ? "0" : "") + fastingHours;
            String fastingMinutesString = (fastingMinutes < 10 ? "0" : "") + fastingMinutes;
            String fastingSecondsString = (fastingSeconds < 10 ? "0" : "") + fastingSeconds;
            fastingSinceTextView.setText(String.format("%s:%s:%s", fastingHoursString, fastingMinutesString, fastingSecondsString));
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadStoredData();
        initUi();
    }

    private void loadStoredData() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        isFasting = sharedPreferences.getBoolean(getString(R.string.is_fasting), false);
        fastingSince = sharedPreferences.getLong(getString(R.string.fasting_since), 0);
    }

    private void initUi() {
        fastingStatusTextView = findViewById(R.id.fastingStatusTextView);
        fastingSinceTextView = findViewById(R.id.fastingSinceTextView);
        fastingToggleButton = findViewById(R.id.fastingToggleButton);
        if (isFasting) {
            fastingStatusTextView.setText(R.string.you_re_fasting);
            fastingToggleButton.setText(R.string.stop_fasting);
            startFastingSinceTimer();
        } else {
            fastingStatusTextView.setText(R.string.you_re_not_fasting);
            fastingToggleButton.setText(R.string.start_fasting);
        }
        fastingToggleButton.setOnClickListener(v -> {
            Log.i(LOG_TAG, "onClick()");
            if (isFasting) {
                isFasting = false;
                fastingSince = 0;
                stopFastingSinceTimer();
                fastingStatusTextView.setText(R.string.you_re_not_fasting);
                fastingToggleButton.setText(R.string.start_fasting);
                DialogFragment saveFastDialogFragment = new SaveFastDialogFragment();
                saveFastDialogFragment.show(getSupportFragmentManager(), "SaveFastDialogFragment");
            } else {
                isFasting = true;
                fastingSince = System.currentTimeMillis();
                startFastingSinceTimer();
                fastingStatusTextView.setText(R.string.you_re_fasting);
                fastingToggleButton.setText(R.string.stop_fasting);
            }
        });
    }

    private void stopFastingSinceTimer() {
        handler.removeCallbacks(fastingSinceTimer);
    }

    private void startFastingSinceTimer() {
        handler.post(fastingSinceTimer);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopFastingSinceTimer();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.is_fasting), isFasting);
        editor.putLong(getString(R.string.fasting_since), fastingSince);
        editor.apply();
    }

    @Override
    public void onSaveFastDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onSaveFastDialogNegativeClick(DialogFragment dialog) {
        // Nothing to do.
    }
}
