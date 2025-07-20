package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class ApiController {
    public JSONObject getJSON(String url1) throws Exception{
        URL url = new URL(url1);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();//открываем соединение по ссылке

        conn.setRequestMethod("GET"); // обозначаем вид запроса get получить инфу

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); // создаём читалку ответа на запрос через поток

        StringBuilder responce = new StringBuilder();
        String line;

        while((line = reader.readLine()) !=null){
            responce.append(line);
        }

        reader.close();

        JSONObject recipe = new JSONObject(responce.toString());

        JSONArray array = recipe.getJSONArray("meals");

        JSONObject object = array.getJSONObject(0);

        return object;
    }
    
}
