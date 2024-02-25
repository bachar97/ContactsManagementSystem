package com.isima.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddContactActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        nameEditText = findViewById(R.id.editTextName);
        phoneEditText = findViewById(R.id.editTextPhone);
        addressEditText = findViewById(R.id.editTextAddress);
        saveButton = findViewById(R.id.buttonSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }
}