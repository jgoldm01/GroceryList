package com.github.jgoldm01.grocerylist.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.jgoldm01.grocerylist.Constants.Colors;
import com.github.jgoldm01.grocerylist.*;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by jeremy on 3/30/15.
 */
public class listAdapter extends ArrayAdapter{
    int layoutResourceId;
    ArrayList data;
    String type;

    @SuppressWarnings("unchecked")
    public listAdapter(Context context, int layoutResourceId, ArrayList data, String type) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.type = type;
    }

    //sets up the view for each item in the data arraylist
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
        }


        TextView itemName = (TextView) convertView.findViewById(R.id.list_adapter_title);
        TextView itemNotes = (TextView) convertView.findViewById(R.id.list_adapter_notes);
        ImageView itemPic = (ImageView) convertView.findViewById(R.id.list_adapter_image);
        switch (type) {
            case "groceryList": GList glist = (GList) data.get(position);
                                itemName.setText(glist.getName());
                                itemNotes.setText("");
                                itemPic.setImageResource(R.mipmap.ic_list_light);
                                break;
            case "food":        Food food = (Food) data.get(position);
                                itemName.setText(food.getName());
                                itemNotes.setText(food.getNotes());
                                itemPic.setImageResource(R.drawable.ic_apple);
                                switch (food.getSupply()) {
                                    case "Good":    itemPic.setImageResource(R.mipmap.ic_green_apple);
                                                    break;
                                    case "Low":     itemPic.setImageResource(R.mipmap.ic_yellow_apple);
                                                    break;
                                    case "None":    itemPic.setImageResource(R.mipmap.ic_red_apple);
                                                    break;
                                }
                                break;
        }

        return convertView;
    }

}

