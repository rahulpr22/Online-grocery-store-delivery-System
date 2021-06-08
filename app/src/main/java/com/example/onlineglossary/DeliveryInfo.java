package com.example.onlineglossary;

public class DeliveryInfo{
    String name,phone,email,place,street,zip,city,state,oid,status;

    public DeliveryInfo(String name, String phone, String email, String place, String street, String zip, String city, String state, String oid, String status) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.place = place;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.state = state;
        this.oid = oid;
        this.status = status;
    }

    public DeliveryInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
