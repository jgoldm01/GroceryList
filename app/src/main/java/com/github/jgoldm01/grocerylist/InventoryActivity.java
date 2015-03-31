package com.github.jgoldm01.grocerylist;

import android.content.Intent;
import android.graphics.Color;
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
 * Created by jeremy on 2/10/15.
 */
public class InventoryActivity extends ActionBarActivity{
    DataController dataController;
    ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataController = DataController.getController();
        setContentView(R.layout.inventory);
        listView =  (ListView) findViewById(R.id.inventory_list);

//       String[] foods = new String[] {
//               "apple",
//               "banana",
//               "fruit roll ups",
//               "steak",
//               "eye of newt",
//               "tongue of toad"
//       };

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
        int id = item.getItemId();
        switch(id) {
            case R.id.menu_main_button:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addToInventory(View view){
        EditText editText = (EditText) findViewById(R.id.new_inventory_food);
        String newFoodName = editText.getText().toString().trim();

        if (!newFoodName.equals("")) {
            Food newFood = new Food(newFoodName);
            if (!dataController.addToInventory(newFood)) {
                Toast.makeText(getApplicationContext(), newFoodName + " is already in your inventory",
                        Toast.LENGTH_LONG).show();
            } else {
                updateListView();
            }
        }
        editText.setHint("Enter a Food");
        editText.setText("");
    }

    public void goToFoodActivity(String foodName) {
        Intent intent = new Intent(this, FoodActivity.class);
        intent.putExtra("foodName", foodName);
        startActivity(intent);
    }

    private void updateListView() {
        ArrayList foods = dataController.getInventoryFoods();

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, android.R.id.text1, foods);

        listAdapter adapter = new listAdapter(this, R.layout.list_adapter, foods, "food");

        listView.setAdapter(adapter);

        //testing
//        listView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }
}
