package com.eder.rodriguez.minersup;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImagePostActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 100;
    private Uri uri;

    private StorageReference storage;
    private DatabaseReference databaseReference;

    private Button doneButton;
    private Button cancelButton;
    private ImageButton addImage;
    private EditText titleImage;
    private EditText descriptionImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_post);

        storage = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("minersup");

        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this::doneButtonClicked);

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this::cancelButtonClicked);

        addImage = (ImageButton) findViewById(R.id.addImageButton);
        addImage.setOnClickListener(this::addImageClicked);

        titleImage = (EditText) findViewById(R.id.titleText);
        descriptionImage = (EditText) findViewById(R.id.descriptionText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            addImage.setImageURI(uri);
        }
    }

    protected void doneButtonClicked(View view) {
        final String imageTitle = titleImage.getText().toString().trim();
        final String imageDescription = descriptionImage.getText().toString().trim();
        if (!TextUtils.isEmpty(imageTitle) && !TextUtils.isEmpty(imageDescription)) {
            StorageReference path = storage.child("imagepost").child(uri.getLastPathSegment());
            path.putFile(uri).addOnSuccessListener((takeSnapshot) -> {
                Uri download = takeSnapshot.getDownloadUrl();
                DatabaseReference post = databaseReference.push();
                post.child("title").setValue(imageTitle);
                post.child("description").setValue(imageDescription);
                post.child("image").setValue(download.toString());
                Toast.makeText(this, "Upload Completed", Toast.LENGTH_LONG).show();
            });
        }
    }

    protected void addImageClicked(View view) {
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, GALLERY_REQUEST_CODE);
    }

    protected void cancelButtonClicked(View view) {
        finish();
    }
}