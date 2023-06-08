package com.example.lecture9;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    private  OpenGLView openGLView;
    private CheckBox animationCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        animationCheckBox = (CheckBox) findViewById(R.id.animation_CB);
        animationCheckBox.setOnCheckedChangeListener(onAnimationCheckBoxChange);
        openGLView = (OpenGLView)findViewById(R.id.opengl_view);

    }

    CompoundButton.OnCheckedChangeListener onAnimationCheckBoxChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            openGLView.setAnimationFlag(b);
        }
    };

}