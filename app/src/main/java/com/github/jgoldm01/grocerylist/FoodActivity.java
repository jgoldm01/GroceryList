package com.github.jgoldm01.grocerylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jgoldm01.grocerylist.Constants.Colors;

import org.w3c.dom.Text;

/**
 * Created by jeremy on 2/12/15.
 */
public class FoodActivity extends ActionBarActivity {
    String foodName;
    Food food;
    DataController dataController;
    GridView gridView;
    TextView notesView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataController = DataController.getController();
        setContentView(R.layout.food);

        //retrieves the foodName from the intent
        Intent intent = getIntent();
        foodName = intent.getStringExtra("foodName");
        food = dataController.findFood(foodName);

        //initialize text fields specific to food
        TextView title = (TextView) findViewById(R.id.specific_food);
        title.setText(foodName);
        String supply = food.getSupply();
        updateSupplyUI(supply);

        notesView = (TextView) findViewById(R.id.food_notes);

        //sets the grid view of lists containing the food
        gridView =  (GridView) findViewById(R.id.list_grid);
        String[] gLists = food.getStringGLists();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, gLists);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) gridView.getItemAtPosition(position);
                goToGListActivity(itemValue);
            }
        });
    }

    protected void onResume () {
        super.onResume();
        String notes = food.getNotes();
        notesView.setText(notes);
    }

    protected void onPause () {
        super.onPause();
        String notes = notesView.getText().toString().trim();
        food.setNotes(notes);
        dataController.storeFoodData(food);
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

    private void updateSupplyUI(String supply) {
        TextView supplyLevel = (TextView) findViewById(R.id.supply_level);
        supplyLevel.setText("Supply: " + supply);

        View view = this.getWindow().getDecorView();
        switch (supply) {
            case "Good":    view.setBackgroundColor(Colors.GREEN);
                            break;
            case "Low":     view.setBackgroundColor(Colors.YELLOW);
                            break;
            case "None":    view.setBackgroundColor(Colors.RED);
                            break;
        }
    }

    public void setSupply(View view) {
        //extract text from button
        Button b = (Button)view;
        String supplyString = b.getText().toString();

        //set supply level on food object and activity UI
        food.setSupply(supplyString);

        updateSupplyUI(supplyString);
    }

    public void returnToParent(View view) {
        finish();
    }

    private void goToGListActivity(String gListName) {
        Intent intent = new Intent(this, GListActivity.class);
        intent.putExtra("gListName", gListName);
        startActivity(intent);
    }

}
