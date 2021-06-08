package com.example.onlineglossary;

public class productgrid {
    private String price, title, description, logo;
    private int pid, catid;

    public productgrid() {
    }

    public productgrid(String price, String title, String description, String logo, int pid, int catid) {
        this.price = price;
        this.title = title;
        this.description = description;
        this.logo = logo;
        this.pid = pid;
        this.catid = catid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }
}
