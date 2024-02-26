package com.isima.contacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AddContactActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText;
    private ImageView contactPhotoImageView;
    private Button saveButton, uploadPhotoButton;
    private Uri imageUri; // URI of the selected image

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private static final String TAG = "AddContactActivity"; // Tag for log messages

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        addressEditText = findViewById(R.id.editTextAddress);
        contactPhotoImageView = findViewById(R.id.contact_photo);
        saveButton = findViewById(R.id.buttonSave);
        uploadPhotoButton = findViewById(R.id.button_upload_photo);

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Upload button clicked");
                openFileChooser();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
            }
        });
    }

    private void openFileChooser() {
        Log.d(TAG, "Attempting to open file chooser");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Requesting storage permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: requestCode=" + requestCode);
        if (requestCode == PERMISSION_REQUEST_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Storage permission granted");
            openFileChooser();
        } else {
            Log.d(TAG, "Storage permission denied");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();

                // Attempt to take the persistable URI permission grant
                try {
                    int takeFlags = data.getFlags();
                    takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    // Check for the freshest data.
                    getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
                } catch (SecurityException e) {
                    // Handle the exception here
                    Log.e(TAG, "Error taking persistable URI permission", e);
                }

                contactPhotoImageView.setImageURI(imageUri);
            }
        }
    }


    private void saveContact() {
        Intent replyIntent = new Intent();
        if (nameEditText.getText().toString().trim().isEmpty() || phoneEditText.getText().toString().trim().isEmpty()) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            String name = nameEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String address = addressEditText.getText().toString();
            replyIntent.putExtra("contact_name", name);
            replyIntent.putExtra("contact_phone", phone);
            replyIntent.putExtra("contact_address", address);
            // Include the photo URI if available
            if (imageUri != null) {
                replyIntent.putExtra("contact_photo", imageUri.toString());
            }
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }
}
