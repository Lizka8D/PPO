package com.example.androidgame_withdb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Breakout extends Activity implements OnItemSelectedListener {

    public static final String PREFS_NAME = "PrefsAndScores";
    private static final String DIFFICULTY_KEY = "difficulty";

    private String nickname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_difficultyLevel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_level_names, android.R.layout.preference_category);
        adapter.setDropDownViewResource(android.R.layout.preference_category);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        restorePreferences();
        updateControls();
    }

    private void updateControls() {
        Spinner difficulty = (Spinner) findViewById(R.id.spinner_difficultyLevel);
        difficulty.setSelection(GameActivity.getDifficultyIndex());

        Button resume = (Button) findViewById(R.id.button_resumeGame);
        resume.setEnabled(GameActivity.canResumeFromSave());

        TextView player = (TextView) findViewById(R.id.player);
        UseDatabase db = new UseDatabase(getApplicationContext());
        nickname = db.GetActivePlayer();
        player.setText(String.valueOf(nickname));
    }

    public void clickNewGame(View view) {
        GameActivity.invalidateSavedGame();
        startGame();
    }

    public void clickResumeGame(View view) {
        startGame();
    }

    private void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void clickToList(View view) {
        Intent intent = new Intent(this, Players.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Spinner spinner = (Spinner) parent;
        int difficulty = spinner.getSelectedItemPosition();

        GameActivity.setDifficultyIndex(difficulty);
        updateControls();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @SuppressLint("ApplySharedPref")
    private void savePreferences() {

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(DIFFICULTY_KEY, GameActivity.getDifficultyIndex());
        editor.commit();
    }

    private void restorePreferences() {
        TextView player = findViewById(R.id.player);
        UseDatabase db = new UseDatabase(getApplicationContext());
        nickname = db.GetActivePlayer();
        player.setText(nickname);
    }
}
