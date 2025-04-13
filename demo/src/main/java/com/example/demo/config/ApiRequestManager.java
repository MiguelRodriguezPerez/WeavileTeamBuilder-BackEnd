package com.example.demo.config;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ApiRequestManager {
    
    public static JsonNode callGetRequest(String api_url) {
        try {
            URL url = new URI(api_url).toURL();
            HttpURLConnection solicitud = (HttpURLConnection) url.openConnection();
            solicitud.setRequestMethod("GET");

            if(solicitud.getResponseCode() == 200) {
                String resultado_peticion = new String(url.openStream().readAllBytes());
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode resultado_final = objectMapper.readTree(resultado_peticion);

                return resultado_final;
            }
            
            else return null;
        }
        catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
