package com.isima.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

    private boolean isEditMode = false;
    private String originalPhoneNumber = null; // To hold the original phone number if in edit mode

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageUri = uri;
                    contactPhotoImageView.setImageURI(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        initializeViews();

        uploadPhotoButton.setOnClickListener(view -> mGetContent.launch("image/*"));

        isEditMode = getIntent().getBooleanExtra("edit_mode", false);
        if (isEditMode) {
            populateExistingContactDetails();
        }

        saveButton.setOnClickListener(view -> {
            if (isEditMode) {
                saveEditedContact();
            } else {
                saveNewContact();
            }
        });
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        addressEditText = findViewById(R.id.editTextAddress);
        contactPhotoImageView = findViewById(R.id.contact_photo);
        saveButton = findViewById(R.id.buttonSave);
        uploadPhotoButton = findViewById(R.id.button_upload_photo);
    }

    private void populateExistingContactDetails() {
        nameEditText.setText(getIntent().getStringExtra("contact_name"));
        originalPhoneNumber = getIntent().getStringExtra("contact_phone");
        phoneEditText.setText(originalPhoneNumber);
        addressEditText.setText(getIntent().getStringExtra("contact_address"));
        String photoUriStr = getIntent().getStringExtra("contact_photo");
        if (photoUriStr != null && !photoUriStr.isEmpty()) {
            imageUri = Uri.parse(photoUriStr);
            contactPhotoImageView.setImageURI(imageUri);
        }
    }

    private void saveNewContact() {
        Intent replyIntent = new Intent();
        if (!packageContactDetailsIntoIntent(replyIntent, false)) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    private void saveEditedContact() {
        Intent replyIntent = new Intent();
        replyIntent.putExtra("original_phone", originalPhoneNumber); // Include original phone number to identify the contact
        if (!packageContactDetailsIntoIntent(replyIntent, true)) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    private boolean packageContactDetailsIntoIntent(Intent intent, boolean isEditMode) {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        if (name.isEmpty() || phone.isEmpty()) {
            return false;
        }
        intent.putExtra("contact_name", name);
        intent.putExtra("contact_phone", phone);
        intent.putExtra("contact_address", address);
        if (imageUri != null) {
            intent.putExtra("contact_photo", imageUri.toString());
        }
        intent.putExtra("edit_mode", isEditMode);
        return true;
    }
}
