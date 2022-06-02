package com.NumismaticApp.Server.NumismaticApp.BusinessComponents.UcoinParser;


import com.NumismaticApp.Server.NumismaticApp.Exception.SiteConnectionError;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;
@Log4j2
public class UcoinConnection {


    private static Semaphore semaphore = new Semaphore(1);

    public static Document getUcoinPage(String url) throws SiteConnectionError {

        try {
            semaphore.acquire();

            Connection.Response connection = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.143 YaBrowser/22.5.0.1814 Yowser/2.5 Safari/537.36")
                    .method(Connection.Method.GET)
                    .execute();

            if (connection.statusCode() != 200) {

                throw new SiteConnectionError("failed connect to" + url);

            }
            return connection.parse();

        }
        catch (UnknownHostException e){
            log.error("Failed connect to url:"+url);
            throw new SiteConnectionError("failed connect to Ucoin:");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }




    }




}
