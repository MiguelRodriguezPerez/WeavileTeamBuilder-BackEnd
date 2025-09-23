package com.example.demo.domain.pokemon;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.domain.movements.MoveData;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PokemonType {
    
    @GeneratedValue
    @Id
    private Long id;

    private String nombre;

    @JsonBackReference
    @ManyToMany(mappedBy = "pokemonData_pokemonType", fetch = FetchType.LAZY)
    private Set<PokemonData> pokemon_list = new HashSet<>();

    
    @OneToMany(
        mappedBy = "pokemonType_moveData", 
        cascade = CascadeType.ALL, 
        orphanRemoval = false
    )
    private Set<MoveData> move_list = new HashSet<>();
}
