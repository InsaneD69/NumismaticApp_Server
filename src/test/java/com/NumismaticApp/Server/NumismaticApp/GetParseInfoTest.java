package com.NumismaticApp.Server.NumismaticApp;

import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.PropertyConnection;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.CountryInformation;
import com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser.GetParseInfo;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class GetParseInfoTest {

    @Test
    void getList() throws IOException, ClassNotFoundException {

        PropertyConnection propertyConnection = new PropertyConnection(new File("").getAbsolutePath()+"/src/main/resources/ucoin.properties");

        GetParseInfo getParseInfo = new GetParseInfo(propertyConnection.open().getProperty("countriesInfo")+"Япония_ru.txt");

       CountryInformation object =(CountryInformation)getParseInfo.get();

       object.getPeriods().forEach((period)->{

           System.out.println(period);
       });

    }



}
