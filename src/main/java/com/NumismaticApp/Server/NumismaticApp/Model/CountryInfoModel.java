package com.NumismaticApp.Server.NumismaticApp.Model;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryInformation;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class CountryInfoModel implements Serializable {

    public ArrayList<CountryPeriodInterface> periodsModel;


    public ArrayList<CountryPeriodInterface> getPeriodsModel() {
        return periodsModel;
    }

    public void setPeriodsModel(ArrayList<CountryPeriodInterface> periodsModel) {
        this.periodsModel = periodsModel;
    }

    public static CountryInfoModel toModel(CountryInformation countryInformation){

        CountryInfoModel countryInfoModel = new CountryInfoModel();
        ArrayList<CountryPeriodInterface> periodsModel=new ArrayList<>();

        countryInformation.getPeriods().forEach((countryPeriod -> {

             if(countryPeriod.getCurrencies()==null){

                 periodsModel.add(CountryPeriodDemoModel.getModel(countryPeriod));

           }else{

                 try {
                     periodsModel.add(CountryPeriodModel.getModel(countryPeriod));
                 } catch (URISyntaxException e) {
                     throw new RuntimeException(e);
                 }

             }

        }));


        countryInfoModel.setPeriodsModel(periodsModel);
        return  countryInfoModel;

    }


}
