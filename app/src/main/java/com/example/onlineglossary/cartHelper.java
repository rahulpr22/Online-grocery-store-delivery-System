package com.example.onlineglossary;

public class cartHelper {
    String uid,item,price,quantity,logo;
    int pid;

    public cartHelper() {
    }

    public cartHelper(String uid, int pid, String item, String price, String quantity, String logo) {
        this.uid = uid;
        this.item = item;
        this.price = price;
        this.quantity = quantity;
        this.logo = logo;
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}

