package com.example.androidgame_withdb;

public class Brick extends com.example.androidgame_withdb.BasicAlignedRect {

    private boolean mAlive = false;
    private int mPoints = 0;

    public boolean isAlive() {
        return mAlive;
    }

    public void setAlive(boolean alive) {
        mAlive = alive;
    }

    public int getScoreValue() {
        return mPoints;
    }

    public void setScoreValue(int points) {
        mPoints = points;
    }
}
