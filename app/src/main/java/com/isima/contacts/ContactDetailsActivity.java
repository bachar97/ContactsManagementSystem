package com.isima.contacts;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
        contactPhoto = findViewById(R.id.contactPhoto); // Assuming this is your ImageView for the photo
        callButton = findViewById(R.id.callButton);

        // Retrieve and set contact data from intent
        String photoUriString = getIntent().getStringExtra("contact_photo");
        String name = getIntent().getStringExtra("contact_name");
        String phone = getIntent().getStringExtra("contact_phone");
        String address = getIntent().getStringExtra("contact_address");

        contactName.setText(name);
        contactPhoneNumber.setText(phone);
        contactAddress.setText(address);

        // Use the provided snippet here to load the photo
        if (photoUriString != null && !photoUriString.isEmpty()) {
            Uri photoUri = Uri.parse(photoUriString);
            try {
                InputStream inputStream = getContentResolver().openInputStream(photoUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                contactPhoto.setImageBitmap(bitmap);
                inputStream.close(); // Don't forget to close the InputStream
            } catch (FileNotFoundException e) {
                Log.e(TAG, "File not found", e);
            } catch (SecurityException e) {
                Log.e(TAG, "Security Exception: Do not have permission to access this file", e);
            } catch (IOException e) {
                Log.e(TAG, "IOException: Error closing input stream", e);
            }
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
                onBackPressed(); // Go back when the back arrow is clicked
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the back arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
