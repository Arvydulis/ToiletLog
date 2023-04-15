package com.example.toiletlog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1; // Unique ID for the notification

    @Override
    public void onReceive(Context context, Intent intent) {
        Handler handler = new Handler(); // Create a new handler for running code on the main thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Do some long-running operation here (e.g. fetch data from server)
                final String data = fetchData();

                // Display the notification on the main thread
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Create the notification
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Daily")
                                .setSmallIcon(R.drawable.baseline_list_24)
                                .setContentTitle("ToiletLog")
                                .setContentText("Did you poop today?")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        // Display the notification
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);


                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        notificationManager.notify(NOTIFICATION_ID, builder.build());
                    }
                });
            }
        }).start();
    }

    private String fetchData() {
        // Perform some long-running operation here (e.g. fetch data from server)
        try {
            Thread.sleep(5000); // Simulate delay for demonstration purposes
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Some data";
    }
}

