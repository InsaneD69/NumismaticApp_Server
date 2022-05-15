package com.NumismaticApp.Server.NumismaticApp.UcoinParser;

public class liteCoin {


    private String year;
    private String valueAndCurrency;
    private String url;


    public String yearProperty() {
        return year;
    }

    public String urlProperty() {
        return url;
    }


    public String currencyProperty() {
        return valueAndCurrency;
    }

    public liteCoin(String years,String valueAndCurrency, String url) {

        this.year = years;
        this.valueAndCurrency=valueAndCurrency;
        this.url = url;

    }

    @Override
    public String toString(){
        return " значение: "+valueAndCurrency+
                " url: "+url+
                " год: "+ year;
    }

}
