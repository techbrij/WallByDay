package com.techbrij.wallbyday;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Dictionary;

/**
 * Created by Brij on 4/19/2015.
 */
public class SettingsHelper {

//Settings File Structure
// First line will show Enable or Disable. 1 For Enable, 0 For Disable
// Second line will show mode
public static String[] GetSettings(Context context) {
    String[] ret;
    try {
        ret = FileManager.readArrayFromFile(context.getFileStreamPath(MyActivity.SETTING_FILE_NAME));
    } catch (Exception e){
        ret = null;
    }
    if (ret == null || ret.length == 0){
        ret = new String[] { "1", "settings_stretch" };
    }
    return ret;
}


//To save settings
public static void SaveSettings(Context context, int isEnabled, String mode) {

        FileManager.writeArrayToFileSync
                (context.getFileStreamPath(MyActivity.SETTING_FILE_NAME),
                        new String[]{ "" + isEnabled,mode });
    }

//Get picture of today
public static String GetTodayPictureFromFile(Context context){
        String picture = null;
        File file =context.getFileStreamPath(MyActivity.DATA_FILE_NAME);
        if (file!=null){
            String[] ret = FileManager.readArrayFromFile(file);
            if (ret.length == 7){
                picture = ret[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1];
            }
        }
        return picture;
    }

    public static void SetBlankWallpaper(Context context){
        String[] setting = SettingsHelper.GetSettings(context);
        if (setting[0].equals("1")){
            //Get Screen Size
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;
            //Create Black image
            Bitmap img = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            img.eraseColor(Color.BLACK);
            try {
                WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);
                myWallpaperManager.setBitmap(img);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }




//Read settings and set wallpaper
public static void SetWallpaper(Context context){
        String[] setting = SettingsHelper.GetSettings(context);
        if (setting[0].equals("1")){
            String picture = GetTodayPictureFromFile(context);
            SetWallpaper(picture,setting[1],context);
        }

    }






    public static int GetListItemWidth(Context context, int spacingBetweenListItem, int gridHorzPadding){
        //Get Screen Size
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        float ratio =(float) height/width;

        if(ratio > 2.0){
            return (width -gridHorzPadding -spacingBetweenListItem) /2;
        }
        else if (ratio > 1.0){
            return (width -gridHorzPadding -spacingBetweenListItem*2)/3;
        }
        else if (ratio > 0.5){
            return (height -gridHorzPadding -spacingBetweenListItem*2)/3;
        }
        else {
            return (height -gridHorzPadding -spacingBetweenListItem)/2;
        }
    }


//To set image as wallpaper in the defined mode
public static void SetWallpaper( String picture, String mode, Context context){
        if (picture == null ||  picture.isEmpty())
            return;
        else {
            //Get Screen Size
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            //((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;

           // Bitmap imageOrg = BitmapFactory.decodeFile(picture);
            Bitmap imageOrg = BitmapHelper.decodeSampledBitmapFromPath(picture,width,height);
            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(context);//getApplicationContext()
            try {
                if (mode.equals("settings_no_resize")) {
                    // Default Fit to all
                    myWallpaperManager. setBitmap(imageOrg);
                }
                else {
                    Bitmap imageScaled = null;
                    if (mode.equals("settings_stretch")) {
                        // STRETCH (Resize to display height width, ignoring image original height width ratio)
                        imageScaled= Bitmap.createScaledBitmap(imageOrg, width, height, true);

                    }
                    if (mode.equals("settings_fit_width")) {
                        int newHeight = imageOrg.getHeight() * width/imageOrg.getWidth();
                        imageScaled= Bitmap.createScaledBitmap(imageOrg, width, newHeight, true);
                    }
                    if (mode.equals("settings_fit_height")) {
                        int newWidth = imageOrg.getWidth() * height/imageOrg.getHeight();
                        imageScaled= Bitmap.createScaledBitmap(imageOrg, newWidth, height, true);
                    }
                    myWallpaperManager.setWallpaperOffsetSteps(1, 1);
                    myWallpaperManager.suggestDesiredDimensions(width, height);
                    myWallpaperManager.setBitmap(imageScaled);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}




