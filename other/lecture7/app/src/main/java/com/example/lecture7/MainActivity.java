package com.example.lecture7;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    private DrawView drawView;
    private Button triangleBtn;
    private Button circleBtn;
    private Button rectangleBtn;
    private Button clearBtn;
    private CheckBox fillFlagCB;

    private Boolean fillFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawView) findViewById(R.id.draw_view);

        triangleBtn = (Button) findViewById(R.id.tringle_btn);
        circleBtn = (Button) findViewById(R.id.circle_btn);
        rectangleBtn = (Button) findViewById(R.id.rectangle_btn);
        clearBtn = (Button) findViewById(R.id.clear_btn);
        fillFlagCB = (CheckBox) findViewById(R.id.fill_flag_cb);

        triangleBtn.setOnClickListener(triangleBtnClick);
        circleBtn.setOnClickListener(circleBtnClick);
        rectangleBtn.setOnClickListener(rectangleBtnClick);
        clearBtn.setOnClickListener(clearBtnClick);
        fillFlagCB.setOnCheckedChangeListener(fillFlagCBChange);
    }

    public void setFigure(final int figure, final boolean fillFlag){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                drawView.setFigure(figure);
                drawView.setFillFlag(fillFlag);
                drawView.invalidate();
            }
        });
    }

    CompoundButton.OnCheckedChangeListener fillFlagCBChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            fillFlag = b;
        }
    };

    View.OnClickListener triangleBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setFigure(1, fillFlag);
        }
    };

    View.OnClickListener circleBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setFigure(2, fillFlag);
        }
    };

    View.OnClickListener rectangleBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setFigure(3, fillFlag);
        }
    };

    View.OnClickListener clearBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setFigure(0, fillFlag);
        }
    };
}