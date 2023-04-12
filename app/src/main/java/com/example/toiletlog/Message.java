package com.example.toiletlog;

import android.content.Context;
import android.widget.Toast;

public class Message {
    public static void ShowToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
