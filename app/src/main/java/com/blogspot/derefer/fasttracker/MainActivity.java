package com.blogspot.derefer.fasttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SaveFastDialogFragment.SaveFastDialogListener {
    private Button fastingToggleButton;
    private TextView fastingStatusTextView;
    private TextView fastingSinceTextView;

    private boolean isFasting;
    private long fastingSince;
    private SharedPreferences sharedPreferences;
    private ArrayList<Fast> previousFasts;
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

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.previousFastsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FastArrayAdapter adapter = new FastArrayAdapter(this, previousFasts);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        initUi();
    }

    private void loadStoredData() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        isFasting = sharedPreferences.getBoolean(getString(R.string.is_fasting), false);
        fastingSince = sharedPreferences.getLong(getString(R.string.fasting_since), 0);
        previousFasts = getPreviousFasts();
    }

    private ArrayList<Fast> getPreviousFasts() {
        // TODO: Load the list from a file
        //   - Open file with a constant name
        //   - Read line-by-line (id:long, begin:long, end:long)
        //   - Extend the list
        //   - The newest fast shall appear first
        // https://stackoverflow.com/questions/8077530/android-get-current-timestamp
        // https://stackoverflow.com/questions/56007124/how-do-i-convert-system-currenttimemillis-to-time-format-hhmmss
        String previousFastsFromFile = readFileToString("previous_fasts.txt");
        Log.i(LOG_TAG, previousFastsFromFile);

        ArrayList<Fast> list = new ArrayList<>();
        list.add(new Fast(1,101, 201));
        list.add(new Fast(2,102, 202));
        list.add(new Fast(3,103, 203));
        list.add(new Fast(4,104, 204));
        list.add(new Fast(5,105, 205));
        list.add(new Fast(6,106, 206));
        list.add(new Fast(7,107, 207));
        list.add(new Fast(8,108, 208));
        return list;
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

    private void writeStringToFile(final String fileContents, String fileName) {
        Context context = getApplicationContext();
        File path = context.getFilesDir();
        File file = new File(path, "previous_fasts.txt"); // TODO: Use parameter
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write("Hello from file!".getBytes());
            outStream.close();
        } catch (IOException e) {
            // TODO: Review logging of exceptions properly
            // https://stackoverflow.com/questions/4341363/android-print-full-exception-backtrace-to-log
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
    }

    private String readFileToString(String fileName) {
        Context context = getApplicationContext();
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader inStream;
        File path = context.getFilesDir();
        File file = new File(path, "previous_fasts.txt"); // TODO: Use parameter
        try {
            inStream = new BufferedReader(new FileReader(file));
            String line;
            while ((line = inStream.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (FileNotFoundException e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
        } catch (IOException e) {
            Log.e(e.getClass().getName(), e.getMessage(), e);
        }
        return stringBuilder.toString();
    }

    @Override
    public void onSaveFastDialogPositiveClick(DialogFragment dialog) {
        // TODO: Insert the two timestamps into the file
        //   - Reset the layout?
        //   - The last id must be stored on load
        writeStringToFile("Hello from file!", "previous_fasts.txt");
    }

    @Override
    public void onSaveFastDialogNegativeClick(DialogFragment dialog) {
        // TODO: Reset the layout?
    }
}
