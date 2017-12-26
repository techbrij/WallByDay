package com.techbrij.wallbyday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
/*
* When Mobile date or time changed manually then AlarmManager stops to fire Wall receiver
* The solution is create a BroadcastReceiver to listen time or date changes and generate a new alarm.
* This class is used for this
* */


public class DateChangeReceiver extends BroadcastReceiver {
    public DateChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ScheduleHelper.reSchedule(context);
    }
}
