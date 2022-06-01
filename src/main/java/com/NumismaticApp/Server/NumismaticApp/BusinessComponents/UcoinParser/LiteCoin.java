package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;

import java.io.Serializable;

public class LiteCoin implements Serializable {


    private Integer year;
    private String valueAndCurrency;
    private String url;

    public Integer getYear() {
        return year;
    }

    public LiteCoin() {
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getValueAndCurrency() {
        return valueAndCurrency;
    }

    public void setValueAndCurrency(String valueAndCurrency) {
        this.valueAndCurrency = valueAndCurrency;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LiteCoin(Integer year, String valueAndCurrency, String url) {

        this.year = year;
        this.valueAndCurrency=valueAndCurrency;
        this.url = url;

    }

    @Override
    public String toString(){
        return " значение: "+valueAndCurrency+
                " url: "+url+
                " год: "+ year;
    }

    @Override
    public int hashCode() {
        return url.hashCode()+year.hashCode();
    }

}
