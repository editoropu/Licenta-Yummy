package eu.tutorials.licenta_yummy.ui.ui.fragments;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import eu.tutorials.licenta_yummy.R;
import eu.tutorials.licenta_yummy.data.DatabaseHelper;
import eu.tutorials.licenta_yummy.databinding.FragmentOverviewBinding;
import eu.tutorials.licenta_yummy.models.FavoriteModel;
import eu.tutorials.licenta_yummy.ui.ui.RecipeStore;

public class OverviewFragment extends Fragment {

    FragmentOverviewBinding binding;
    List<FavoriteModel> favList;
    DatabaseHelper myDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Daca aplicatia a fost repornita de Android direct pe acest ecran,
        // reteta nu mai e in memorie. Iesim in loc sa crapam.
        if (!RecipeStore.INSTANCE.isReady()) {
            return view;
        }

        myDB = new DatabaseHelper(getContext());
        favList = new ArrayList<>();
        favList = myDB.getAllFavoritesData();

        refreshFavoriteButton();
//        for(FavoriteModel fav : favList){
//            if(fav.\.equals(RecipeStore.recipe.getId()))
//        }
        Picasso.get().load(RecipeStore.recipe.getImage()).placeholder(R.drawable.holder).into(binding.imgRecipe);
        binding.tvTime.setText(RecipeStore.recipe.getReadyInMinutes()+"");
        binding.tvLikes.setText(RecipeStore.recipe.getAggregateLikes()+"");
        binding.recipeTitle.setText(RecipeStore.recipe.getTitle()+"");
        binding.recipeDescription.setText(Html.fromHtml(RecipeStore.recipe.getSummary(), Html.FROM_HTML_MODE_LEGACY));

        if(RecipeStore.recipe.getCheap()){
            binding.tvCheap.setTextColor(getResources().getColor(R.color.md_primary));
            binding.tvCheap.setTypeface(binding.tvCheap.getTypeface(), android.graphics.Typeface.BOLD);
            Drawable drawable = binding.tvCheap.getCompoundDrawablesRelative()[0]; // 0 = iconita din stanga
            if (drawable != null) {
                drawable.setColorFilter(getResources().getColor(R.color.md_primary), PorterDuff.Mode.SRC_IN);
            }
        }
        if(RecipeStore.recipe.getVegetarian()){
            binding.tvVegetarian.setTextColor(getResources().getColor(R.color.md_primary));
            binding.tvVegetarian.setTypeface(binding.tvVegetarian.getTypeface(), android.graphics.Typeface.BOLD);
            Drawable drawable = binding.tvVegetarian.getCompoundDrawablesRelative()[0]; // 0 = iconita din stanga
            if (drawable != null) {
                drawable.setColorFilter(getResources().getColor(R.color.md_primary), PorterDuff.Mode.SRC_IN);
            }
        }
        if(RecipeStore.recipe.getVegan()){
            binding.tvVegan.setTextColor(getResources().getColor(R.color.md_primary));
            binding.tvVegan.setTypeface(binding.tvVegan.getTypeface(), android.graphics.Typeface.BOLD);
            Drawable drawable = binding.tvVegan.getCompoundDrawablesRelative()[0]; // 0 = iconita din stanga
            if (drawable != null) {
                drawable.setColorFilter(getResources().getColor(R.color.md_primary), PorterDuff.Mode.SRC_IN);
            }
        }
        if(RecipeStore.recipe.getVeryHealthy()){
            binding.tvHealthy.setTextColor(getResources().getColor(R.color.md_primary));
            binding.tvHealthy.setTypeface(binding.tvHealthy.getTypeface(), android.graphics.Typeface.BOLD);
            Drawable drawable = binding.tvHealthy.getCompoundDrawablesRelative()[0]; // 0 = iconita din stanga
            if (drawable != null) {
                drawable.setColorFilter(getResources().getColor(R.color.md_primary), PorterDuff.Mode.SRC_IN);
            }
        }
        if(RecipeStore.recipe.getGlutenFree()){
            binding.tvGluten.setTextColor(getResources().getColor(R.color.md_primary));
            binding.tvGluten.setTypeface(binding.tvGluten.getTypeface(), android.graphics.Typeface.BOLD);
            Drawable drawable = binding.tvGluten.getCompoundDrawablesRelative()[0]; // 0 = iconita din stanga
            if (drawable != null) {
                drawable.setColorFilter(getResources().getColor(R.color.md_primary), PorterDuff.Mode.SRC_IN);
            }
        }
        if(RecipeStore.recipe.getDairyFree()){
            binding.tvDairy.setTextColor(getResources().getColor(R.color.md_primary));
            binding.tvDairy.setTypeface(binding.tvDairy.getTypeface(), android.graphics.Typeface.BOLD);
            Drawable drawable = binding.tvDairy.getCompoundDrawablesRelative()[0]; // 0 = iconita din stanga
            if (drawable != null) {
                drawable.setColorFilter(getResources().getColor(R.color.md_primary), PorterDuff.Mode.SRC_IN);
            }
        }
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int recipeId = RecipeStore.recipe.getId();

                if (myDB.isFavoriteAdded(recipeId)) {
                    myDB.deleteFavorite(recipeId);
                    Toast.makeText(getContext(), "Eliminat din favorite", Toast.LENGTH_SHORT).show();
                } else {
                    // ID-ul de rand se genereaza din timp; il facem intotdeauna pozitiv,
                    // pentru ca (int) System.currentTimeMillis() poate iesi negativ
                    // dupa depasire si producea randuri cu chei ciudate.
                    int rowId = (int) (System.currentTimeMillis() & 0x7FFFFFFF);
                    if (myDB.insertFavoriteData(new FavoriteModel(rowId, recipeId))) {
                        Toast.makeText(getContext(), "Adaugat la favorite", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Nu am putut salva reteta.", Toast.LENGTH_SHORT).show();
                    }
                }

                // Citim din nou din baza de date, ca textul butonului sa reflecte
                // starea reala, nu ce presupunem noi ca s-a intamplat.
                refreshFavoriteButton();
            }
        });
        return view;
    }

    /** Pune pe buton textul potrivit, in functie de ce e in baza de date. */
    private void refreshFavoriteButton() {
        if (myDB == null || !RecipeStore.INSTANCE.isReady()) return;
        if (myDB.isFavoriteAdded(RecipeStore.recipe.getId())) {
            binding.btnAdd.setText(R.string.remove_from_favorites);
        } else {
            binding.btnAdd.setText(R.string.add_to_favorites);
        }
    }
}
