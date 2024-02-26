package com.isima.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText;
    private ImageView contactPhotoImageView;
    private Button saveButton, uploadPhotoButton;
    private Uri imageUri;

    private static final String TAG = "AddContactActivity";

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new androidx.activity.result.ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        imageUri = uri;
                        contactPhotoImageView.setImageURI(uri);
                    }
                }
            });

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

        uploadPhotoButton.setOnClickListener(view -> {
            mGetContent.launch("image/*");
        });

        boolean isEditMode = getIntent().getBooleanExtra("edit_mode", false);
        if (isEditMode) {
            // Populate the fields with existing contact details
            nameEditText.setText(getIntent().getStringExtra("contact_name"));
            phoneEditText.setText(getIntent().getStringExtra("contact_phone"));
            addressEditText.setText(getIntent().getStringExtra("contact_address"));
            String photoUri = getIntent().getStringExtra("contact_photo");
            if (photoUri != null && !photoUri.isEmpty()) {
                contactPhotoImageView.setImageURI(Uri.parse(photoUri));
            }
        }

        saveButton.setOnClickListener(view -> {
            if (isEditMode) {
                // Handle saving edited contact
                saveEditedContact();
            } else {
                // Handle saving new contact
                saveContact();
            }
        });
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
            if (imageUri != null) {
                replyIntent.putExtra("contact_photo", imageUri.toString());
            }
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    private void saveEditedContact() {
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
            if (imageUri != null) {
                replyIntent.putExtra("contact_photo", imageUri.toString());
            }
            replyIntent.putExtra("edit_mode", true);
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

}
