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
import android.widget.TextView;
import android.widget.Toast;
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food foodToVisit = (Food) listView.getItemAtPosition(position);
                String foodName = foodToVisit.getName();
                goToFoodActivity(foodName);
            }
        });
    }

    protected void onResume () {
        super.onResume();
        updateListView();
    }

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
            default:
                return super.onOptionsItemSelected(item);
        }
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
            Food newFood = new Food(newFoodName);
            //set supply because if this object is used, it was initialized in the list
            newFood.setSupply("None");

            //adds newFood to inventory OR returns the already extant object
            newFood = dataController.addReturnFromInventory(newFood);
            if (!gList.addFood(newFood)) {
                Toast.makeText(getApplicationContext(), newFoodName + " is already in this list!",
                        Toast.LENGTH_LONG).show();
            } else {
                newFood.addGList(gList);
                updateListView();
            }
        }
        editText.setHint("Enter a Food");
        editText.setText("");
    }

    //returns a string array of the foods in the GList
    private void updateListView() {
        ArrayList foods = gList.getFoods();

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, foods);

        listAdapter adapter = new listAdapter(this, R.layout.list_adapter, foods, "food");

        listView.setAdapter(adapter);
    }

    //opens the FoodActivity class, and passes the name of the food in the intent
    private void goToFoodActivity(String foodName) {
        Intent intent = new Intent(this, FoodActivity.class);
        intent.putExtra("foodName", foodName);
        startActivity(intent);
    }
}
