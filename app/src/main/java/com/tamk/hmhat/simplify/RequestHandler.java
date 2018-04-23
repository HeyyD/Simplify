package com.tamk.hmhat.simplify;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Because the application has to do so many same kind of fetch requests, I decided to do a simple
 * class that does a GET request and builds the fetched data into a String.
 */

public class RequestHandler {

    private MainActivity host;

    /**
     * Save current MainActivity into an attribute
     * @param host Current MainActivity
     */
    public RequestHandler(MainActivity host){
        this.host = host;
    }

    /**
     * Does a GET request to chosen address and returns the fetched data in a String form
     * @param address url that data is going to be fetched from
     * @return fetched data as String
     */
    public String getMethod(String address){
        try{
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + host.getAccessToken());
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
                reader.close();

                return builder.toString();
            }

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }
}
