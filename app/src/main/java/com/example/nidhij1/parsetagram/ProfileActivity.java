package com.example.nidhij1.parsetagram;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

public class ProfileActivity extends AppCompatActivity {

    public final static int PICK_PROF_CODE = 99;
    public Bitmap takenImage;
    public ParseFile parseFile;
    public ParseUser user;
    String TAG = "PROFILEACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //toolbar
//        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setElevation(
//                getResources().getDimensionPixelSize(R.dimen.action_bar_elevation)
//        );
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites:
                                final Intent intent_but9 = new Intent (ProfileActivity.this, FeedActivity.class);
                                startActivity(intent_but9);
                                finish();


                            case R.id.action_schedules:

                            case R.id.action_music:

                        }
                        return true;
                    }
                });

    }

    // Trigger gallery selection for a photo
    public void onPickProf(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent,PICK_PROF_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PROF_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null && isStoragePermissionGranted()) {
                    try {
                        Uri photoUri = data.getData();
                        // Do something with the photo based on Uri
                        takenImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                        // Load the selected image into a preview
                        ImageView ivPreview = (ImageView) findViewById(R.id.iv_bigprof);
                        ivPreview.setImageBitmap(takenImage);
                        user = ParseUser.getCurrentUser();
                        //final String description = descriptionInput.getText().toString();
                        parseFile = conversionBitmapParseFile(takenImage);
                        //createPost(description, parseFile, user);
                        user.put("profPic", parseFile);
                        user.saveInBackground();

                    } catch (Exception e) {

                    }
                }

            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public ParseFile conversionBitmapParseFile(Bitmap imageBitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        ParseFile parseFile = new ParseFile("image_file.png",imageByte);
        return parseFile;
    }
}
