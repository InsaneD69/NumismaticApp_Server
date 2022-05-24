package com.NumismaticApp.Server.NumismaticApp.repository.Model;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryPeriod;

import java.io.Serializable;
import java.net.URISyntaxException;

public interface CountryPeriodInterface  {

    CountryPeriodInterface toModel(CountryPeriod period) throws URISyntaxException;

}
