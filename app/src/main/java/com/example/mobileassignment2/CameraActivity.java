package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {


    private Button takePictureButton;
    private ImageView imageView;
    Uri file;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatchTakePictureIntent();



    }



    /* Create a file for a taken image */
    private File createImageFile() throws IOException {
        /* Use a public external picture directory; otherwise, all data gets lost once we uninstall the app */
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "my_images");
//
//        if (!mediaStorageDir.exists()){
//            if (!mediaStorageDir.mkdirs()){
//                return null; // make the pictures directory 'Healogy' if it does not exist
//            }
//        }


        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /* Intent to take a picture with the default camera app */

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.mobileassignment2.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
        finish();
    }




//
//
//    /* Request camera and storage permissions, and enable the camera button once we obtain them */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//
//        // proper request
//        if (requestCode == 0) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//                // both sets of permissions enabled
//                takePictureButton.setEnabled(true);
//            }
//        }
//    }
//
//
//    /* Create a File reference that we will use to save the image data */
//    private static File getOutputMediaFile() {
//
//        /* Use a public external picture directory; otherwise, all data gets lost once we uninstall the app */
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "Healogy");
//
//        if (!mediaStorageDir.exists()){
//            if (!mediaStorageDir.mkdirs()){
//                return null; // make the pictures directory 'Healogy' if it does not exist
//            }
//        }
//
//        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // name images by their date and time
//
//        return new File(mediaStorageDir.getPath() + File.separator +
//                "IMG_"+ time + ".jpg");
//
//    }
//
//
//    public void takePicture(View view){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        file = Uri.fromFile(getOutputMediaFile()); // extract File URI
////        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
////
////        startActivityForResult(intent, 100);
//
//        try {
//            startActivityForResult(intent, 100);
//        } catch (ActivityNotFoundException e) {
//            // display error state to the user
//        }
//
//
//
//    }
//
//    /* Populate the file with the member variable URI with the image data */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 100){
//            if (resultCode == RESULT_OK) {
//                imageView.setImageURI(file);
//
//                // TODO: Analyze image with Azure
//            }
//        }
//
//    }
    

}
