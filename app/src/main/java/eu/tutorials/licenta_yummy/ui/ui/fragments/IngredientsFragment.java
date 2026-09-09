package eu.tutorials.licenta_yummy.ui.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import eu.tutorials.licenta_yummy.R;
import eu.tutorials.licenta_yummy.databinding.FragmentOverviewBinding;
import eu.tutorials.licenta_yummy.models.ExtendedIngredient;
import eu.tutorials.licenta_yummy.ui.ui.RecipeStore;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientsFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_ingredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Spoonacular nu trimite intotdeauna lista de ingrediente. Daca lipseste,
        // campul este null si vechiul cod crapa cu NullPointerException.
        List<ExtendedIngredient> ingredients = null;
        if (RecipeStore.INSTANCE.isReady()) {
            ingredients = RecipeStore.recipe.getExtendedIngredients();
        }
        if (ingredients == null) {
            ingredients = new java.util.ArrayList<>();
        }

        ExtendedIngredientAdapter adapter = new ExtendedIngredientAdapter(getContext(), ingredients);
        recyclerView.setAdapter(adapter);

        return view;
    }
}

