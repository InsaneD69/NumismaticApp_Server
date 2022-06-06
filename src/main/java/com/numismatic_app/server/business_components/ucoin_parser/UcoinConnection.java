package com.numismatic_app.server.business_components.ucoin_parser;


import com.numismatic_app.server.exception.SiteConnectionError;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;
@Log4j2
public class UcoinConnection {

    private UcoinConnection() {
        throw new IllegalStateException("Utility class");
    }

    private static Semaphore semaphore = new Semaphore(1);

    public static Document getUcoinPage(String url) throws SiteConnectionError, IOException {

        try {
            semaphore.acquire();

            Connection.Response connection = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.143 YaBrowser/22.5.0.1814 Yowser/2.5 Safari/537.36")
                    .method(Connection.Method.GET)
                    .execute();

            if (connection.statusCode() != 200) {

                throw new SiteConnectionError("failed connect to" + url);

            }
            log.info("Successful connect to Ucoin: "+url);
            return connection.parse();

        }
        catch (UnknownHostException e){

            throw new SiteConnectionError("failed connect to Ucoin:");

        }  catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            throw new SiteConnectionError("failed connect to Ucoin:");

        } finally {
            semaphore.release();
        }




    }




}
