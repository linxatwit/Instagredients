package edu.wit.mobileapp.instagredients;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeArrayAdapter extends ArrayAdapter<Recipes> {
    private LayoutInflater mInflater;

    public RecipeArrayAdapter(Context context, int rid, List<Recipes> recipes) {
        super(context, rid, recipes);
        mInflater= (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
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
        title = (TextView) view.findViewById(R.id.recipe_title);
        title.setText(recipe.getTitle());

        // Set recipe link
        TextView link;
        link= (TextView) view.findViewById(R.id.recipe_link);
        link.setText(recipe.getLink());
        link.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }
}
