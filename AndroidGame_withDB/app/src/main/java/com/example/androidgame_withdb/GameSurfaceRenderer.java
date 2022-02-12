package com.example.androidgame_withdb;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.ConditionVariable;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Main game display class.
 * <p>
 * The methods here expect to run on the Renderer thread.  Calling them from other threads
 * must be done through GLSurfaceView.queueEvent().
 */
public class GameSurfaceRenderer implements GLSurfaceView.Renderer {

    public static final boolean EXTRA_CHECK = true;

    static final float[] mProjectionMatrix = new float[16];

    private int mViewportWidth;
    private int mViewportXoff;

    private final GameSurfaceView mSurfaceView;
    private final com.example.androidgame_withdb.GameState mGameState;
    private final TextResources.Configuration mTextConfig;

    public GameSurfaceRenderer(com.example.androidgame_withdb.GameState gameState, GameSurfaceView surfaceView,
                               TextResources.Configuration textConfig) {
        mSurfaceView = surfaceView;
        mGameState = gameState;
        mTextConfig = textConfig;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        if (EXTRA_CHECK) Util.checkGlError("onSurfaceCreated start");

        BasicAlignedRect.createProgram();
        TexturedAlignedRect.createProgram();

        com.example.androidgame_withdb.GameState gameState = mGameState;
        gameState.setTextResources(new TextResources(mTextConfig));
        gameState.allocBorders();
        gameState.allocBricks();
        gameState.allocPaddle();
        gameState.allocBall();
        gameState.allocScore();
        gameState.allocMessages();
        gameState.allocDebugStuff();

        gameState.restore();

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        if (EXTRA_CHECK) {
            GLES20.glEnable(GLES20.GL_CULL_FACE);
        } else {
            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }

        if (EXTRA_CHECK) Util.checkGlError("onSurfaceCreated end");
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {

        if (EXTRA_CHECK) Util.checkGlError("onSurfaceChanged start");

        float arenaRatio = com.example.androidgame_withdb.GameState.ARENA_HEIGHT / com.example.androidgame_withdb.GameState.ARENA_WIDTH;
        int x, y, viewWidth, viewHeight;

        if (height > (int) (width * arenaRatio)) {
            viewWidth = width;
            viewHeight = (int) (width * arenaRatio);
        } else {
            viewHeight = height;
            viewWidth = (int) (height / arenaRatio);
        }
        x = (width - viewWidth) / 2;
        y = (height - viewHeight) / 2;

        GLES20.glViewport(x, y, viewWidth, viewHeight);

        mViewportWidth = viewWidth;
        mViewportXoff = x;

        Matrix.orthoM(mProjectionMatrix, 0,  0, com.example.androidgame_withdb.GameState.ARENA_WIDTH,
                0, com.example.androidgame_withdb.GameState.ARENA_HEIGHT,  -1, 1);

        mGameState.surfaceChanged();

        if (EXTRA_CHECK) Util.checkGlError("onSurfaceChanged end");
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        com.example.androidgame_withdb.GameState gameState = mGameState;

        gameState.calculateNextFrame();

        if (EXTRA_CHECK) Util.checkGlError("onDrawFrame start");

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        BasicAlignedRect.prepareToDraw();
        gameState.drawBorders();
        gameState.drawBricks();
        gameState.drawPaddle();
        BasicAlignedRect.finishedDrawing();

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        TexturedAlignedRect.prepareToDraw();
        gameState.drawScore();
        gameState.drawBall();
        gameState.drawMessages();
        TexturedAlignedRect.finishedDrawing();

        gameState.drawDebugStuff();

        GLES20.glDisable(GLES20.GL_BLEND);

        if (EXTRA_CHECK) Util.checkGlError("onDrawFrame end");

        if (!gameState.isAnimating()) {
            mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
    }

    public void onViewPause(ConditionVariable syncObj) {

        mGameState.save();
        syncObj.open();
    }

    public void touchEvent(float x) {

        float arenaX = (x - mViewportXoff) * (com.example.androidgame_withdb.GameState.ARENA_WIDTH / mViewportWidth);

        mGameState.movePaddle(arenaX);
    }
}
