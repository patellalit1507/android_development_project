package com.patelcodex.Supplimo;

public class orders_model {
    String date;
    String name;
    String status;
    String code;
    String ordtotal;
    String orderid;
    orders_model(){

    }
    public orders_model(String date,String name,String status,String code,String ordtotal,String orederid){
        this.date=date;
        this.name=name;
        this.status=status;
        this.code=code;
        this.ordtotal=ordtotal;
        this.orderid=orederid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrdtotal() {
        return ordtotal;
    }

    public void setOrdtotal(String ordtotal) {
        this.ordtotal = ordtotal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
