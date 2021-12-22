package com.patelcodex.Supplimo;

public class company_model {
    String co_url;
    String company;
    company_model(){}
    public company_model(String co_url,String company){
        this.co_url=co_url;
        this.company=company;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCo_url() {
        return co_url;
    }

    public void setCo_url(String co_url) {
        this.co_url = co_url;
    }
}
