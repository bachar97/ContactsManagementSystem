package com.example.contactsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.isima.contacts.R;

public class ContactDetailsActivity extends AppCompatActivity {

    private TextView contactName, contactPhoneNumber, contactAddress;
    private ImageView contactPhoto, callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        contactName = findViewById(R.id.contactName);
        contactPhoneNumber = findViewById(R.id.contactPhoneNumber);
        contactAddress = findViewById(R.id.contactAddress);
        contactPhoto = findViewById(R.id.contactPhoto);
        callButton = findViewById(R.id.callButton);

        // Retrieve contact data from intent
        // Set the contact data to views

        callButton.setOnClickListener(view -> {
            // Intent to call the contact's phone number
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
