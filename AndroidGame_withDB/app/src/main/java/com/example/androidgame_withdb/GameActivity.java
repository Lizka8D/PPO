package com.example.androidgame_withdb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;


public class GameActivity extends Activity {

    private static final int DIFFICULTY_MIN = 0;
    private static final int DIFFICULTY_MAX = 3;
    private static final int DIFFICULTY_DEFAULT = 1;
    private static int sDifficultyIndex;

    private GameSurfaceView mGLView;
    private GameState mGameState;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextResources.Configuration textConfig = TextResources.configure(this);

        mGameState = new GameState();
        configureGameState();

        mGLView = new GameSurfaceView(this, mGameState, textConfig);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
        updateHighScore(GameState.getFinalScore());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }


    private void configureGameState() {
        int maxLives, minSpeed, maxSpeed;
        float ballSize, paddleSize, scoreMultiplier;

        switch (sDifficultyIndex) {
            case 0:                     // easy
                ballSize = 2.0f;
                paddleSize = 2.0f;
                scoreMultiplier = 0.75f;
                maxLives = 4;
                minSpeed = 200;
                maxSpeed = 500;
                break;
            case 1:                     // normal
                ballSize = 1;
                paddleSize = 1.0f;
                scoreMultiplier = 1.0f;
                maxLives = 3;
                minSpeed = 300;
                maxSpeed = 800;
                break;
            case 2:                     // hard
                ballSize = 1.0f;
                paddleSize = 0.8f;
                scoreMultiplier = 1.25f;
                maxLives = 3;
                minSpeed = 600;
                maxSpeed = 1200;
                break;
            case 3:                     // absurd
                ballSize = 1.0f;
                paddleSize = 0.5f;
                scoreMultiplier = 0.1f;
                maxLives = 1;
                minSpeed = 1000;
                maxSpeed = 100000;
                break;
            default:
                throw new RuntimeException("bad difficulty index " + sDifficultyIndex);
        }

        mGameState.setBallSizeMultiplier(ballSize);
        mGameState.setPaddleSizeMultiplier(paddleSize);
        mGameState.setScoreMultiplier(scoreMultiplier);
        mGameState.setMaxLives(maxLives);
        mGameState.setBallInitialSpeed(minSpeed);
        mGameState.setBallMaximumSpeed(maxSpeed);
    }

    public static int getDifficultyIndex() {
        return sDifficultyIndex;
    }

    public static void setDifficultyIndex(int difficultyIndex) {

        if (difficultyIndex < DIFFICULTY_MIN || difficultyIndex > DIFFICULTY_MAX) {
            difficultyIndex = DIFFICULTY_DEFAULT;
        }

        if (sDifficultyIndex != difficultyIndex) {
            sDifficultyIndex = difficultyIndex;
            invalidateSavedGame();
        }
    }

    public static void invalidateSavedGame() {
        GameState.invalidateSavedGame();
    }

    public static boolean canResumeFromSave() {
        return GameState.canResumeFromSave();
    }

    @SuppressLint("ApplySharedPref")
    private void updateHighScore(int lastScore) {
        UseDatabase db = new UseDatabase(getApplicationContext());

        int highScore = Integer.parseInt(db.GetScore());

        if (lastScore > highScore) {
            db.UpDataScore(Integer.toString(lastScore));
        }
    }
}
