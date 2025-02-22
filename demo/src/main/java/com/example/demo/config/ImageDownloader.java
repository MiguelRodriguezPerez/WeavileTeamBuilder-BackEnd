package com.example.demo.config;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.stereotype.Component;

/* El objetivo de esta clase es abstraer todo el boilerplate que se necesitaría para descargar 
una imagen de la url del json que luego se usaría en item.setImage() */

// TODO: Comentar clase
@Component
public class ImageDownloader {
    
    public static byte[] getImage(String url_arg){

        try {
            URL url = new URI(url_arg).toURL();
            InputStream inputStream = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int number = 0;
            while (-1!=(number=inputStream.read(buf))) {
                byteArrayOutputStream.write(buf, 0, number);
            }
            
            byteArrayOutputStream.close();
            inputStream.close();
            byte[] response = byteArrayOutputStream.toByteArray();

            return response;
        } 
        catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
