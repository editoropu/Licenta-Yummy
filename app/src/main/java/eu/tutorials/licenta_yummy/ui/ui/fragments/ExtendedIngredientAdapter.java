package eu.tutorials.licenta_yummy.ui.ui.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import eu.tutorials.licenta_yummy.R;
import eu.tutorials.licenta_yummy.models.ExtendedIngredient;

public class ExtendedIngredientAdapter extends RecyclerView.Adapter<ExtendedIngredientAdapter.ViewHolder> {

    private final List<ExtendedIngredient> ingredientList;
    private final Context context;

    public ExtendedIngredientAdapter(Context context, List<ExtendedIngredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_extended_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExtendedIngredient ingredient = ingredientList.get(position);

        // Log.d(tag, null) arunca NullPointerException, iar getImage() este deseori
        // null pentru ingredientele fara poza. Linia de log a fost scoasa.
        String name = ingredient.getName() != null ? ingredient.getName() : "Ingredient";
        String unit = ingredient.getUnit() != null ? ingredient.getUnit() : "";

        holder.nameTextView.setText(name);
        holder.amountTextView.setText(String.format(java.util.Locale.getDefault(), "%.0f %s", ingredient.getAmount(), unit).trim());
        holder.unitTextView.setText("");

    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView amountTextView;
        private final TextView unitTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.ingredient_name);
            amountTextView = itemView.findViewById(R.id.ingredient_amount);
            unitTextView = itemView.findViewById(R.id.ingredient_unit);
        }
    }
}
