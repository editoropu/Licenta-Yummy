package eu.tutorials.licenta_yummy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import eu.tutorials.licenta_yummy.models.FavoriteModel;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "app_data.db";
    public static final String TABLE_FAVORITE = "favorite_table";
    public static final String TABLE_USERS = "users_table";

    // Versiunea bazei de date - crescuta de la 1 la 2 ca sa se creeze tabelul nou
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabelul de favorite (exista deja)
        db.execSQL("Create Table IF NOT EXISTS " + TABLE_FAVORITE + "(ID INTEGER PRIMARY KEY, recipeId INTEGER)");

        // Tabelul de utilizatori (NOU) - doar email si parola
        db.execSQL("Create Table IF NOT EXISTS " + TABLE_USERS +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE, " +
                "password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    // ========== METODE PENTRU UTILIZATORI ==========

    // Transforma parola in hash SHA-256 (ireversibil)
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Inregistreaza un utilizator nou. Returneaza true daca a reusit.
    public boolean registerUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", hashPassword(password)); // salvam parola criptata
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1; // -1 inseamna ca a esuat (ex: email deja folosit)
    }

    // Verifica daca exista un cont cu acest email
    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Verifica login: email + parola corecte. Returneaza true daca se potrivesc.
    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedInput = hashPassword(password); // criptam parola introdusa
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?",
                new String[]{email, hashedInput});
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    // ========== METODE PENTRU FAVORITE (existau deja) ==========

    public boolean insertFavoriteData(FavoriteModel modeClass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", modeClass.getID());
        contentValues.put("recipeId", modeClass.getRecipeId());
        long isInserted = db.insert(TABLE_FAVORITE, null, contentValues);
        return isInserted != -1;
    }

    public List<FavoriteModel> getAllFavoritesData() {
        List<FavoriteModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FAVORITE, null);
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                int id = res.getInt(0);
                int recipeId = res.getInt(1);
                FavoriteModel model = new FavoriteModel(id, recipeId);
                list.add(model);
            }
        }
        res.close();
        return list;
    }

    public boolean deleteFavorite(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FAVORITE + " where recipeId='" + recipeId + "'");
        return true;
    }

    public boolean isFavoriteAdded(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_FAVORITE + " where recipeId='" + recipeId + "'", null);
        boolean added = res.getCount() > 0;
        res.close();
        return added;
    }
}