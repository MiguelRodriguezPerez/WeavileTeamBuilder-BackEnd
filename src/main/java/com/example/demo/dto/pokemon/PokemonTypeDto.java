package com.example.demo.dto.pokemon;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
public class PokemonTypeDto {
    
    private Long id;

    private String name;

    @Lob // Indica que es un campo grande (BLOB)
    @Column(columnDefinition = "MEDIUMBLOB") // Para MySQL
    private byte[] sprite;
}
