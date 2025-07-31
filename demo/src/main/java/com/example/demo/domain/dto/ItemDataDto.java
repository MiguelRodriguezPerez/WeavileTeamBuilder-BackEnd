package com.example.demo.domain.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemDataDto {

    @Lob // Indica que es un campo grande (BLOB)
    @Column(columnDefinition = "MEDIUMBLOB") // Para MySQL
    private byte[] image_sprite;

    private String name;
    private String description;
    
}
