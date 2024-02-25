package com.isima.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileDescriptor;
import java.io.IOException;

import android.view.Menu;

public class ContactDetailsActivity extends AppCompatActivity {

    private TextView contactName, contactPhoneNumber, contactAddress;
    private ImageView contactPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_details);

        contactName = findViewById(R.id.contactName);
        contactPhoneNumber = findViewById(R.id.contactPhoneNumber);
        contactAddress = findViewById(R.id.contactAddress);
        contactPhoto = findViewById(R.id.contactPhoto); // Reference to the ImageView

        // Retrieve data from intent
        String photoUriString = getIntent().getStringExtra("contact_photo");
        String name = getIntent().getStringExtra("contact_name");
        String phone = getIntent().getStringExtra("contact_phone");
        String address = getIntent().getStringExtra("contact_address");

        // Set the text fields
        contactName.setText(name);
        contactPhoneNumber.setText(phone);
        contactAddress.setText(address);

        // Load the photo from URI and set it to the ImageView
        if (photoUriString != null && !photoUriString.isEmpty()) {
            Uri photoUri = Uri.parse(photoUriString);
            try {
                Bitmap bitmap = getBitmapFromUri(photoUri);
                contactPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e("ContactDetailsActivity", "Error loading image", e);
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

        options.inSampleSize = calculateInSampleSize(options, 120, 120);

        options.inJustDecodeBounds = false;
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        parcelFileDescriptor.close();
        return image;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            // Start AddContactActivity with the intent filled with contact details for editing
            Intent intent = new Intent(this, AddContactActivity.class);
            intent.putExtra("edit_mode", true);
            intent.putExtra("contact_name", contactName.getText().toString());
            intent.putExtra("contact_phone", contactPhoneNumber.getText().toString());
            intent.putExtra("contact_address", contactAddress.getText().toString());

            // Assuming you're storing the image URI as a String or have a way to retrieve it
            // If you saved the URI in imageUri variable when setting the ImageView, use it here
            // intent.putExtra("contact_photo", imageUri.toString());

            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        // Implement other cases if necessary
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_details_menu, menu);
        return true;
    }
}
