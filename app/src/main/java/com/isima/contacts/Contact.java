package com.isima.contacts;

public class Contact {
    private String name;
    private String phoneNumber;
    private String address;
    private String photoUri;

    public Contact(String name, String phoneNumber, String address, String photoUri) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
