package com.techbrij.wallbyday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * Created by Brij on 4/19/2015.
 */
public class WallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            SettingsHelper.SetWallpaper(context);
            if (MyActivity.debugToast) {
                Toast.makeText(context, "Wallpaper is changed successfully", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex){
            ex.printStackTrace();
            if (MyActivity.debugToast) {
                Toast.makeText(context, "WallByDay failed to change wallpaper", Toast.LENGTH_LONG).show();
            }
        }
          /*  if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {

            }*/
    }
}
