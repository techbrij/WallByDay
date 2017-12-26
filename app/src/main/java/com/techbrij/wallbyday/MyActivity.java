package com.techbrij.wallbyday;

import java.io.File;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;




public class MyActivity extends ActionBarActivity {
//************************************
    //Config Variable Start

    //file to save path of images
    final static String DATA_FILE_NAME = "datafile.txt";
    //file to save settings
    final static String SETTING_FILE_NAME = "settings.txt";
    //path of images
    String[] wallInfo= new String[7];
    int listItemWidth;
    final static boolean debugToast = false;

    //Config Variable End
//************************************





//***********************



    File file;
    GridView gridView;
    ArrayList<ListItem> gridArray = new ArrayList<ListItem>();
    CustomGridViewAdapter customGridAdapter;

    //Selected day index
    private int selectedIndex;

    public String[] getShortWeekdays() {
        String[] namesOfDays =  DateFormatSymbols.getInstance().getWeekdays();
        return Arrays.copyOfRange(namesOfDays,1,namesOfDays.length);
    }

    protected void loadData(){
        String[] ret = FileManager.readArrayFromFile(file);
        if (ret.length == 7){
            wallInfo = ret;
        }
    }

    protected void saveData(){
        FileManager.writeArrayToFile(file,wallInfo );
    }


    protected void initApp() {
        // init file
        try{
            file = getFileStreamPath(DATA_FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            else{
                loadData();
            }
            } catch (IOException e) {
                e.printStackTrace();
            }

        try{
           File fileSetting = getFileStreamPath(SETTING_FILE_NAME);
            if (!fileSetting.exists()) {
                fileSetting.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);

        //to set schedule
        ScheduleHelper.settingsBasedSchedule(this);

        //Set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle( " " + this.getString(R.string.app_name));  //To set space between icon and text
        actionBar.show();

        //initialize app
        initApp();

        //set grid view list item width
        int defaultSpacing = (int)(10 * getResources().getDisplayMetrics().density); // default -5dp both side
        int horzSpacing = 2* getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) +defaultSpacing;
        int listItemSpacing = getResources().getDimensionPixelSize(R.dimen.listitem_spacing);
        listItemWidth =  SettingsHelper.GetListItemWidth(this, listItemSpacing, horzSpacing);


        Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.a);
        String[] shortWeekdays = getShortWeekdays();
        File imgFile = null;
        for (int i = 0; i < 7; i++) {

            if (wallInfo[i] != null &&  !wallInfo[i].isEmpty()) {
                imgFile = new File(wallInfo[i]);
            }
            //In case of default, no selected
            if (imgFile== null || !imgFile.exists() || wallInfo[i].isEmpty()) {
                gridArray.add(new ListItem(homeIcon, shortWeekdays[i]));
                //case when file is removed from the mobile
                wallInfo[i] = "";
            }
            else{
                Bitmap icon = BitmapHelper.decodeSampledBitmapFromPath(imgFile.getAbsolutePath(),listItemWidth,listItemWidth);
                //To stretch the image
                //Comment below line if Stretch not needed
                icon = Bitmap.createScaledBitmap(icon, listItemWidth, listItemWidth, true);
                gridArray.add(new ListItem(icon, shortWeekdays[i]));
            }
        }

        gridView = (GridView) findViewById(R.id.gridView1);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.listitem, gridArray, listItemWidth);
        gridView.setColumnWidth(listItemWidth);
        gridView.setAdapter(customGridAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case R.id.action_settings:
                Intent intent = new Intent
                        (getApplicationContext(), ActivitySettings.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    public View.OnClickListener thumbnailClick= new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            int pos = ((CustomGridViewAdapter.RecordHolder)v.getTag()).position;
            //Note: putExtra not working (system activities will not send back the extras)
            //http://stackoverflow.com/questions/2859831/startactivityforresult-and-intents-extras-it-seems-extras-are-not-pushed-back
            selectedIndex = pos;


            if (wallInfo[pos] != null &&  !wallInfo[pos].isEmpty()) {

                String[] shortWeekdays = getShortWeekdays();
                Intent intent = new Intent
                        (getApplicationContext(), ActivityViewImage.class);
                intent.putExtra("filename", wallInfo[pos]);
                intent.putExtra("dayTitle", shortWeekdays[pos]);
                startActivityForResult(intent,0);
            }
            else {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case 0: //Change or Delete Existing Image
                if (resultCode == RESULT_OK && null != data) {
                    String opr=data.getStringExtra("operation");
                    if (opr.equals("delete")) {
                       if (DeleteImage(selectedIndex)){
                           Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                       }else{
                           Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                       }
                    }
                    if (opr.equals("change")) {
                        //To select new image
                        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 1);
                    }
                }

                break;
            case 1: // New Image is selected
                try {
                    if (resultCode == RESULT_OK && null != data) {

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        // String picturePath contains the path of selected Image
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        if(SetImageFromPath(selectedIndex, picturePath)){
                          Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                            //if it is current date, change wallpaper
                            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1 == selectedIndex){
                                SettingsHelper.SetWallpaper(this);
                            }
                        }
                        else{
                          Toast.makeText(this, "ImageView not found", Toast.LENGTH_LONG).show();
                      }
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                break;
        }



    }

    //To set user selected image in ImageView in Gridview
    private boolean SetImageFromPath(int position, String picturePath){
        ImageView img = (ImageView)gridView.getChildAt(position).findViewById(R.id.Thumbnail);
        if (img != null) {
          wallInfo[position] = picturePath;
          saveData();
            //Get dp values in pixels
            Bitmap icon = BitmapHelper.decodeSampledBitmapFromPath(picturePath, listItemWidth, listItemWidth);
            //Comment below line if Stretch not needed
            icon = Bitmap.createScaledBitmap(icon, listItemWidth, listItemWidth, true);
            img.setImageBitmap(icon);
          return true;
      }
      else{
         return false;
      }
    }

    //To delete image and reset imageview with default icon
    private boolean DeleteImage(int position){
        ImageView img = (ImageView)gridView.getChildAt(position).findViewById(R.id.Thumbnail);
        if (img != null) {
            wallInfo[position] = "";
            saveData();
            Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.a);
            img.setImageBitmap(homeIcon);

            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1 == position){
                //For today, if user deletes image, it should set black wallpaper
               SettingsHelper.SetBlankWallpaper(this);
            }
            return true;
        }
        else{
            return false;
        }
    }




}
