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
 * Created by hmhat on 7.3.2018.
 */

public class RequestHandler {

    private MainActivity host;
    private String address;
    private String request;

    public RequestHandler (MainActivity host){
        this.host = host;
    }

    public String makeRequest(String address, String request){
        this.address = address;
        this.request = request;
        return doInBackground();
    }

    private String doInBackground() {

        try{
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request);
            connection.setRequestProperty("Authorization", "Bearer " + host.getAccessToken());
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null){
                    builder.append(line + "\n");
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
