package eu.tutorials.licenta_yummy.ui.ui;

import static eu.tutorials.licenta_yummy.util.Constants.API_KEY;
import static eu.tutorials.licenta_yummy.util.Constants.BASE_URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class SpoonacularApiClient {


    private OkHttpClient client;

    public SpoonacularApiClient() {
        client = new OkHttpClient();
    }

    public String getAnalyzedInstructions(int recipeId) throws IOException {
        String url = BASE_URL + "/recipes/" + recipeId + "/analyzedInstructions?apiKey=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();
        }
    }
}
