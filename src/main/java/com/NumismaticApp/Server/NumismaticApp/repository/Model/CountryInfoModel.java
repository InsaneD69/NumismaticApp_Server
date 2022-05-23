package com.NumismaticApp.Server.NumismaticApp.repository.Model;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryInformation;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryPeriod;

import java.io.Serializable;
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

        countryInformation.periods.forEach((countryPeriod -> {

             if(countryPeriod.getCurrencies()==null){

                 periodsModel.add(CountryPeriodDemoModel.getModel(countryPeriod));

           }else{

                 periodsModel.add(CountryPeriodModel.getModel(countryPeriod));

             }

        }));


        countryInfoModel.setPeriodsModel(periodsModel);
        return  countryInfoModel;

    }


}
