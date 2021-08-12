package edu.wit.mobileapp.instagredients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ViewSavedActivity extends BaseActivity {

    private ListView listView;
    private SaveRecipeArrayAdapter adapter;
    private List<Recipes> recipes = new ArrayList<>();
    private TextView noSavedRecipes;
    private String filename = "saveRecipe.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_recipes);
        getLayoutInflater().inflate(R.layout.activity_view_saved, frameLayout);

        // Find recipes matching user inputs in csv
        try {
            readRecipesData();
        } catch (FileNotFoundException e) {
            Log.v("myApp", "Error = " + e);
        }

        // Find text view for to display recipe not found message
        noSavedRecipes = (TextView) findViewById(R.id.no_saved_recipes);

        //Log.v("myApp", "size = " + recipes.size());

        if(recipes.size() == 0) {
            noSavedRecipes.setText(R.string.no_saved_recipes);
        } else {
            Collections.sort(recipes, new TitleComparator());
            for (int i = 0; i < recipes.size(); i++) {
                Recipes item = new Recipes();
                item.setTitle(recipes.get(i).getTitle());
                item.setLink(recipes.get(i).getLink());
                item.setSaveText(recipes.get(i).getSaveText());
            }
        }

        // List view
        listView = findViewById(R.id.listView2);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        // Create list adapter to fill data
        adapter = new SaveRecipeArrayAdapter(this, 0, this.recipes);
        // Set adapter
        listView.setAdapter(adapter);
        // Add divider to last element
        listView.addFooterView(new View(getApplicationContext()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ViewSavedActivity.this, WebViewActivity.class);

                String link = recipes.get(position).getLink();

                //Log.v("myApp", link);

                intent.putExtra("link", link);
                startActivity(intent);
            }
        });
    }

    private void readRecipesData() throws FileNotFoundException {
        // Read csv file
        FileInputStream fis = getApplicationContext().openFileInput(filename);
        InputStreamReader isr= new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String result = "";
        //int count = 0;
        try {
            while ((result = br.readLine()) != null) {
                Log.v("myApp", "result: " + result.length());
                if (result.length() == 0){
                    continue;
                } else if (result.trim().isEmpty()){
                    break;
                }
                // Split by comma in csv 3 times
                String[] tokens = result.split(",", 3);

                // Read data, create new Recipes object
                Recipes recipeData = new Recipes();
                // Get ingredients array in csv
                String ingredientsS = tokens[2].replaceAll("\\[|\\]|'", "");
                // Change the string from string to array list
                List<String> ingredientsA = new ArrayList<String>(Arrays.asList(ingredientsS.split(",")));

                Log.v("myApp", "ingredients array: " + ingredientsA.toString());

                // Set link in array list
                recipeData.setLink(tokens[0]);
                // Set title in array list
                recipeData.setTitle(tokens[1].replaceAll("\"|\\[|\\]|'", ""));
                // Set ingredients array in array list
                recipeData.setIngredientsArray(ingredientsA);
                // Set save state
                recipeData.setSaveText(String.valueOf(R.string.saveText));
                // Add the data to recipes array list
                recipes.add(recipeData);

                //Log.d("myApp", "Ingredients array: " + tokens[2].replaceAll("\"|\\[|\\]", ""));
            }
        } catch (FileNotFoundException e) {
            Log.v("myApp", "Error = " + e);
        } catch (IOException e) {
            Log.v("myApp", "Error reading data file on line " + result, e);
        }

        //Log.v("myApp", "count = " + count);

        //if(recipes.size() != 0) {
        //    Log.d("myApp", "recipes arrayList: " + (recipes.get(0).getIngredientsArray().toString()) + "size: " + recipes.size());
        //}
    }
}