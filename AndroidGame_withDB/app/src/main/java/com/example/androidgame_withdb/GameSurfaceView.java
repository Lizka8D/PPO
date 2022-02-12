package com.example.androidgame_withdb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.ConditionVariable;
import android.view.MotionEvent;

@SuppressLint("ViewConstructor")
public class GameSurfaceView extends GLSurfaceView {

    private final GameSurfaceRenderer mRenderer;
    private final ConditionVariable syncObj = new ConditionVariable();

    public GameSurfaceView(Context context, GameState gameState,
                           TextResources.Configuration textConfig) {
        super(context);

        setEGLContextClientVersion(2);

        mRenderer = new GameSurfaceRenderer(gameState, this, textConfig);
        setRenderer(mRenderer);
    }

    @Override
    public void onPause() {
        super.onPause();

        syncObj.close();
        queueEvent(() -> mRenderer.onViewPause(syncObj));
        syncObj.block();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            final float x;
            x = e.getX();
            queueEvent(() -> mRenderer.touchEvent(x));
        }

        return true;
    }
}
