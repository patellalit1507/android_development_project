package com.patelcodex.Supplimo;

public class order_detail_model {
    String id;
    String price;
    String quant;
    String weight;
    order_detail_model(){
    }

    public order_detail_model(String id,String price,String quant,String weight){
        this.id=id;
        this.price=price;
        this.quant=quant;
        this.weight=weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
