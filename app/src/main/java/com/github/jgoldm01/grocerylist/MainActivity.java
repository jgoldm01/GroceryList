package com.github.jgoldm01.grocerylist;

import java.util.Scanner;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.text.BoringLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.jgoldm01.grocerylist.adapters.listAdapter;
import com.github.jgoldm01.grocerylist.Utilities.StringCheck;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    DataController dataController;
    ListView listView;
    private Menu menu;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataController = DataController.getController();
        //sets the context so that the dataController can access sharedPreferences
        dataController.setContext(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.main_list);
        updateListView();

        //functions of delete mode are first determined and called by the actionbar
    }



    protected void onResume () {
        super.onResume();
        updateListView();
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
            case R.id.menu_main_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_inventory_button:
                intent = new Intent(this, InventoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.main_actionbar_delete_icon:
                if(dataController.isDeleteMode()) {
                    toNormalMode();
                } else {
                    toDeleteMode();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //changes icon to an X, and sets the onclicklistener to link to the activity
    private void toNormalMode() {
        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_delete));

        //optimize later to pass the data object itself
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GList gListToVisit = (GList) listView.getItemAtPosition(position);
                String gListName = gListToVisit.getName();
                goToGListActivity(gListName);
            }
        });

        dataController.setDeleteMode(false);
    }

    //changes icon to an checkMark, and sets the onclicklistener to delete the item from data
    private void toDeleteMode() {
        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_checkmark));

        //optimize later to pass the data object itself
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataController.deleteGList(position);
                updateListView();
            }
        });

        dataController.setDeleteMode(true);
    }

    // gets editText string, removes spaces before and after. checks edge case of empty string
    // if not empty string
    public void addGList(View view) {
        EditText editText = (EditText) findViewById(R.id.main_gList_entry);
        String newGListName = editText.getText().toString().trim();

        if (!newGListName.equals("")) {
            if (StringCheck.isNotInt(newGListName)) {
                GList newGList = new GList(newGListName);
                //returns false if glist already exists
                if (!dataController.addGList(newGList)) {
                    Toast.makeText(getApplicationContext(), newGListName + " is already a list!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    updateListView();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Enter a name, not a number!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        editText.setHint("Enter a List Name");
        editText.setText("");
    }



    private void updateListView() {
        ArrayList gLists = dataController.getGLists();
        listAdapter adapter = new listAdapter(this, R.layout.list_adapter, gLists, "groceryList");
        listView.setAdapter(adapter);
    }

    private void setColor() {
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.main_relative_layout);
        rl.setBackgroundColor(dataController.getColor());
    }

    //optimize later to pass the GList object itself
    private void goToGListActivity(String gListName) {
        Intent intent = new Intent(this, GListActivity.class);
        intent.putExtra("gListName", gListName);
        startActivity(intent);
    }
}
