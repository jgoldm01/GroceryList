package com.github.jgoldm01.grocerylist.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.github.jgoldm01.grocerylist.R;

import java.util.ArrayList;

/**
 * Created by jeremy on 4/1/15.
 */
public class settingsColorAdapter extends BaseAdapter{
    Context context;
    int layoutResourceId;
    int [] data;

    @SuppressWarnings("unchecked")
    public settingsColorAdapter(Context context, int layoutResourceId, int[] data) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);
        }

        View colorView = convertView.findViewById(R.id.settings_individual_color);
        colorView.setBackgroundColor(data[position]);
        Log.i("here", "here");

        return convertView;
    }

    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
