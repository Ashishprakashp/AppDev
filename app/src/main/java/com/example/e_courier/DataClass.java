package com.example.e_courier;

public class DataClass {
    private String name;
    private String address;
    private String mobile;
    private String mail;

    private String imageUrl;
    public DataClass() {
        // Default constructor required for Firebase
    }

    public DataClass(String name, String address, String mobile, String mail,String imageUrl) {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.mail = mail;
        this.imageUrl=imageUrl;
    }

    // Getter and setter methods for each field
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getImageUrl(){return imageUrl;}
    public void setImageUrl(String imageUrl){this.imageUrl=imageUrl;}
}
