package com.techbrij.wallbyday;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Brij on 5/3/2015.
 */
public class ScheduleHelper {


    public static boolean isSchedule(Context context){
        // check task is scheduled or not
        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent(context,WallReceiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        return alarmUp;
    }

//*********************
// To set schedule if not set
//*********************
 public static void setSchedule(Context context){

        if ( !isSchedule(context)) {

            Intent intent = new Intent(context,WallReceiver.class);
            intent.putExtra("activate", true);
            PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(context, 0,
                            intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 1);
            calendar.set(Calendar.SECOND, 0);

            AlarmManager alarmManager =
                    (AlarmManager)
                            context.getSystemService(context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                    pendingIntent);

        }
     if (MyActivity.debugToast) {
         Toast.makeText(context, "WallByDay: Set Schedule fired.", Toast.LENGTH_LONG).show();
     }
    }
//*********************
// To update schedule / Reschedule
//*********************
    public static  void reSchedule(Context context) {
      if (isSchedule(context)) {
            Intent intent = new Intent(context,WallReceiver.class);
            //update the existing service
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 1);
            calendar.set(Calendar.SECOND, 0);


            AlarmManager alarmManager = (AlarmManager)
                    context.getSystemService(context.ALARM_SERVICE);
            PendingIntent pendingIntent2 =
                    PendingIntent.getBroadcast(context, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent2);
        }
        if (MyActivity.debugToast) {
            Toast.makeText(context, "WallByDay: ReSchedule fired.", Toast.LENGTH_LONG).show();
        }
    }

//*********************
// To cancel schedule
//*********************
    public static  void cancelSchedule(Context context) {
        if (isSchedule(context)) {
            Intent intent = new Intent(context,WallReceiver.class);
            PendingIntent pendingIntent2 =
                    PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager)
                    context.getSystemService(context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent2);
        }
        if (MyActivity.debugToast) {
            Toast.makeText(context, "WallByDay: Cancel Schedule fired.", Toast.LENGTH_LONG).show();
        }
    }

//*********************
// If app is enabled then schedule it else cancel it
//*********************

    public static  void settingsBasedSchedule(Context context) {
        String[] setting = SettingsHelper.GetSettings(context);
        if (setting[0].equals("1")){
            setSchedule(context);
        }
        else{
            cancelSchedule(context);
        }
    }


}
