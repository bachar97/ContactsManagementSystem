package com.isima.contacts;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonFileUtility {

    private static final String FILE_NAME = "contacts.json";

    public static List<Contact> loadContacts(Context context) {
        List<Contact> contacts = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (!file.exists()) {
                return contacts; // Return an empty list if the file doesn't exist
            }
            FileInputStream fis = new FileInputStream(file);
            StringBuilder builder = new StringBuilder();
            int character;
            while ((character = fis.read()) != -1) {
                builder.append((char) character);
            }
            fis.close();
            JSONArray jsonArray = new JSONArray(builder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Contact contact = new Contact(
                        jsonObject.getString("photoUri"),
                        jsonObject.getString("name"),
                        jsonObject.getString("phoneNumber"),
                        jsonObject.getString("address"));
                contacts.add(contact);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public static void saveContacts(Context context, List<Contact> contacts) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Contact contact : contacts) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", contact.getName());
                jsonObject.put("phoneNumber", contact.getPhoneNumber());
                jsonObject.put("address", contact.getAddress());
                jsonArray.put(jsonObject);
            }
            File file = new File(context.getFilesDir(), FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file, false);
            fos.write(jsonArray.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
