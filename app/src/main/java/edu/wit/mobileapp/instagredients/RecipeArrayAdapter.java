package edu.wit.mobileapp.instagredients;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class RecipeArrayAdapter extends ArrayAdapter<Recipes> {
    private LayoutInflater mInflater;
    private String filename = "saveRecipe.csv";

    public RecipeArrayAdapter(Context context, int rid, List<Recipes> recipes) {
        super(context, rid, recipes);
        mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Retrieve data
        Recipes recipe = (Recipes) getItem(position);

        // Use layout file to generate View
        View view= mInflater.inflate(R.layout.item_recipe, null);

        // Set recipe title
        TextView title;
        // Find title text view
        title = (TextView) view.findViewById(R.id.recipe_title);
        // Set text through get method in Recipes class
        title.setText(recipe.getTitle());

        // Set recipe link
        TextView link;
        // Find link text view
        link= (TextView) view.findViewById(R.id.recipe_link);
        // Set text through get method in Recipes class
        link.setText(recipe.getLink());

        ImageView saveButton;
        saveButton =  view.findViewById(R.id.fav_icon);
        FileInputStream fis;
        FileOutputStream outputStream;
        try {
            File file = getContext().getFileStreamPath(filename);
            if (file.exists()) {
                fis = getContext().openFileInput(filename);
            } else {
                outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.close();
                fis = getContext().openFileInput(filename);
            }
        } catch (FileNotFoundException e) {
            fis = null;
            e.printStackTrace();
        } catch (IOException e) {
            fis = null;
            e.printStackTrace();
        }
        InputStreamReader isr= new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String result = "";
        try {
            while (((result = br.readLine()) != null)) {
                String[] tokens = result.split(",", 3);
                if (tokens[0].equals(recipe.getLink())) {
                    saveButton.setImageResource(R.drawable.heart_filled);
                }
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveButton.getDrawable().getConstantState() == view.getContext().getResources().getDrawable(R.drawable.heart_empty).getConstantState()) {
                    saveButton.setImageResource(R.drawable.heart_filled);
                    FileOutputStream outputStream;

                    String string = recipe.getLink() + "," + recipe.getTitle() + "," + recipe.getIngredientsArray() + "\n";
                    //Log.v("myApp", "The full string is: " + string);

                    try{
                        outputStream = getContext().openFileOutput(filename, Context.MODE_APPEND);
                        outputStream.write(string.getBytes());
                    } catch(Exception e){
                        Log.v("myApp", "Error: " + e);
                    }
                } else {
                    saveButton.setImageResource(R.drawable.heart_empty);

                    try {
                        // Read csv file
                        FileInputStream fis = getContext().openFileInput(filename);
                        InputStreamReader isr= new InputStreamReader(fis);
                        BufferedReader br = new BufferedReader(isr);

                        // Create temp file
                        String tempFile = "tempFile.csv";
                        FileOutputStream outputStream = getContext().openFileOutput(tempFile, Context.MODE_PRIVATE);

                        File oldFile = getContext().getFileStreamPath(filename);
                        File newFile = getContext().getFileStreamPath(tempFile);

                        String string = "";

                        String result = "";
                        while ((result = br.readLine()) != null) {
                            // Split by comma in csv 3 times
                            String[] tokens = result.split(",", 3);
                            if (tokens[0].equals(recipe.getLink())) {
                                string += "\n";
                            } else {
                                string += tokens[0] + "," + tokens[1] + "," + tokens[2] + "\n";
                            }
                        }
                        outputStream.write(string.getBytes());
                        oldFile.delete();
                        File dump = getContext().getFileStreamPath(filename);
                        newFile.renameTo(dump);
                        outputStream.close();
                        fis.close();
                    } catch (Exception e) {
                        Log.v("myApp", "Error: " + e);
                    }
                }
            }
        });

        return view;
    }
}
