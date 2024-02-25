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

    // New variables for edit mode
    private boolean isEditMode = false;
    private String editingContactId; // You might not need this depending on your implementation

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
                openImagePicker();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
            }
        });

        // Check if we're in edit mode
        checkForEditMode();
    }

    private void checkForEditMode() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isEditMode = extras.getBoolean("edit_mode", false);
            if (isEditMode) {
                editingContactId = extras.getString("contact_id"); // Only needed if you use a database ID
                nameEditText.setText(extras.getString("contact_name"));
                phoneEditText.setText(extras.getString("contact_phone"));
                addressEditText.setText(extras.getString("contact_address"));
                // If an image URI was passed, display the image
                if (extras.containsKey("contact_photo")) {
                    imageUri = Uri.parse(extras.getString("contact_photo"));
                    contactPhotoImageView.setImageURI(imageUri);
                    // Consider handling persistable URI permission if needed
                }
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: requestCode=" + requestCode);
        if (requestCode == PERMISSION_REQUEST_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Storage permission granted");
            openImagePicker();
        } else {
            Log.d(TAG, "Storage permission denied");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                contactPhotoImageView.setImageURI(imageUri);
                // Persist the permission to access the image URI
                final int takeFlags = data.getFlags()
                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
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
