package com.numismatic_app.server.validator;

import com.numismatic_app.server.dto.CountryInfoDTO;

import java.util.ArrayList;

public class IncomingCountryInfoValidator {

    private IncomingCountryInfoValidator() {
        throw new IllegalStateException("Utility class");
    }
    public static CountryInfoDTO checkCountryInfoReq(CountryInfoDTO countryInfoDTO) {



        if(countryInfoDTO.getPeriods()==null){

            countryInfoDTO.setPeriods(new ArrayList<>());

        }
        return countryInfoDTO;

    }
}
