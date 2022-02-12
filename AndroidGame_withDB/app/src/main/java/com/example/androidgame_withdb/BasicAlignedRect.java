package com.example.androidgame_withdb;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.FloatBuffer;

public class BasicAlignedRect extends BaseRect {

    static final String VERTEX_SHADER_CODE =
            "uniform mat4 u_mvpMatrix;" +
                    "attribute vec4 a_position;" +

                    "void main() {" +
                    "  gl_Position = u_mvpMatrix * a_position;" +
                    "}";

    static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform vec4 u_color;" +

                    "void main() {" +
                    "  gl_FragColor = u_color;" +
                    "}";

    static FloatBuffer sVertexBuffer = getVertexArray();

    static int sProgramHandle = -1;
    static int sColorHandle = -1;
    static int sPositionHandle = -1;
    static int sMVPMatrixHandle = -1;

    float[] mColor = new float[4];

    private static boolean sDrawPrepared;

    static float[] sTempMVP = new float[16];

    public static void createProgram() {
        sProgramHandle = com.example.androidgame_withdb.Util.createProgram(VERTEX_SHADER_CODE,
                FRAGMENT_SHADER_CODE);

        sPositionHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_position");
        com.example.androidgame_withdb.Util.checkGlError("glGetAttribLocation");

        sColorHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_color");
        com.example.androidgame_withdb.Util.checkGlError("glGetUniformLocation");

        sMVPMatrixHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_mvpMatrix");
        com.example.androidgame_withdb.Util.checkGlError("glGetUniformLocation");
    }

    public void setColor(float r, float g, float b) {
        com.example.androidgame_withdb.Util.checkGlError("setColor start");
        mColor[0] = r;
        mColor[1] = g;
        mColor[2] = b;
        mColor[3] = 1.0f;
    }

    public float[] getColor() {
        return mColor;
    }

    public static void prepareToDraw() {
        GLES20.glUseProgram(sProgramHandle);
        com.example.androidgame_withdb.Util.checkGlError("glUseProgram");

        GLES20.glEnableVertexAttribArray(sPositionHandle);
        com.example.androidgame_withdb.Util.checkGlError("glEnableVertexAttribArray");

        GLES20.glVertexAttribPointer(sPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, VERTEX_STRIDE, sVertexBuffer);
        com.example.androidgame_withdb.Util.checkGlError("glVertexAttribPointer");

        sDrawPrepared = true;
    }

    public static void finishedDrawing() {
        sDrawPrepared = false;

        GLES20.glDisableVertexAttribArray(sPositionHandle);
        GLES20.glUseProgram(0);
    }

    public void draw() {
        if (GameSurfaceRenderer.EXTRA_CHECK) com.example.androidgame_withdb.Util.checkGlError("draw start");
        if (!sDrawPrepared) {
            throw new RuntimeException("not prepared");
        }

        float[] mvp = sTempMVP;
        Matrix.multiplyMM(mvp, 0, GameSurfaceRenderer.mProjectionMatrix, 0, mModelView, 0);

        GLES20.glUniformMatrix4fv(sMVPMatrixHandle, 1, false, mvp, 0);
        if (GameSurfaceRenderer.EXTRA_CHECK) com.example.androidgame_withdb.Util.checkGlError("glUniformMatrix4fv");

        GLES20.glUniform4fv(sColorHandle, 1, mColor, 0);
        if (GameSurfaceRenderer.EXTRA_CHECK) com.example.androidgame_withdb.Util.checkGlError("glUniform4fv ");

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, VERTEX_COUNT);
        if (GameSurfaceRenderer.EXTRA_CHECK) com.example.androidgame_withdb.Util.checkGlError("glDrawArrays");
    }
}
