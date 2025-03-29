package com.example.demo.domain;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.domain.movements.MoveData;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
/* Las compatiblidades de tipos que las gestione el cliente, porque tener varios
ManyToMany que se referencian a si mismos es difícil de realizar, mantener y 
vivirás con el peligro constante de recursión */

public class PokemonType {
    
    @GeneratedValue
    @Id
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "pokemon_type", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<MoveData> move_list = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "pokemon_type-pokemon_data",
        joinColumns = @JoinColumn(name = "pokemonType_id"),
        inverseJoinColumns = @JoinColumn(name = "pokemonData_id")
    )
    @JsonBackReference
    private Set<PokemonData> pokemonData_list = new HashSet<>();

}
