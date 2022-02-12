package com.example.androidgame_withdb;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

public class OutlineAlignedRect extends BasicAlignedRect {
    private static final FloatBuffer sOutlineVertexBuffer = getOutlineVertexArray();

    private static boolean sDrawPrepared;

    public static void prepareToDraw() {

        GLES20.glUseProgram(sProgramHandle);
        Util.checkGlError("glUseProgram");

        GLES20.glEnableVertexAttribArray(sPositionHandle);
        Util.checkGlError("glEnableVertexAttribArray");

        GLES20.glVertexAttribPointer(sPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, VERTEX_STRIDE, sOutlineVertexBuffer);
        Util.checkGlError("glVertexAttribPointer");

        sDrawPrepared = true;
    }

    public static void finishedDrawing() {
        sDrawPrepared = false;

        GLES20.glDisableVertexAttribArray(sPositionHandle);
        GLES20.glUseProgram(0);
    }

    @Override
    public void draw() {
        if (GameSurfaceRenderer.EXTRA_CHECK) Util.checkGlError("draw start");
        if (!sDrawPrepared) {
            throw new RuntimeException("not prepared");
        }

        float[] mvp = sTempMVP;
        Matrix.multiplyMM(mvp, 0, GameSurfaceRenderer.mProjectionMatrix, 0, mModelView, 0);

        GLES20.glUniformMatrix4fv(sMVPMatrixHandle, 1, false, mvp, 0);
        Util.checkGlError("glUniformMatrix4fv");

        GLES20.glUniform4fv(sColorHandle, 1, mColor, 0);
        Util.checkGlError("glUniform4fv ");

        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, VERTEX_COUNT);
        if (GameSurfaceRenderer.EXTRA_CHECK) Util.checkGlError("glDrawArrays");
    }
}
