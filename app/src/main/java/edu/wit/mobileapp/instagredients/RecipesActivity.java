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

        // get user input(s)
        Bundle bundle = this.getIntent().getExtras();
        List<String> ingredients = bundle.getStringArrayList("chipArray");

        Log.v("myApp", "chipArray = " + ingredients.toString());

        // find recipes matching user inputs in csv
        readRecipesData(ingredients);

        itemNotFound = (TextView) findViewById(R.id.item_not_found);

        Log.v("myApp", "size = " + recipes.size());

        if(recipes.size() == 0) {
            itemNotFound.setText(R.string.item_not_found);
        } else {
            for (int i = 0; i < recipes.size(); i++) {
                Recipes item = new Recipes();
                item.setTitle(recipes.get(i).getTitle());
                item.setLink(recipes.get(i).getLink());
            }
        }

        // list view
        listView = findViewById(R.id.listView);
        // create list adapter to fill data
        adapter = new RecipeArrayAdapter(this, 0, this.recipes);
        // set adapter
        listView.setAdapter(adapter);
        // add divider to last element
        listView.addFooterView(new View(getApplicationContext()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(RecipesActivity.this, WebViewActivity.class);

                String link = recipes.get(position).getLink();

                Log.v("myApp", link);

                intent.putExtra("link", link);
                startActivity(intent);
            }
        });
    }

    private void readRecipesData(List<String> ingredients) {
        InputStream in = getResources().openRawResource(R.raw.recipes);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));

        String result = "";
        int count = 0;
        try {
            while ((result = br.readLine()) != null) {
                // split by
                String[] tokens = result.split(",", 3);

                // read data
                Recipes recipeData = new Recipes();
                String ingredientsS = tokens[2].replaceAll("\"|\\[|\\]|'", "").replaceAll(" ", "");
                List<String> ingredientsA = new ArrayList<String>(Arrays.asList(ingredientsS.split(",")));

                Log.v("myApp", "ingredients array: " + ingredientsA.toString());

                if (ingredientsA.containsAll(ingredients)) {
//                    String linkedText = String.format("<a href=\"%s\">%s</a> ", tokens[0], tokens[0]);
//                    recipeData.setLink(Html.fromHtml(linkedText));
                    recipeData.setLink(tokens[0]);
                    recipeData.setTitle(tokens[1]);
                    recipeData.setIngredientsArray(ingredientsA);
                    recipes.add(recipeData);
                }
                count++;

                //Log.d("myApp", "Ingredients array: " + tokens[2].replaceAll("\"|\\[|\\]", ""));
            }
        } catch (IOException e) {
            Log.v("myApp", "Error reading data file on line " + result, e);
        }

        Log.v("myApp", "count = " + count);

        if(recipes.size() != 0) {
            Log.d("myApp", "recipes arrayList: " + (recipes.get(0).getIngredientsArray().toString()) + "size: " + recipes.size());
        }
    }
}