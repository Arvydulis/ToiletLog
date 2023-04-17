package com.example.toiletlog;

import static android.provider.Settings.System.getString;

//import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.maps.CameraUpdateFactory;

import java.util.Calendar;
import java.util.Locale;

public class Message extends AppCompatActivity {
    static final String CHANNEL_ID = "test_channel";
    static final int NOTIFICATION_ID = 8000;
    final static int REQUEST_CODE = 200;

    public static void ShowToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void ShowNotification(Activity activity, Context appContext,
                                        String notifTitle, String notifText) {
        boolean getNotifs = PreferenceManager.getDefaultSharedPreferences(activity)
                .getBoolean("notification", true);

        if(getNotifs) {
            // Create notification channel if running on API 26 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Test chanel";
                String description = "My channel";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                NotificationManager notificationManager =
                        activity.getSystemService(NotificationManager.class);

                notificationManager.createNotificationChannel(channel);
            }

            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(activity.getBaseContext(), ListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0,
                    intent, PendingIntent.FLAG_IMMUTABLE);

            // Create notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle(notifTitle)
                    .setContentText(notifText)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(appContext);

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_CODE
                );

            }
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    public static void SetNotif(Context context, Activity activity, int hour, int minute, boolean repeat){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel1 = new NotificationChannel("Daily", "Daily", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = activity.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);

        }


        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if(repeat)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        else
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
