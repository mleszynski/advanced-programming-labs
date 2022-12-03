package com.mleszynski.fmc;

import android.util.Log;
import java.net.*;
import java.io.*;
import request.*;
import result.*;
import encodeDecode.*;

public class ServerProxy {

    public ServerProxy() {

    }

    public RegisterResult register(URL url, RegisterRequest registerRequest) {
        RegisterResult result = new RegisterResult();

        try {
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.addRequestProperty("Accept", "application/json");
            conn.connect();
            String json = Encoder.encode(registerRequest);
            OutputStream os = conn.getOutputStream();
            IOHandler.writeToString(json, os);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream requestBody = conn.getInputStream();
                String requestData = IOHandler.readFromString(requestBody);
                result = Decoder.decode(requestData, RegisterResult.class);
                conn.getInputStream().close();
                return result;
            }
        } catch (Exception ex) {
            Log.e("HttpClient", ex.getMessage(), ex);
        }
        result.setMessage("Error: Failed to register in ServerProxy");
        result.setSuccess(false);
        return result;
    }

    public LoginResult login(URL url, LoginRequest loginRequest) {
        LoginResult result = new LoginResult();

        try {
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.addRequestProperty("Accept", "application/json");
            conn.connect();
            String json = Encoder.encode(loginRequest);
            OutputStream os = conn.getOutputStream();
            IOHandler.writeToString(json, os);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream requestBody = conn.getInputStream();
                String requestData = IOHandler.readFromString(requestBody);
                result = Decoder.decode(requestData, LoginResult.class);
                conn.getInputStream().close();
                return result;
            }
        } catch (Exception ex) {
            Log.e("HttpClient", ex.getMessage(), ex);
        }
        result.setMessage("Error: Failed to login in ServerProxy");
        result.setSuccess(false);
        return result;
    }

    public AllPersonResult persons (URL url, String authtoken) {
        AllPersonResult result = new AllPersonResult();

        try {
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.addRequestProperty("Accept", "application/json");
            conn.addRequestProperty("Authorization", authtoken);
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream requestBody = conn.getInputStream();
                String requestData = IOHandler.readFromString(requestBody);
                result = Decoder.decode(requestData, AllPersonResult.class);
                conn.getInputStream().close();
                return result;
            }
        } catch (Exception ex) {
            Log.e("HttpClient", ex.getMessage(), ex);
        }
        result.setMessage("Error: Failed to get persons in ServerProxy");
        result.setSuccess(false);
        return result;
    }

    public AllEventResult events(URL url, String authtoken) {
        AllEventResult result = new AllEventResult();

        try {
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(false);
            conn.addRequestProperty("Accept", "application/json");
            conn.addRequestProperty("Authorization", authtoken);
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream requestBody = conn.getInputStream();
                String requestData = IOHandler.readFromString(requestBody);
                result = Decoder.decode(requestData, AllEventResult.class);
                conn.getInputStream().close();
                return result;
            }
        } catch (Exception ex) {
            Log.e("HttpClient", ex.getMessage(), ex);
        }
        result.setMessage("Error: Failed to get events in ServerProxy");
        result.setSuccess(false);
        return result;
    }
}
