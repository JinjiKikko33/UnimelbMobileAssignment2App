package com.example.mobileassignment2;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionClient;
import com.microsoft.azure.cognitiveservices.vision.computervision.ComputerVisionManager;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.Category;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageAnalysis;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.ImageTag;
import com.microsoft.azure.cognitiveservices.vision.computervision.models.VisualFeatureTypes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* Network operations must be done in the background. Call an intent service to connect to
        Azure Computer vision in the background
   */
public class ClassifyImageService extends IntentService {


    static final String PARAM_FILE = "file";
    HashSet<String> categories;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    public ClassifyImageService() {
        super("ClassifyImageService");
    }


    @Override
    protected void onHandleIntent(Intent workIntent) {
        String file = workIntent.getStringExtra(ClassifyImageService.PARAM_FILE);
        try {
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            analyzeImage(file);

        } catch (IOException e) {
            Log.e("IOERROR", e.toString());
        }
    }

    private void analyzeImage(String file) throws IOException {
        String endpoint = getString(R.string.azure_cognitive_services_endpoint);
        String key = getString(R.string.azure_cognitive_services_key);

        ComputerVisionClient compVisClient = ComputerVisionManager.authenticate(key).withEndpoint(endpoint);

        // analyse the image defined in current photopath
        File rawImage = new File(file);
        byte[] imageByteArray = Files.readAllBytes(rawImage.toPath());


        List<VisualFeatureTypes> featuresToExtractFromLocalImage = new ArrayList<>();
        featuresToExtractFromLocalImage.add(VisualFeatureTypes.DESCRIPTION);
        featuresToExtractFromLocalImage.add(VisualFeatureTypes.CATEGORIES);
        featuresToExtractFromLocalImage.add(VisualFeatureTypes.TAGS);
        featuresToExtractFromLocalImage.add(VisualFeatureTypes.COLOR);
        featuresToExtractFromLocalImage.add(VisualFeatureTypes.IMAGE_TYPE);

        ImageAnalysis analysis = compVisClient.computerVision().analyzeImageInStream().withImage(imageByteArray).withVisualFeatures(featuresToExtractFromLocalImage).execute();


        categories = new HashSet<>();

        // add high confidence classes to an array list
        for (ImageTag tag : analysis.tags()) {
            System.out.printf("\'%s\' with confidence %f\n", tag.name(), tag.confidence());
            categories.add(tag.name());
        }


        String url = "http://" + getString(R.string.host_name) + "/food/categories";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("Response: ", response);

                        ArrayList<FoodCategoryResultSet> dbResultList = new ArrayList<>();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            Iterator keys = jsonObject.keys();

                            while (keys.hasNext()) {
                                Object key = keys.next();
                                JSONObject value = jsonObject.getJSONObject((String) key);

                                int score = value.getInt("score");
                                int id = value.getInt("id");


                                FoodCategoryResultSet dbResult = new FoodCategoryResultSet((String) key, id, score);
                                dbResultList.add(dbResult);
                            }



                            /* Once we have a list of food categories from the database, calculate the
                                highest scoring food category that was classified with confidence by
                                computer vision
                             */


                            String[] classifiedFood = getMaxClassifiedFoodLabelAndScore(categories, dbResultList);
                            String maxFood = classifiedFood[0];
                            String maxScore = classifiedFood[1];


                            // Notify the user how we have classified their food, and how many points they have earned
                            Toast toast;

                            if (maxFood.equals("None")) {
                                toast = Toast.makeText(getApplicationContext(), "Could not classify the food in your picture", Toast.LENGTH_LONG);
                                toast.show();

                            } else {
                                // TODO: Update the user's score in the database
                                Log.d("making backend request: ", maxFood);
                                updateUsersFoodLog(maxFood, maxScore);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Could not reach the server", Toast.LENGTH_LONG);
            }
        });

        queue.add(stringRequest);


    }

    /*
     * make an API call to update the user's food log once we have a food description, and a score
     * a call to update the food log will generate a new row in the food_log table, with an food category id
     * matching the food description the user provided
     *
     * a call to update the food log wil also update the user's daily score
     *
     */

    private void updateUsersFoodLog(final String food, final String score) {

        String email = currentUser.getEmail();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + getString(R.string.host_name)
                + String.format("/users/add-new-food-log-entry?item=%s&score=%s&email=%s", food, score, email);


        Log.d("request url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String userMessage = String.format("Classified food: %s \n You earned %s points!", food, score);
                        Toast toast = Toast.makeText(getApplicationContext(), userMessage, Toast.LENGTH_LONG);
                        toast.show();
                        //Log.d("Backend response: ", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("Backend response error: ", error.toString());

            }
        });

        queue.add(stringRequest);

    }

    /*
     * return the max scoring food label in a set of classified food labels, corresponding to the classifications for
     * an image of food the user takes with their device
     * */
    private String[] getMaxClassifiedFoodLabelAndScore(HashSet<String> classifiedCategories, ArrayList<FoodCategoryResultSet> foodCategories) {
        String classifiedFoodLabel = "None";
        int classifiedScore = -100;

        String s = Arrays.toString(categories.toArray());

        for (FoodCategoryResultSet category : foodCategories) {
            String description = category.getDescription();

            if (classifiedCategories.contains(description)) {
                if (category.getScore() > classifiedScore) {
                    // new max scoring item
                    classifiedFoodLabel = description;
                    classifiedScore = category.getScore();
                }
            }

        }


        return new String[]{classifiedFoodLabel, Integer.toString(classifiedScore)};


    }


    /*
     * A data class to store the food item's labels, database ID, and score, when we retrieve food
     * categories from the database.
     *
     */
    public class FoodCategoryResultSet {
        String description;
        int id;
        int score;

        public FoodCategoryResultSet(String description, int id, int score) {
            this.description = description;
            this.id = id;
            this.score = score;
        }

        public String getDescription() {
            return description;
        }

        public int getId() {
            return id;
        }

        public int getScore() {
            return score;
        }
    }

}