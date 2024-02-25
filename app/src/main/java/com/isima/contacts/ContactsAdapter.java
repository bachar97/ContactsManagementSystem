package com.isima.contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Contact> contactList;
    private List<Contact> contactListFiltered;

    public ContactsAdapter(List<Contact> contactList) {
        this.contactList = contactList;
        this.contactListFiltered = new ArrayList<>(contactList);
    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        Contact contact = contactListFiltered.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactPhoneNumber.setText(contact.getPhoneNumber());
        // Set click listener if needed
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactPhoneNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhoneNumber = itemView.findViewById(R.id.contactPhoneNumber);
        }
    }

    public void filter(String query) {
        if (query.isEmpty()) {
            contactListFiltered = new ArrayList<>(contactList);
        } else {
            List<Contact> filteredList = contactList.stream()
                    .filter(contact -> contact.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            contactListFiltered = new ArrayList<>(filteredList);
        }
        notifyDataSetChanged();
    }

    public void addContact(Contact contact) {
        contactList.add(contact);
        notifyDataSetChanged(); // Notify the adapter to refresh the list
    }
}
