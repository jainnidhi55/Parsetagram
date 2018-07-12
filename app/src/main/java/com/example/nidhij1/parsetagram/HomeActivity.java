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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nidhij1.parsetagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private EditText descriptionInput;
    private Button createButton;
    private Button refreshButton;
    public String TAG = "HOMEACT";
    public Button logOutBut;


    private static final String imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20180709_160522.jpg";

    public final String APP_TAG = "PARSETAGRAM";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "/storage/emulated/0/DCIM/Camera/IMG_20180709_160522.jpg";
    File photoFile;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    public File img;

    public final static int PICK_PHOTO_CODE = 1046;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //toolbar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(
                getResources().getDimensionPixelSize(R.dimen.action_bar_elevation)
        );
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites:
                                final Intent intent_but = new Intent (HomeActivity.this, FeedActivity.class);
                                startActivity(intent_but);
                                finish();


                            case R.id.action_schedules:

                            case R.id.action_music:

                        }
                        return true;
                    }
                });


        descriptionInput = findViewById(R.id.et_descrip);
        createButton = findViewById(R.id.but_create);
        refreshButton = findViewById(R.id.but_refresh2);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = descriptionInput.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();

                if (isStoragePermissionGranted()) {
                    final File file = new File(imagePath);
                    ParseFile parseFile = new ParseFile(file);
                    //createPost(description, parseFile, user);
                    onLaunchCamera();
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPosts();
            }
        });

        loadTopPosts();

        //logout
        logOutBut = (Button) findViewById(R.id.logoutbut);
        logOutBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                final Intent intent = new Intent (HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);

        //launch the camera
 //       onLaunchCamera();


        newPost.setImage(imageFile);
        newPost.setUser(user);


        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("HomeActivity", "create post success");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e==null){
                    for (int i = 0; i < objects.size(); i++) {
                        Log.d("HomeActivity", "Post[" + i + "]" + "\nusername " + objects.get(i).getUser().getUsername());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                // by this point we have the camera photo on disk
                Bitmap takenImage = (Bitmap) extras.get("data");
                //Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below


                //converting bitmap to file attempt 1
                /*File file = new File(imagePath);
                try {
                    OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                    takenImage.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.close();
                } catch (Exception e) {}*/


                //converting bitmap to file attempt 2
                //saveBitmap(takenImage, "/");

                //attempt 3
                ParseFile parseFile = conversionBitmapParseFile(takenImage);

                //creating new post
                final ParseUser user = ParseUser.getCurrentUser();
                if (isStoragePermissionGranted()) {
                    //ParseFile parseFile = new ParseFile(file);
                    final String description = descriptionInput.getText().toString();
                    createPost(description, parseFile, user);
                    // Load the taken image into a preview
                    ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                    ivPreview.setImageBitmap(takenImage);
                }
                EditText descrip = (EditText) findViewById(R.id.et_descrip);
                descrip.setText("");


            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (data != null && isStoragePermissionGranted()) {
                try {
                    Uri photoUri = data.getData();
                    // Do something with the photo based on Uri
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    // Load the selected image into a preview
                    ImageView ivPreview = (ImageView) findViewById(R.id.ivPreview);
                    ivPreview.setImageBitmap(selectedImage);
                    final ParseUser user = ParseUser.getCurrentUser();
                    final String description = descriptionInput.getText().toString();
                    ParseFile parseFile = conversionBitmapParseFile(selectedImage);
                    createPost(description, parseFile, user);

                } catch (Exception e) {

                }
            }
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

    private File saveBitmap(Bitmap bitmap, String path) {
        File file = null;
        if (bitmap != null) {
            file = new File(path);
            try {
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(path); //here is set your file path where you want to save or also here you can set file object directly

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // bitmap is your Bitmap instance, if you want to compress it you can compress reduce percentage
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public ParseFile conversionBitmapParseFile(Bitmap imageBitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        ParseFile parseFile = new ParseFile("image_file.png",imageByte);
        return parseFile;
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }



}
