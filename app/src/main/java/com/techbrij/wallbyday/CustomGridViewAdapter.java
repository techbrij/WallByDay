package com.techbrij.wallbyday;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.provider.MediaStore;
/**
 * Created by Administrator on 4/11/2015.
 * To display individual week button layout
 */
public class CustomGridViewAdapter extends ArrayAdapter<ListItem> {
    //

    //
    Context context;
    int layoutResourceId;
    ArrayList<ListItem> data = new ArrayList<ListItem>();
    int itemWidth;

    public CustomGridViewAdapter(Context context, int layoutResourceId,
                                 ArrayList<ListItem> data,int width) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.itemWidth = width;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.FilePath);


            holder.imageItem = (ImageView) row.findViewById(R.id.Thumbnail);


            holder.position = position;
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        //Assign Data
        ListItem item = data.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.imageItem.setImageBitmap(item.getImage());

        //Set Width
        holder.txtTitle.setWidth(itemWidth);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(itemWidth, itemWidth);
        holder.imageItem.setLayoutParams(params);
        //row.setLayoutParams(params);


        //Set Click event
        if(context instanceof MyActivity){
            //set thumbnail click handler
            row.setOnClickListener( ((MyActivity)context).thumbnailClick);
        }


        return row;
    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;
        int position;
    }
}
