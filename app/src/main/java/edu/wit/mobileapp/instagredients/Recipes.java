package edu.wit.mobileapp.instagredients;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Recipes extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_recipes, frameLayout);
    }
}