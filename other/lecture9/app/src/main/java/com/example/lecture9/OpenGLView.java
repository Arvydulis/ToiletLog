package com.example.lecture9;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class OpenGLView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private OpenGLRender Renderer;
    private float PreviousX;
    private float PreviousY;
    private boolean animatioFlag = false;

    public OpenGLView(Context context) {
        super(context);
        init();
    }

    public OpenGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setEGLContextClientVersion(2);

        Renderer = new OpenGLRender();
        setRenderer(Renderer);

        Renderer.setAnimationFlag(animatioFlag);

        if (getAnimationFlag() == false){
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }else {
            setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        }
    }

    public void setAnimationFlag(boolean animatioFlag){
        this.animatioFlag = animatioFlag;
        Renderer.setAnimationFlag(animatioFlag);
        if (getAnimationFlag() == false){
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }else {
            setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        }
    }

    public  boolean getAnimationFlag(){
        return animatioFlag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (getAnimationFlag() == false){
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()){
                case MotionEvent.ACTION_MOVE:
                    float dx = x - PreviousX;
                    float dy = y - PreviousY;
                    if (y > getHeight() / 2) { dx = dx * -1; }
                    if (x < getWidth() / 2) { dy = dy * -1; }
                    Renderer.setAngle(Renderer.getAngle() + ((dx+dy) * TOUCH_SCALE_FACTOR));
                    requestRender();
            }
            PreviousX = x;
            PreviousY = y;
        }

        return true;
    }
}
