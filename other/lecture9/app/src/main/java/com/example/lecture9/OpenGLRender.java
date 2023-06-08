package com.example.lecture9;

import android.content.SharedPreferences;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRender implements GLSurfaceView.Renderer {

    private final float[] MVPMatrix = new float[16];
    private final float[] ProjectionMatrix = new float[16];
    private final float[] ViewMatrix = new float[16];
    private final float[] MMatrix = new float[16];
    public volatile float Angle;

    private boolean animationFlag = false;

    private Triangle Triangle;


    public OpenGLRender() { }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Triangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(ProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
        Matrix.setLookAtM(ViewMatrix, 0,0,0,-3,0f,0f,0f,0f,1.0f,0.0f);

        if (getAnimationFlag() == true){
            long time = SystemClock.uptimeMillis() %4000L;
            float angle = 0.090f * ((int) time);
            Angle = angle;
        }

        Matrix.setRotateM(MMatrix,0,Angle,0,0,1.0f);
        Matrix.multiplyMM(MVPMatrix,0,ViewMatrix,0,MMatrix,0);
        Matrix.multiplyMM(MVPMatrix, 0, ProjectionMatrix, 0, MVPMatrix, 0);

        Triangle.draw(MVPMatrix);
    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        return shader;
    }

    public float getAngle() {
        return Angle;
    }

    public void setAngle(float angle) {
        Angle = angle;
    }

    public  void  setAnimationFlag(boolean animationFlag){
        this.animationFlag = animationFlag;
    }

    public boolean getAnimationFlag(){
        return  animationFlag;
    }

}
