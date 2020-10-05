package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.util.Date;

public class CameraActivity extends AppCompatActivity {


    private Button takePictureButton;
    private ImageView imageView;
    Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        takePictureButton = (Button) findViewById(R.id.button_image);
        imageView = (ImageView) findViewById(R.id.imageview);


        /* Check if we have storage and camera permissions. If not, disable the button until we do */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }



    }


    /* Request camera and storage permissions, and enable the camera button once we obtain them */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        // proper request
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                // both sets of permissions enabled
                takePictureButton.setEnabled(true);
            }
        }
    }


    /* Create a File reference that we will use to save the image data */
    private static File getOutputMediaFile() {

        /* Use a public external picture directory; otherwise, all data gets lost once we uninstall the app */
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Healogy");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null; // make the pictures directory 'Healogy' if it does not exist
            }
        }

        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); // name images by their date and time

        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ time + ".jpg");

    }

    
    public void takePicture(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        file = Uri.fromFile(getOutputMediaFile()); // extract File URI
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
//
//        startActivityForResult(intent, 100);

        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }



    }

    /* Populate the file with the member variable URI with the image data */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100){
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(file);

                // TODO: Analyze image with Azure
            }
        }

    }
    
}
