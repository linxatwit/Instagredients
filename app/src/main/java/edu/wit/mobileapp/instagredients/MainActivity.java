package edu.wit.mobileapp.instagredients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemSelectedListener, View.OnClickListener {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<Ingredients> ingredients = new ArrayList<>();
    private EditText userInput;
    private ChipGroup mchipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // drawable layout
        dl = (DrawerLayout)findViewById(R.id.activity_main);
        // action bar drawer toggle
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        // recycler view
        recyclerView = findViewById(R.id.recyclerView);
        // user input
        userInput = findViewById(R.id.text_ingredient);
        // chip group
        mchipGroup = findViewById(R.id.chip_group);

        // set layout manager for recycler view
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // call getIngredients() to fill ingredients list
        getIngredients();

        // create recycler adapter to fill data
        recyclerAdapter = new RecyclerAdapter(this, this.ingredients);
        // set adapter
        recyclerView.setAdapter(recyclerAdapter);

        // user input text listener
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userInput = s.toString();
                List<Ingredients> newIngredients = new ArrayList<>();

                // filter ingredients string array
                for (Ingredients ingredient : ingredients) {
                    if(ingredient.getIngredients().contains(userInput)){
                        newIngredients.add(ingredient);
                    }
                }
                recyclerAdapter = new RecyclerAdapter(MainActivity.this, newIngredients);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);

        // navigation bar toggle
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.home:
                        Toast.makeText(MainActivity.this, "Home",Toast.LENGTH_SHORT).show();
                    case R.id.saved:
                        Toast.makeText(MainActivity.this, "Saved",Toast.LENGTH_SHORT).show();
                    default:
                        return true;
                }




            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    // turn item selected into chip
    @Override
    public void onItemSelected(Ingredients ingredient) {
        Chip chip = new Chip(this);
        chip.setText(ingredient.getIngredients());
        chip.setCloseIconVisible(true);
        chip.setCheckable(false);
        chip.setClickable(false);
        chip.setOnCloseIconClickListener(this);

        mchipGroup.addView(chip);
        mchipGroup.setVisibility(View.VISIBLE);
    }

    // add ingredients from string array to list
    private void getIngredients() {
        List<String> ingredientsList = Arrays.asList(getResources().getStringArray(R.array.ingredients));
        Collections.sort(ingredientsList);

        int count = 0;
        for (String ingredient : ingredientsList){
            ingredients.add(new Ingredients(ingredient));
        }
    }

    // remove chip
    @Override
    public void onClick(View v) {
        Chip chip = (Chip) v;
        mchipGroup.removeView(chip);
    }
}