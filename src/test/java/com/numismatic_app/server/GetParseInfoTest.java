package com.numismatic_app.server;

import com.numismatic_app.server.file_worker.PropertyConnection;
import com.numismatic_app.server.business_components.ucoin_parser.objects.CountryInformation;
import com.numismatic_app.server.file_worker.GetterInfo;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class GetParseInfoTest {

    @Test
    void getList() throws IOException, ClassNotFoundException {

        PropertyConnection propertyConnection = new PropertyConnection(new File("").getAbsolutePath()+"/src/main/resources/ucoin.properties");

        GetterInfo getParseInfo = new GetterInfo(propertyConnection.open().getProperty("countriesInfo")+"Япония_ru.txt");

       CountryInformation object =(CountryInformation)getParseInfo.get();

       object.getPeriods().forEach((period)->{

           System.out.println(period);
       });

    }



}
