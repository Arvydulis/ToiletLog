package com.example.lecture8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public ImageView rocket;
    public View root;

    public AnimatorSet animSetXY;
    public ObjectAnimator translateX;
    public ObjectAnimator translateY;
    public ObjectAnimator rotation;
    public ObjectAnimator alphaAnimation;
    public ObjectAnimator scaleX;
    public ObjectAnimator scaleY;

    public float deltaX = 0f;
    public float deltaY = 0f;
    public double rockeDirection = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        rocket = (ImageView) findViewById(R.id.rocket);
        root = (View) findViewById(R.id.rootView);
        root.setOnTouchListener(onTouchView);
        root.setBackgroundColor(Color.BLACK);
        rotation = new ObjectAnimator();
        animSetXY = new AnimatorSet();
    }

    Animator.AnimatorListener animSetXYListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(@NonNull Animator animator) {
            rocket.setImageDrawable(getDrawable(R.drawable.rocket_1));
        }

        @Override
        public void onAnimationEnd(@NonNull Animator animator) {
            rocket.setImageDrawable(getDrawable(R.drawable.rocket_0));
        }

        @Override
        public void onAnimationCancel(@NonNull Animator animator) {

        }

        @Override
        public void onAnimationRepeat(@NonNull Animator animator) {

        }
    };

    Animator.AnimatorListener rotationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(@NonNull Animator animator) {

        }

        @Override
        public void onAnimationEnd(@NonNull Animator animator) {
            animSetXY.start();
        }

        @Override
        public void onAnimationCancel(@NonNull Animator animator) {

        }

        @Override
        public void onAnimationRepeat(@NonNull Animator animator) {

        }
    };

    View.OnTouchListener onTouchView = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if ((motionEvent.getAction() == MotionEvent.ACTION_DOWN)) {
                if ((!rotation.isRunning()) && (!animSetXY.isRunning())) {

                    deltaX = motionEvent.getX() - (rocket.getWidth() / 2);
                    deltaY = motionEvent.getY() - (rocket.getHeight() / 2);

                    rockeDirection = Math.atan2(deltaY - rocket.getY(),
                            deltaX - rocket.getX()) * 180 / Math.PI;

                    rotation = ObjectAnimator.ofFloat(rocket, "rotation", (float) (rockeDirection));
                    rotation.addListener(rotationListener);
                    rotation.setDuration(800);
                    rotation.setInterpolator(new AccelerateDecelerateInterpolator());
                    rotation.start();

                    animSetXY = new AnimatorSet();
                    translateX = ObjectAnimator.ofFloat(rocket, "translationX", deltaX);
                    translateY = ObjectAnimator.ofFloat(rocket, "translationY", deltaY);
                    alphaAnimation = ObjectAnimator.ofFloat(rocket, "alpha", 1f, 0.88f, 1f);
                    scaleX = ObjectAnimator.ofFloat(rocket, "scaleX", 1f, 1.06f, 1f);
                    scaleY = ObjectAnimator.ofFloat(rocket, "scaleY", 1f, 1.01f, 1f);
                    animSetXY.playTogether(translateX, translateY, alphaAnimation, scaleX, scaleY);
                    animSetXY.setInterpolator(new AccelerateDecelerateInterpolator());
                    animSetXY.addListener(animSetXYListener);
                    animSetXY.setDuration(1000);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "We're flying!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            return true;
        }
    };

}