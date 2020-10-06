package com.example.mobileassignment2;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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
import java.util.List;

/* Network operations must be done in the background. Call an intent service to connect to
        Azure Computer vision in the background
     */
public class ClassifyImageService extends IntentService {


    static final String PARAM_FILE = "file";

    public ClassifyImageService(){
        super("ClassifyImageService");
    }


    @Override
    protected  void onHandleIntent(Intent workIntent){
        String file = workIntent.getStringExtra(ClassifyImageService.PARAM_FILE);
        try {
            analyzeImage(file);

        } catch (IOException e){
            Log.e("IOERROR", e.toString());
        }
    }

    private void analyzeImage(String file) throws IOException{
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
        featuresToExtractFromLocalImage.add(VisualFeatureTypes.FACES);
        featuresToExtractFromLocalImage.add(VisualFeatureTypes.ADULT);
        featuresToExtractFromLocalImage.add(VisualFeatureTypes.COLOR);
        featuresToExtractFromLocalImage.add(VisualFeatureTypes.IMAGE_TYPE);

        ImageAnalysis analysis = compVisClient.computerVision().analyzeImageInStream().withImage(imageByteArray).withVisualFeatures(featuresToExtractFromLocalImage).execute();


        System.out.println("\nCategories: ");
        for (Category category : analysis.categories()) {
            System.out.printf("\'%s\' with confidence %f\n", category.name(), category.score());
        }
        System.out.println("\nTags: ");
        for (ImageTag tag : analysis.tags()) {
            System.out.printf("\'%s\' with confidence %f\n", tag.name(), tag.confidence());
        }

    }
}