package edu.wit.mobileapp.instagredients;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity implements RecyclerItemSelectedListener, View.OnClickListener {

    private ImageView speechButton; //Speech to Text
    private EditText speechText; // speech to text
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<Ingredients> ingredients = new ArrayList<>();
    private EditText userInput;
    private ChipGroup mchipGroup;
    private Button findButton;

    private static final int RECOGNIZER_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

        // Speech to Text
        speechButton = findViewById(R.id.imageView);
        userInput = findViewById(R.id.text_ingredient);

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speechIntent = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");
                startActivityForResult(speechIntent,RECOGNIZER_RESULT );
            }
        });
        // recycler view
        recyclerView = findViewById(R.id.recyclerView);
        // user input
        userInput = findViewById(R.id.text_ingredient);
        // chip group
        mchipGroup = findViewById(R.id.chip_group);
        // find recipes button
        findButton = findViewById(R.id.find_button);

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

        // switch activities
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Recipes.class);
                startActivity(intent);
            }
        });


    }

    //voice
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(requestCode == RECOGNIZER_RESULT && requestCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            speechText.setText(matches.get(0).toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
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
