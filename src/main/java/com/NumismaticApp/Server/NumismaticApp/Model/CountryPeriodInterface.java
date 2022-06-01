package com.NumismaticApp.Server.NumismaticApp.Model;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryPeriod;

import java.net.URISyntaxException;

public interface CountryPeriodInterface  {

    CountryPeriodInterface toModel(CountryPeriod period) throws URISyntaxException;

}
