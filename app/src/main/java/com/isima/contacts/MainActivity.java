package com.isima.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.SearchView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.ContactClickListener {

    private RecyclerView recyclerViewContacts;
    private FloatingActionButton fabAddContact;
    private SearchView searchView;
    private ContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        fabAddContact = findViewById(R.id.fabAddContact);
        searchView = findViewById(R.id.searchView);

        // Initialize the contacts list
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("John Doe", "1234567890", "123 Main St", "./res/drawable/photo.jpg"));
        contacts.add(new Contact("Jane Smith", "0987654321", "456 Elm St", "./res/drawable/photo.jpg"));
        // More dummy contacts can be added here

        // Initialize the adapter with the list of contacts and 'this' as the click listener
        adapter = new ContactsAdapter(contacts, this);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewContacts.setAdapter(adapter);

        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivityForResult(intent, 1); // Use a request code of your choice
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("contact_name");
            String phone = data.getStringExtra("contact_phone");
            String address = data.getStringExtra("contact_address");
            String photo = data.getStringExtra("contact_photo");
            Contact newContact = new Contact(name, phone, address, photo);
            adapter.addContact(newContact);
        }
    }

    @Override
    public void onContactClick(Contact contact) {
        Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
        intent.putExtra("contact_name", contact.getName());
        intent.putExtra("contact_phone", contact.getPhoneNumber());
        intent.putExtra("contact_address", contact.getAddress());
        intent.putExtra("contact_photo", contact.getPhotoUri());
        startActivity(intent);
    }
}
