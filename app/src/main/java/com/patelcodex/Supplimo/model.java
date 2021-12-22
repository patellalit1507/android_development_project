package com.patelcodex.Supplimo;

import com.google.firebase.storage.StorageReference;

public class model {
    String category;
    String company;
    String name;
    String p1;
    String w1;
    String url;
    String id;;
    model(){
    }

    public model(String category, String company, String name, String p1, String w1, String url, String id) {
        this.category = category;
        this.company = company;
        this.name = name;
        this.p1 = p1;
        this.w1=w1;
        this.url = url;
        this.id=id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getW1() {
        return w1;
    }

    public void setW1(String w1) {
        this.w1 = w1;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
