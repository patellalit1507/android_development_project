package com.patelcodex.Supplimo;

public class cart_model {
    String url;
    String name;
    String price;
    String weight;
    String company;
    String item;
    String quant;
    String category;
    cart_model(){
    }
    public cart_model(String company,String item,String name,String price,String url,String quant,String weight,String category){
        this.company=company;
        this.item=item;
        this.name=name;
        this.price=price;
        this.url=url;
        this.quant=quant;
        this.weight=weight;
        this.category=category;

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }
}
