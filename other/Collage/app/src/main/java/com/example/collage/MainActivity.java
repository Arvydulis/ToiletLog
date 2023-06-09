package com.example.collage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.Manifest;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private ImageView image1, image2, image3, image4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);

        Button captureButton = findViewById(R.id.captureButton);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the camera to capture images
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }catch (ActivityNotFoundException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Assign captured image to the ImageViews
            if (image1.getDrawable() == null) {
                image1.setImageBitmap(imageBitmap);
            } else if (image2.getDrawable() == null) {
                image2.setImageBitmap(imageBitmap);
            } else if (image3.getDrawable() == null) {
                image3.setImageBitmap(imageBitmap);
            } else if (image4.getDrawable() == null) {
                image4.setImageBitmap(imageBitmap);
                // Display the collage when all images are captured
                displayCollage();
            }
        }
    }

    private void displayCollage() {
        // Combine the four images into a single 2x2 collage
        Bitmap collage = Bitmap.createBitmap(image1.getWidth() * 2, image1.getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(collage);
        canvas.drawBitmap(getBitmapFromImageView(image1), 0, 0, null);
        canvas.drawBitmap(getBitmapFromImageView(image2), image1.getWidth(), 0, null);
        canvas.drawBitmap(getBitmapFromImageView(image3), 0, image1.getHeight(), null);
        canvas.drawBitmap(getBitmapFromImageView(image4), image1.getWidth(), image1.getHeight(), null);

        // Set the collage image to the ImageView
        ImageView collageImageView = findViewById(R.id.collageImageView);
        collageImageView.setImageBitmap(collage);
    }

    private Bitmap getBitmapFromImageView(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        return drawable.getBitmap();
    }

}