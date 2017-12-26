package com.techbrij.wallbyday;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class ActivitySettings extends ActionBarActivity {

    String[] setting;
    ProgressDialog  progress;
    Handler handler; //to detect and do after when async operation finished
    final Activity act = this;
    public static final int OPR_COMPLETED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //Set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle( " " +  this.getString(R.string.action_settings));  //To set space between icon and text
        actionBar.show();
        LoadSettings();

        //To hide the progress bar
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case OPR_COMPLETED:
                        progress.dismiss();
                }
            }
        };
    }

    private AlertDialog AboutDialog()
    {
        AlertDialog aboutDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("About")
                .setMessage("Developed by TechBrij. Visit us at TechBrij.Com for more information.")
                        //   .setIcon(R.drawable.delete)

                .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://techbrij.com"));
                        startActivity(browserIntent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .create();
        return aboutDialogBox;
    }



    void LoadSettings(){
        setting = SettingsHelper.GetSettings(this);
        if (setting != null && setting.length == 2){
          //Set Enable checkbox
          CheckBox checkboxEnable =(CheckBox)findViewById(R.id.checkbox_enable);
          checkboxEnable.setChecked(setting[0].equals("1"));
          //Set Radio Button
          int radId =  getResources().getIdentifier(setting[1], "id", getPackageName());
          RadioButton rad  =(RadioButton)findViewById(radId);
          rad.setChecked(true);
        }
        else{
            Toast.makeText(this, "Problem to load settings", Toast.LENGTH_LONG).show();
        }
    }
    public void onAboutClicked(View view) {
        //About dialog
        AlertDialog diaBox = AboutDialog();
        diaBox.show();
    }

    public void onCheckboxClicked(View view) {

        final Activity context = this;
        final  boolean checked = ((CheckBox) view).isChecked();

        progress = ProgressDialog.show(this, "", "Saving...", true);

        //Thread to show progress bar
        new Thread() {
            public void run() {
                if (checked) {
                    setting[0] = "1";
                    SettingsHelper.SaveSettings(act, 1, setting[1]);
                    //Set wallpaper
                    String picture = SettingsHelper.GetTodayPictureFromFile(act);
                    SettingsHelper.SetWallpaper(picture, setting[1], act);
                    ScheduleHelper.setSchedule(context);
                } else {
                    setting[0] = "0";
                    SettingsHelper.SaveSettings(act, 0, setting[1]);
                    ScheduleHelper.cancelSchedule(context);
                }
                //Trigger operation completed
                handler.sendEmptyMessage(OPR_COMPLETED);
            }
        }.start();
    }


    public void onRadioButtonClicked(View view) {



        progress = ProgressDialog.show(this, "", "Saving...", true);
        // Is the button now checked?
        boolean checked = ((RadioButton)view).isChecked();
        String mode = null;
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.settings_stretch:
                if (checked)
                    mode="settings_stretch";
                break;
            case R.id.settings_fit_width:
                if (checked)
                    mode="settings_fit_width";
                break;
            case R.id.settings_fit_height:
                if (checked)
                    mode="settings_fit_height";
                break;
            case R.id.settings_no_resize:
                if (checked)
                    mode="settings_no_resize";
                break;
        }
        final String selMode = mode;
        new Thread() {
            public void run() {
                try {
                    if (!setting[1].equals(selMode)) {
                        setting[1] = selMode;
                        SettingsHelper.SaveSettings(act, Integer.parseInt(setting[0]), selMode);
                        SettingsHelper.SetWallpaper(act);
                    }
                }
                catch (Exception ex){
                    Toast.makeText(act, "Problem to save settings", Toast.LENGTH_LONG).show();
                }

                //Trigger operation completed
                handler.sendEmptyMessage(OPR_COMPLETED);
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
