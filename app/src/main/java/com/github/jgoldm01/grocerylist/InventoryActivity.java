package com.github.jgoldm01.grocerylist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jgoldm01.grocerylist.Utilities.StringCheck;
import com.github.jgoldm01.grocerylist.adapters.listAdapter;

import java.util.ArrayList;

/**
 * Created by jeremy on 2/10/15.
 */
public class InventoryActivity extends ActionBarActivity{
    DataController dataController;
    ListView listView;
    Menu menu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataController = DataController.getController();
        setContentView(R.layout.inventory);
        listView =  (ListView) findViewById(R.id.inventory_list);
        updateListView();
    }

    protected void onResume () {
        super.onResume();
        updateListView();
        setColor();
    }

    protected void onStop () {
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

        //optimize later to pass the data object itself
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataController.deleteFood(position);
                updateListView();
            }
        });

        dataController.setDeleteMode(true);
    }

    public void addToInventory(View view){
        EditText editText = (EditText) findViewById(R.id.new_inventory_food);
        String newFoodName = editText.getText().toString().trim();

        if (!newFoodName.equals("")) {
            if(StringCheck.isNotInt(newFoodName)) {
                Food newFood = new Food(newFoodName);
                if (!dataController.addToInventory(newFood)) {
                    Toast.makeText(getApplicationContext(), newFoodName + " is already in your inventory",
                            Toast.LENGTH_LONG).show();
                } else {
                    updateListView();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Enter a name, not a number!", Toast.LENGTH_SHORT).show();
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
        listAdapter adapter = new listAdapter(this, R.layout.list_adapter, foods, "food");
        listView.setAdapter(adapter);
    }

    private void setColor() {
        LinearLayout ll = (LinearLayout)findViewById(R.id.inventory_linear_layout);
        ll.setBackgroundColor(dataController.getColor());
    }
}
