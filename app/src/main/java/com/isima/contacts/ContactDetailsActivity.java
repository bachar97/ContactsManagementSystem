package com.isima.contacts;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class ContactDetailsActivity extends AppCompatActivity {

    private TextView contactName, contactPhoneNumber, contactAddress;
    private ImageView contactPhoto, callButton;
    private String name, phone, address, photoUriString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        contactName = findViewById(R.id.contactName);
        contactPhoneNumber = findViewById(R.id.contactPhoneNumber);
        contactAddress = findViewById(R.id.contactAddress);
        contactPhoto = findViewById(R.id.contactPhoto); // ImageView for the contact photo
        callButton = findViewById(R.id.callButton);

        // Retrieve contact data from intent
        String photoUriString = getIntent().getStringExtra("contact_photo");
        String name = getIntent().getStringExtra("contact_name");
        String phone = getIntent().getStringExtra("contact_phone");
        String address = getIntent().getStringExtra("contact_address");

        contactName.setText(name);
        contactPhoneNumber.setText(phone);
        contactAddress.setText(address);

        // Load the contact photo if URI string is not null or empty
        if (photoUriString != null && !photoUriString.isEmpty()) {
            Uri photoUri = Uri.parse(photoUriString);
            contactPhoto.setImageURI(photoUri);
        }

        // Dial action
        callButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        });

        // Enable the back arrow in the ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); // Ensure the home icon is displayed
        }

        // Set click listener for the back arrow
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarContactDetails);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbarContactDetails) {
            Intent intent = new Intent(ContactDetailsActivity.this, AddContactActivity.class);
            intent.putExtra("contact_name", name);
            intent.putExtra("contact_phone", phone);
            intent.putExtra("contact_address", address);
            intent.putExtra("contact_photo", photoUriString);
            intent.putExtra("edit_mode", true); // Indicate this is an edit operation
            startActivity(intent);
            Log.d(TAG, "Editing contact: " + name);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
