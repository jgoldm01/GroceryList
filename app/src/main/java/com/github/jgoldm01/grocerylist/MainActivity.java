package com.github.jgoldm01.grocerylist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.jgoldm01.grocerylist.adapters.listAdapter;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    DataController dataController;
    ListView listView;

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

        //optimize later to pass the data object itself
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GList gListToVisit = (GList) listView.getItemAtPosition(position);
                String itemValue = gListToVisit.getName();
                goToGListActivity(itemValue);
            }
        });

        dataController.getData();
    }

    protected void onResume () {
        super.onResume();
        updateListView();
    }

    protected void onStop() {
        super.onStop();
        dataController.storeData();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.menu_inventory_button:
                Intent intent = new Intent(this, InventoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // gets editText string, removes spaces before and after. checks edge case of empty string
    // if not empty string
    public void addGList(View view) {
        EditText editText = (EditText) findViewById(R.id.main_gList_entry);
        String newGListName = editText.getText().toString().trim();
        if (!newGListName.equals("")) {
            GList newGList = new GList(newGListName);
            //returns false if glist already exists
            if (!dataController.addGList(newGList)) {
                Toast.makeText(getApplicationContext(), newGListName + " is already a list!",
                        Toast.LENGTH_LONG).show();
            } else {
                updateListView();
            }
        }
        editText.setHint("Enter a List Name");
        editText.setText("");
    }



    private void updateListView() {
        ArrayList gLists = dataController.getGLists();

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, gLists);

        listAdapter adapter = new listAdapter(this, R.layout.list_adapter, gLists, "groceryList");

        listView.setAdapter(adapter);
    }

    //optimize later to pass the GList object itself
    private void goToGListActivity(String gListName) {
        Intent intent = new Intent(this, GListActivity.class);
        intent.putExtra("gListName", gListName);
        startActivity(intent);
    }
}
