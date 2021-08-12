package edu.wit.mobileapp.instagredients;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AllRecipesActivity extends BaseActivity {

    private List<Recipes> recipes = new ArrayList<>();
    private ListView listView;
    private RecipeArrayAdapter adapter;
    private TextView itemNotFound;
    ImageView filter;
    EditText search_ingredients;
    ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_all_recipes);
        getLayoutInflater().inflate(R.layout.activity_all_recipes, frameLayout);


        readRecipesData();
        itemNotFound = (TextView) findViewById(R.id.item_not_found);
        filter = findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AllRecipesActivity.this, MainActivity.class);
                startActivity(it);
            }
        });

        search_ingredients = findViewById(R.id.search_ingredients);
        search = findViewById(R.id.searchBtn);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_ingredients.getText().toString().trim().isEmpty()){
                    populateListView(recipes);
                    return;
                }
                filterRecipes(search_ingredients.getText().toString());
            }
        });

        populateListView(recipes);
    }

    private void populateListView(List<Recipes> list){

        Toast.makeText(AllRecipesActivity.this, "Retrieved Recipes" , Toast.LENGTH_SHORT).show();

        if (list.size() == 0) {
            itemNotFound.setText(R.string.item_not_found);
        } else {
            Collections.sort(list, new TitleComparator());
            for (int i = 0; i < list.size(); i++) {
                Recipes item = new Recipes();
                item.setTitle(list.get(i).getTitle());
                item.setLink(list.get(i).getLink());
                item.setSaveText(list.get(i).getSaveText());
            }
        }

        // List view
        listView = findViewById(R.id.listView);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        // Create list adapter to fill data
        adapter = new RecipeArrayAdapter(this, 0, list);
        // Set adapter
        listView.setAdapter(adapter);
        // Add divider to last element
        listView.addFooterView(new View(getApplicationContext()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(AllRecipesActivity.this, WebViewActivity.class);
                String link = list.get(position).getLink();
                //Log.v("myApp", link);
                intent.putExtra("link", link);
                startActivity(intent);
            }
        });
    }

    private void readRecipesData() {
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

    public void filterRecipes(String keyword) {

         List<Recipes> fRecipes = new ArrayList<>();

        for (int i = 0; i < recipes.size(); i++) {
            if (keyword.equalsIgnoreCase("dairy free")) {
                if (recipes.get(i).getIngredientsArray().contains("milk") || recipes.get(i).getIngredientsArray().contains("cheese")
                        || recipes.get(i).getIngredientsArray().contains("yogurt") || recipes.get(i).getIngredientsArray().contains("cream")) {
                }else{
                    fRecipes.add(recipes.get(i));
                }
            }else if (keyword.equalsIgnoreCase("sugar free")) {
                if (recipes.get(i).getIngredientsArray().contains("sugar")) {

                }else{
                    fRecipes.add(recipes.get(i));
                }
            }
        }

        populateListView(fRecipes);
    }
}