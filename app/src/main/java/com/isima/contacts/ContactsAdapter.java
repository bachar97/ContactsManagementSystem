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
    private ContactClickListener listener;

    public ContactsAdapter(List<Contact> contactList, ContactClickListener listener) {
        this.contactList = contactList;
        this.contactListFiltered = new ArrayList<>(contactList);
        this.listener = listener;
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
        holder.bind(contact, listener);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactPhoneNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contactName);
            contactPhoneNumber = itemView.findViewById(R.id.contactPhoneNumber);
        }

        public void bind(final Contact contact, final ContactClickListener listener) {
            contactName.setText(contact.getName());
            contactPhoneNumber.setText(contact.getPhoneNumber());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Use contact directly, no need to access contactListFiltered
                    if (listener != null) {
                        listener.onContactClick(contact);
                    }
                }
            });
        }
    }

    public void filter(String query) {
        if (query.isEmpty()) {
            contactListFiltered = new ArrayList<>(contactList);
        } else {
            contactListFiltered = contactList.stream()
                    .filter(contact -> contact.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
        notifyDataSetChanged();
    }

    public void addContact(Contact contact) {
        contactList.add(contact);
        notifyDataSetChanged();
    }

    public interface ContactClickListener {
        void onContactClick(Contact contact);
    }

    public void setContacts(List<Contact> contacts) {
        this.contactList = contacts;
        this.contactListFiltered = new ArrayList<>(contacts);
        notifyDataSetChanged();
    }

}
