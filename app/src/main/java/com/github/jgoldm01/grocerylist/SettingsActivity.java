package com.github.jgoldm01.grocerylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jgoldm01.grocerylist.adapters.settingsColorAdapter;
import com.github.jgoldm01.grocerylist.Constants.Colors;

import org.w3c.dom.Text;

/**
 * Created by jeremy on 4/1/15.
 */
public class SettingsActivity extends ActionBarActivity{
    DataController dataController;
    GridView gridView;
    private Menu menu;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataController = DataController.getController();

        setContentView(R.layout.settings);

        //functions of delete mode are first determined and called by the actionbar
    }

    protected void onResume() {
        super.onResume();
        setColor();
    }

    protected void onStop() {
        super.onStop();
        dataController.storeData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_actionbar, menu);
        this.menu = menu;
        if (dataController.isDeleteMode()) {
            toDeleteMode();
        } else {
            toNormalMode();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        int id = item.getItemId();
        switch(id) {
            case R.id.menu_inventory_button:
                intent = new Intent(this, InventoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_main_button:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.main_actionbar_delete_icon:
                if(dataController.isDeleteMode()) {
                    toNormalMode();
                } else {
                    toDeleteMode();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toNormalMode() {
        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_delete));
        dataController.setDeleteMode(false);
    }

    private void toDeleteMode() {
        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_checkmark));
        dataController.setDeleteMode(true);
    }

    public void setColor(View view) {
        TextView textView = (TextView) view;
        String color = textView.getText().toString();
        dataController.setColor(color);

        setColor();
    }

    public void setSortMechanism(View view) {
        TextView textView = (TextView) view;
        String mechanism = textView.getText().toString();
        dataController.setSortMechanism(mechanism);
        dataController.sort();
        setColor();
    }

    public void setColor() {
        int color = dataController.getColor();
        String sortMechanism = dataController.getSortMechanism();

        LinearLayout ll = (LinearLayout)findViewById(R.id.settings_main_layout);
        ll.setBackgroundColor(color);

        int selectColor, otherColor;
        if (color == Colors.GREEN) {
            selectColor = Colors.YELLOW;
        } else {
            selectColor = Colors.GREEN;
        }
        switch (sortMechanism) {
            case "alphabetical":    findViewById(R.id.sort_alphabetical).setBackgroundColor(selectColor);
                                    findViewById(R.id.sort_edited).setBackgroundColor(color);
                                    findViewById(R.id.sort_created).setBackgroundColor(color);
                                    break;
            case "edited":          findViewById(R.id.sort_edited).setBackgroundColor(selectColor);
                                    findViewById(R.id.sort_alphabetical).setBackgroundColor(color);
                                    findViewById(R.id.sort_created).setBackgroundColor(color);
                                    break;
            case "created":         findViewById(R.id.sort_created).setBackgroundColor(selectColor);
                                    findViewById(R.id.sort_edited).setBackgroundColor(color);
                                    findViewById(R.id.sort_alphabetical).setBackgroundColor(color);
                                    break;
        }
    }
}
