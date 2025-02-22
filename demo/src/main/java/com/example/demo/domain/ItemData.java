package com.example.demo.domain;

import com.example.demo.config.ImageDownloader;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class ItemData {

    @Id
    @GeneratedValue
    private Long id;

    @Lob // Indica que es un campo grande (BLOB)
    @Column(columnDefinition = "MEDIUMBLOB") // Para MySQL
    private byte[] image_sprite;

    private String name;
    private String description;

    
    public void setImage_sprite(String url_arg){
        this.image_sprite = ImageDownloader.getImage(url_arg);
    }

    public void setImage_sprite(byte[] image_sprite) {
        this.image_sprite = image_sprite;
    }

}