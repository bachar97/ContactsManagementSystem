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

public class MainActivity extends AppCompatActivity {

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
        // Dummy data for testing
        contacts.add(new Contact("John Doe", "1234567890", "123 Main St"));
        contacts.add(new Contact("Jane Smith", "0987654321", "456 Elm St"));
        // More dummy contacts can be added here

        // Create the adapter with the list of contacts
        adapter = new ContactsAdapter(contacts, new ContactsAdapter.ContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                Intent intent = new Intent(MainActivity.this, ContactDetailsActivity.class);
                intent.putExtra("contact_name", contact.getName());
                intent.putExtra("contact_phone", contact.getPhoneNumber());
                // Add other contact details as needed
                startActivity(intent);
            }
        });
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewContacts.setAdapter(adapter);

        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to open AddContactActivity
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(intent);
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
}
