package com.github.jgoldm01.grocerylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jgoldm01.grocerylist.Utilities.StringCheck;
import com.github.jgoldm01.grocerylist.adapters.listAdapter;

import java.util.ArrayList;

/**
 * Created by jeremy on 2/12/15.
 */
public class GListActivity extends ActionBarActivity {
    String gListName;
    GList gList;
    DataController dataController;
    ListView listView;
    Menu menu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataController = DataController.getController();
        setContentView(R.layout.glist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        gListName = intent.getStringExtra("gListName");
        gList = dataController.findGList(gListName);

        //initialize text fields specific to food
        TextView title = (TextView) findViewById(R.id.gList_specific_name);
        title.setText(gListName);

        listView = (ListView) findViewById(R.id.gList_list);
        updateListView();
    }

    //update the listView of foods and set the color according to preferences
    protected void onResume () {
        super.onResume();
        updateListView();
        gList.updateEditTime();
        if (dataController.getSortMechanism().equalsIgnoreCase("edited")) {
            dataController.sortGLists();
            //todo:sort each food's list of glists?
        }
        setColor();
    }

    //when activity no longer viewable, update last editTime, and store the data
    protected void onStop() {
        super.onStop();
        dataController.storeData();
    }

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
            case R.id.menu_main_button:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.main_actionbar_delete_icon:
                if (dataController.isDeleteMode()) {
                    toNormalMode();
                } else {
                    toDeleteMode();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toNormalMode() {
        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_delete));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food foodToVisit = (Food) listView.getItemAtPosition(position);
                String foodName = foodToVisit.getName();
                goToFoodActivity(foodName);
            }
        });

        dataController.setDeleteMode(false);
    }

    private void toDeleteMode() {
        menu.getItem(0).setIcon(getResources().getDrawable(R.mipmap.ic_checkmark));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gList.deleteFood(position);
                updateListView();
            }
        });

        dataController.setDeleteMode(true);
    }

    //adding a new food to a list.
    //first attempts to add the food to the inventory. if this is already present in the inventory,
    // the food object with that name is returned.
    // the food is then added to the list, and if it does not already exist in the list the
    // list is added to the food object as well.
    public void addFood(View view) {
        EditText editText = (EditText) findViewById(R.id.gList_food_entry);
        String newFoodName = editText.getText().toString().trim();

        if (!newFoodName.equals("")) {
            if (StringCheck.isNotInt(newFoodName)) {
                Food newFood = new Food(newFoodName);
                //set supply because if this object is used, it was initialized in the list
                newFood.setSupply("None");

                //adds newFood to inventory OR returns the already extant object
                newFood = dataController.addReturnFromInventory(newFood);
                if (!gList.addFood(newFood, dataController.getSortMechanism())) {
                    Toast.makeText(getApplicationContext(), newFoodName + " is already in this list!",
                            Toast.LENGTH_LONG).show();
                } else {
                    //todo: have this function be sorted
                    newFood.addGList(gList);
                    updateListView();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Enter a name, not a number!", Toast.LENGTH_SHORT).show();
            }
        }
        editText.setHint("Enter a Food");
        editText.setText("");
    }

    //returns a string array of the foods in the GList
    private void updateListView() {
        ArrayList foods = gList.getFoods();
        listAdapter adapter = new listAdapter(this, R.layout.list_adapter, foods, "food");
        listView.setAdapter(adapter);
    }

    private void setColor() {
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.glist_relativeLayout);
        rl.setBackgroundColor(dataController.getColor());
    }

    //opens the FoodActivity class, and passes the name of the food in the intent
    private void goToFoodActivity(String foodName) {
        Intent intent = new Intent(this, FoodActivity.class);
        intent.putExtra("foodName", foodName);
        startActivity(intent);
    }
}
