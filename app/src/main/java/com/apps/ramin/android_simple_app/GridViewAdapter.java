package com.apps.ramin.android_simple_app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Created by Ramin on 07/06/2017.
 */

public class GridViewAdapter extends ArrayAdapter<Bitmap> {
    Context context;
    int layoutResourceId;
    ArrayList<Bitmap> data = new ArrayList<Bitmap>();

    public GridViewAdapter(Context context, int layoutResourceId,
                                 ArrayList<Bitmap> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            // holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        Bitmap item = data.get(position);
        holder.imageItem.setImageBitmap(item);
        return row;

    }

    static class RecordHolder {
        ImageView imageItem;
    }
}
