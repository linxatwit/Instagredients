package edu.wit.mobileapp.instagredients;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipesActivity extends BaseActivity {

    private ListView listView;
    private RecipeArrayAdapter adapter;
    private List<Recipes> recipes = new ArrayList<>();
    private TextView itemNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_recipes);
        getLayoutInflater().inflate(R.layout.activity_recipes, frameLayout);

        // Get user input(s)
        Bundle bundle = this.getIntent().getExtras();
        List<String> ingredients = bundle.getStringArrayList("chipArray");

        Log.v("myApp", "chipArray = " + ingredients.toString());

        // Find recipes matching user inputs in csv
        readRecipesData(ingredients);

        // Find text view for to display recipe not found message
        itemNotFound = (TextView) findViewById(R.id.item_not_found);

        //Log.v("myApp", "size = " + recipes.size());

        if(recipes.size() == 0) {
            itemNotFound.setText(R.string.item_not_found);
        } else {
            for (int i = 0; i < recipes.size(); i++) {
                Recipes item = new Recipes();
                item.setTitle(recipes.get(i).getTitle());
                item.setLink(recipes.get(i).getLink());
            }
        }

        // List view
        listView = findViewById(R.id.listView);
        // Create list adapter to fill data
        adapter = new RecipeArrayAdapter(this, 0, this.recipes);
        // Set adapter
        listView.setAdapter(adapter);
        // Add divider to last element
        listView.addFooterView(new View(getApplicationContext()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(RecipesActivity.this, WebViewActivity.class);

                String link = recipes.get(position).getLink();

                //Log.v("myApp", link);

                intent.putExtra("link", link);
                startActivity(intent);
            }
        });
    }

    private void readRecipesData(List<String> ingredients) {
        // Read csv file
        InputStream in = getResources().openRawResource(R.raw.recipes);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));

        String result = "";
        //int count = 0;
        try {
            while ((result = br.readLine()) != null) {
                // Split by comma in csv 3 times
                String[] tokens = result.split(",", 3);

                // Read data, create new Recipes object
                Recipes recipeData = new Recipes();
                // Replace unnecessary characters in ingredients string with empty
                String ingredientsS = tokens[2].replaceAll("\"|\\[|\\]|'", "").replaceAll(" ", "");
                // Change the string from string to array list
                List<String> ingredientsA = new ArrayList<String>(Arrays.asList(ingredientsS.split(",")));

                //Log.v("myApp", "ingredients array: " + ingredientsA.toString());

                if (ingredientsA.containsAll(ingredients)) {
                    // Set link in array list
                    recipeData.setLink(tokens[0]);
                    // Set title in array list
                    recipeData.setTitle(tokens[1]);
                    // Set ingredients array in array list
                    recipeData.setIngredientsArray(ingredientsA);
                    // Add the data to recipes array list
                    recipes.add(recipeData);
                }
                //count++;

                //Log.d("myApp", "Ingredients array: " + tokens[2].replaceAll("\"|\\[|\\]", ""));
            }
        } catch (IOException e) {
            Log.v("myApp", "Error reading data file on line " + result, e);
        }

        //Log.v("myApp", "count = " + count);

        //if(recipes.size() != 0) {
        //    Log.d("myApp", "recipes arrayList: " + (recipes.get(0).getIngredientsArray().toString()) + "size: " + recipes.size());
        //}
    }
}