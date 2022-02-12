package com.example.androidgame_withdb;

import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class TexturedAlignedRect extends BaseRect {

    static final String VERTEX_SHADER_CODE =
            "uniform mat4 u_mvpMatrix;" +       // model/view/projection matrix
                    "attribute vec4 a_position;" +      // vertex data for us to transform
                    "attribute vec2 a_texCoord;" +      // texture coordinate for vertex...
                    "varying vec2 v_texCoord;" +        // ...which we forward to the fragment shader

                    "void main() {" +
                    "  gl_Position = u_mvpMatrix * a_position;" +
                    "  v_texCoord = a_texCoord;" +
                    "}";

    static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform sampler2D u_texture;" +
                    "varying vec2 v_texCoord;" +

                    "void main() {" +
                    "  gl_FragColor = texture2D(u_texture, v_texCoord);" +
                    "}";

    private static final FloatBuffer sVertexBuffer = getVertexArray();

    private static int sProgramHandle = -1;
    private static int sTexCoordHandle = -1;
    private static int sPositionHandle = -1;
    private static int sMVPMatrixHandle = -1;

    private int mTextureDataHandle = -1;
    private int mTextureWidth = -1;
    private int mTextureHeight = -1;
    private final FloatBuffer mTexBuffer;

    private static boolean sDrawPrepared;

    private static final float[] sTempMVP = new float[16];


    public TexturedAlignedRect() {
        FloatBuffer defaultCoords = getTexArray();

        ByteBuffer bb = ByteBuffer.allocateDirect(VERTEX_COUNT * TEX_VERTEX_STRIDE);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(defaultCoords);
        defaultCoords.position(0);      // ugh
        fb.position(0);
        mTexBuffer = fb;
    }

    public static void createProgram() {
        sProgramHandle = Util.createProgram(VERTEX_SHADER_CODE,
                FRAGMENT_SHADER_CODE);

        sPositionHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_position");
        Util.checkGlError("glGetAttribLocation");

        sTexCoordHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_texCoord");
        Util.checkGlError("glGetAttribLocation");

        sMVPMatrixHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_mvpMatrix");
        Util.checkGlError("glGetUniformLocation");

        int textureUniformHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_texture");
        Util.checkGlError("glGetUniformLocation");

        GLES20.glUseProgram(sProgramHandle);
        GLES20.glUniform1i(textureUniformHandle, 0);
        Util.checkGlError("glUniform1i");
        GLES20.glUseProgram(0);

        Util.checkGlError("TexturedAlignedRect setup complete");
    }

    public void setTexture(ByteBuffer buf, int width, int height, int format) {
        mTextureDataHandle =
                Util.createImageTexture(buf, width, height, format);
        mTextureWidth = width;
        mTextureHeight = height;
    }

    public void setTexture(int handle, int width, int height) {
        mTextureDataHandle = handle;
        mTextureWidth = width;
        mTextureHeight = height;
    }

    public void setTextureCoords(Rect coords) {

        float left = (float) coords.left / mTextureWidth;
        float right = (float) coords.right / mTextureWidth;
        float top = (float) coords.top / mTextureHeight;
        float bottom = (float) coords.bottom / mTextureHeight;

        FloatBuffer fb = mTexBuffer;
        fb.put(left);
        fb.put(bottom);
        fb.put(right);
        fb.put(bottom);
        fb.put(left);
        fb.put(top);
        fb.put(right);
        fb.put(top);
        fb.position(0);
    }

    public static void prepareToDraw() {

        GLES20.glUseProgram(sProgramHandle);
        Util.checkGlError("glUseProgram");

        GLES20.glEnableVertexAttribArray(sPositionHandle);
        Util.checkGlError("glEnableVertexAttribArray");

        GLES20.glVertexAttribPointer(sPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, VERTEX_STRIDE, sVertexBuffer);
        Util.checkGlError("glEnableVertexAttribPointer");

        GLES20.glEnableVertexAttribArray(sTexCoordHandle);
        Util.checkGlError("glEnableVertexAttribArray");

        sDrawPrepared = true;
    }

    public static void finishedDrawing() {
        sDrawPrepared = false;

        GLES20.glDisableVertexAttribArray(sPositionHandle);
        GLES20.glUseProgram(0);
    }

    public void draw() {
        if (GameSurfaceRenderer.EXTRA_CHECK) Util.checkGlError("draw start");
        if (!sDrawPrepared) {
            throw new RuntimeException("not prepared");
        }

        GLES20.glVertexAttribPointer(sTexCoordHandle, TEX_COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, TEX_VERTEX_STRIDE, mTexBuffer);
        if (GameSurfaceRenderer.EXTRA_CHECK) Util.checkGlError("glVertexAttribPointer");

        float[] mvp = sTempMVP;
        Matrix.multiplyMM(mvp, 0, GameSurfaceRenderer.mProjectionMatrix, 0, mModelView, 0);

        GLES20.glUniformMatrix4fv(sMVPMatrixHandle, 1, false, mvp, 0);
        if (GameSurfaceRenderer.EXTRA_CHECK) Util.checkGlError("glUniformMatrix4fv");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        if (GameSurfaceRenderer.EXTRA_CHECK) Util.checkGlError("glActiveTexture");

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
        if (GameSurfaceRenderer.EXTRA_CHECK) Util.checkGlError("glBindTexture");

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, VERTEX_COUNT);
        if (GameSurfaceRenderer.EXTRA_CHECK) Util.checkGlError("glDrawArrays");
    }
}
