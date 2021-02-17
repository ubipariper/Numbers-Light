package com.ubiparip.numbersLight;

import android.app.DownloadManager;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class DataCrawler extends Thread {
    String dataPointer = "http://dev.tapptic.com/test/json.php";

    String getData() {
        StringBuilder data = new StringBuilder();
        Thread thread = new Thread() {
            public void run() {

                try {
                    java.net.URL url = new URL(dataPointer);

                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String str;
                    while ((str = in.readLine()) != null) {
                        data.append(str);
                    }
                    in.close();
                } catch (
                        MalformedURLException e) {
                    e.printStackTrace();
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    ArrayList<DataContainer> packData(String rawData){
       return new ArrayList<>(Arrays.asList(new Gson().fromJson(rawData, DataContainer[].class)));
    }

    DetailedDataContainer packDetailedData(String rawData){
        return new Gson().fromJson(rawData, DetailedDataContainer.class);
    }

    String downloadDetailedImage(String name){
        StringBuilder data = new StringBuilder();
        Thread thread = new Thread() {
            public void run() {

                try {
                    java.net.URL url = new URL("http://dev.tapptic.com/test/json.php?name=".concat(name));

                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String str;
                    while ((str = in.readLine()) != null) {
                        data.append(str);
                    }
                    in.close();
                } catch (
                        MalformedURLException e) {
                } catch (
                        IOException e) {
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return data.toString();
    }
}
