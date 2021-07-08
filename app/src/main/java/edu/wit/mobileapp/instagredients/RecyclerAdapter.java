package edu.wit.mobileapp.instagredients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private Context context;
    private List<Ingredients> ingredients;
    private RecyclerItemSelectedListener itemSelectedListener;

    public RecyclerAdapter(Context context, List<Ingredients> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
        itemSelectedListener = (MainActivity) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ingredientName.setText(ingredients.get(position).getIngredients());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView ingredientName;
        LinearLayout RootView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientName = itemView.findViewById(R.id.ingredient_name);
            RootView = itemView.findViewById(R.id.rootView);
            RootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemSelectedListener.onItemSelected(ingredients.get(getAdapterPosition()));
        }
    }

    public void updateList(List<Ingredients> newIngredients) {
        ingredients.clear();
        ingredients.addAll(newIngredients);
        notifyDataSetChanged();
    }
}
