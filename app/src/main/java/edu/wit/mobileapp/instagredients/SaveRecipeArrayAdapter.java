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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public class SaveRecipeArrayAdapter extends ArrayAdapter<Recipes> {
    private LayoutInflater mInflater;
    private String filename = "saveRecipe.csv";

    public SaveRecipeArrayAdapter(Context context, int rid, List<Recipes> recipes) {
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
        saveButton = view.findViewById(R.id.fav_icon);
        saveButton.setImageResource(R.drawable.heart_filled);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        if (result.length() == 0){
                            continue;
                        } else if (result.trim().isEmpty()){
                            break;
                        }
                        // Split by comma in csv 3 times
                        String[] tokens = result.split(",", 3);
                        if (tokens[0].equals(recipe.getLink())) {
                            string += "";
                        } else if (tokens.length == 0 ) {
                            continue;
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
        });

        return view;
    }
}
