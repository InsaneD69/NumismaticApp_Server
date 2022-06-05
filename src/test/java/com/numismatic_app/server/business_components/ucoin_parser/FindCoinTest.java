package com.numismatic_app.server.business_components.ucoin_parser;

import com.numismatic_app.server.file_worker.PropertyConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static com.numismatic_app.server.business_components.ucoin_parser.CoinSearcher.pathToUcoinProperty;

class FindCoinTest {


    @Test
    void dasad() throws IOException {

        int year = 2008;
        String url="/coin/russia-2-rubles-2002-2009/?tid=2016";

        PropertyConnection property = new PropertyConnection(pathToUcoinProperty);

        Document doc = Jsoup.connect(property.open().getProperty("link."+"en")+url).get();
        property.close();
        Element table=doc.select("table").get(3);  //таблица

        Elements  tableCoins = table.getElementsByAttributeValue("class","tr-hr")
                .stream()
                .filter(coin->
                        Integer.compare(Integer.parseInt(coin.getElementsByTag("strong").text()),year)==0
                )
                .collect(Collectors.toCollection(Elements::new));
        ArrayList<String> s = new ArrayList<>();
        tableCoins.forEach(coin->{

          String svfd = coin.attr("data-href");
          s.add(svfd);

          System.out.println(svfd);
        });



        PropertyConnection property1 = new PropertyConnection(pathToUcoinProperty);

        Document docx = Jsoup.connect(property1.open().getProperty("link."+"en")+s.get(0)).get();

        property1.close();


        //Properties property2 = new Properties();
       // FileOutputStream fileOutputStream = new FileOutputStream(pathToUcoinProperty);
       // property2.setProperty("fjgfgjh","dfbdfgh");

    // property2.storeToXML(fileOutputStream,"set language", Charset.forName("UTF-8"));




        System.out.println("----------------------------");
        Element ment=docx.select("table").get(0);
        System.out.println(0+"----------------------------");
        System.out.println(ment);
        Element ement=docx.select("table").get(1);
        System.out.println(1+"----------------------------");
        System.out.println(ement);
        Element lement=docx.select("table").get(2);
        System.out.println(2+"----------------------------");
        System.out.println(lement);
        Element tableq=docx.select("table").get(3);
        System.out.println(3+"----------------------------");
        System.out.println(tableq);



    }

}