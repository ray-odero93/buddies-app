package com.guritchistudios.buudiesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class EditProfilePage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference reference;
    StorageReference storageReference;
    String storagePath = "Users_Profile_Cover_image/";
    String uid;
    ImageView set;
    TextView editName, editPass, profilePic;
    ProgressDialog dialog;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];
    Uri imageUri;
    String profileOrCoverPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);
    }
}