package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private LinearLayout circleContainer;

    private Random random;

    private int count;
    private ArrayList<Integer> circleSizes;
    private ArrayList<Integer> borderColors;
    private ArrayList<Integer> fillColors;
    private ArrayList<Integer> xPositions;
    private ArrayList<Integer> yPositions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        circleContainer = findViewById(R.id.circleContainer);

        random = new Random();
        count = 0;
        circleSizes = new ArrayList<Integer>();
        borderColors = new ArrayList<Integer>();
        fillColors = new ArrayList<Integer>();
        xPositions = new ArrayList<Integer>();
        yPositions = new ArrayList<Integer>();


        // Restore saved parameters if available
        restoreSavedParameters();

        if (count != 0){
            CircleView circleView = new CircleView(MainActivity.this);
            // Add circle to the container
            circleContainer.addView(circleView);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editText.getText().toString();
                int letterCount = word.length();
                count = letterCount;

                // Clear previous circles
                circleContainer.removeAllViews();
                circleSizes = new ArrayList<Integer>();
                borderColors = new ArrayList<Integer>();
                fillColors = new ArrayList<Integer>();
                xPositions = new ArrayList<Integer>();
                yPositions = new ArrayList<Integer>();

                // Generate new circles
                CircleView circleView = new CircleView(MainActivity.this, count, circleContainer.getWidth(), circleContainer.getHeight());
                // Add circle to the container
                circleContainer.addView(circleView);

                // Save the parameters for next run/activity refresh
                saveParameters();
            }
        });
    }

    private void restoreSavedParameters() {
        // Read the saved parameters from the file (format of your choice)
        // Example: read from a text file
        File file = new File(getFilesDir(), "circle_parameters.txt");
        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                count = Integer.parseInt(reader.readLine());
                for (int i = 0; i < count; i++) {
                    int circleSize = Integer.parseInt(reader.readLine());
                    circleSizes.add(circleSize);
                    int borderColor = Integer.parseInt(reader.readLine());
                    borderColors.add(borderColor);
                    int fillColor = Integer.parseInt(reader.readLine());
                    fillColors.add(fillColor);
                    int xPosition = Integer.parseInt(reader.readLine());
                    xPositions.add(xPosition);
                    int yPosition = Integer.parseInt(reader.readLine());
                    yPositions.add(yPosition);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveParameters() {
        // Save the parameters to a file (format of your choice)
        // Example: write to a text file
        File file = new File(getFilesDir(), "circle_parameters.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(String.valueOf(count));
            writer.newLine();
            for (int i = 0; i < count; i++) {
                writer.write(String.valueOf(circleSizes.get(i)));
                writer.newLine();
                writer.write(String.valueOf(borderColors.get(i)));
                writer.newLine();
                writer.write(String.valueOf(fillColors.get(i)));
                writer.newLine();
                writer.write(String.valueOf(xPositions.get(i)));
                writer.newLine();
                writer.write(String.valueOf(yPositions.get(i)));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class CircleView extends View {

        public CircleView(Context context, int count, int width, int height) {
            super(context);
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                // Random size between 50 and 250
                int size = random.nextInt(200) + 50;
                circleSizes.add(size);
                // Random RGB color
                borderColors.add(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                // Random RGB color
                fillColors.add(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));

                int radius = size / 2;
                int x = random.nextInt(width - radius * 2) + radius;
                xPositions.add(x);
                int y = random.nextInt(height - radius * 2) + radius;
                yPositions.add(y);
            }
        }

        public CircleView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for (int i = 0; i < count; i++)
            {
                int radius = circleSizes.get(i) / 2;
                int x = xPositions.get(i);
                int y = yPositions.get(i);

                Paint borderPaint = new Paint();
                borderPaint.setStyle(Paint.Style.STROKE);
                borderPaint.setColor(borderColors.get(i));
                borderPaint.setStrokeWidth(5);

                Paint fillPaint = new Paint();
                fillPaint.setStyle(Paint.Style.FILL);
                fillPaint.setColor(fillColors.get(i));

                canvas.drawCircle(x, y, radius, fillPaint);
                canvas.drawCircle(x, y, radius, borderPaint);
            }
        }
    }

}

