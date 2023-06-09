package com.example.bookapp;

import android.app.Application;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;
//application class runs before your launch activity
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
//created a static method to convert timestamp to proper date format, so we can use it everywhere in project
    public static final String formatTimestamp(long timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        //format timestamp to ss/MM/YYYY
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        return date;
    }
}
