package com.isima.contacts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ContactDetailsActivity extends AppCompatActivity {

    private TextView contactName, contactPhoneNumber, contactAddress;
    private ImageView contactPhoto, callButton, editIcon, deleteIcon;
    private String name, phone, address, photoUriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        contactName = findViewById(R.id.contactName);
        contactPhoneNumber = findViewById(R.id.contactPhoneNumber);
        contactAddress = findViewById(R.id.contactAddress);
        contactPhoto = findViewById(R.id.contactPhoto);
        callButton = findViewById(R.id.callButton);
        editIcon = findViewById(R.id.editContact);
        deleteIcon = findViewById(R.id.deleteContact);

        // Initialize toolbar
        Toolbar toolbar = findViewById(R.id.toolbarContactDetails);
        setSupportActionBar(toolbar);

        // Enable the back arrow in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Retrieve and set contact data
        retrieveAndSetContactData();

        // Set OnClickListeners
        setOnClickListeners();
    }

    private void retrieveAndSetContactData() {
        photoUriString = getIntent().getStringExtra("contact_photo");
        name = getIntent().getStringExtra("contact_name");
        phone = getIntent().getStringExtra("contact_phone");
        address = getIntent().getStringExtra("contact_address");

        contactName.setText(name);
        contactPhoneNumber.setText(phone);
        contactAddress.setText(address);

        if (photoUriString != null && !photoUriString.isEmpty()) {
            Uri photoUri = Uri.parse(photoUriString);
            contactPhoto.setImageURI(photoUri);
        }
    }

    private void setOnClickListeners() {
        // Dial action
        callButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        });

        // Edit action
        editIcon.setOnClickListener(v -> {
            Intent intent = new Intent(ContactDetailsActivity.this, AddContactActivity.class);
            intent.putExtra("contact_name", name);
            intent.putExtra("contact_phone", phone);
            intent.putExtra("contact_address", address);
            intent.putExtra("contact_photo", photoUriString);
            intent.putExtra("edit_mode", true); // Indicate this is an edit operation
            startActivity(intent);
        });

        // Delete action (implementation needed based on your app's data handling)
        deleteIcon.setOnClickListener(v -> {
            // Handle delete operation
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
