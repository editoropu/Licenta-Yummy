package eu.tutorials.licenta_yummy.models;

public class FavoriteModel {
    private int ID;
    private int recipeId;

    public FavoriteModel() {}

    public FavoriteModel(int ID, int recipeId) {
        this.ID = ID;
        this.recipeId = recipeId;
    }

    public int getID() {
        return ID;
    }

    public int getRecipeId() {
        return recipeId;
    }
}
