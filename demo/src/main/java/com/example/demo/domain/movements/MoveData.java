package com.example.demo.domain.movements;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.domain.pokemon.PokemonData;
import com.example.demo.domain.pokemon.PokemonType;
import com.example.demo.domain.team.PokemonTeamMember;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = "pokemon_list")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Entity
public class MoveData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private MoveType move_type;

    @Enumerated(EnumType.STRING)
    private PokemonType pokemon_type;

    private int power;

    private int accuracy;

    private String description;
    private int pp;

    /*
     * Aunque mappedBy lleva el mismo valor por nombre de campo, ese campo
     * es en dos entidades distintas así que no tendrás problemas
     */

    @JsonBackReference
    @ManyToMany(mappedBy = "move_list", fetch = FetchType.EAGER)
    private Set<PokemonData> pokemon_list = new HashSet<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "move_list", fetch = FetchType.EAGER)
    private Set<PokemonTeamMember> pokemon_team_list = new HashSet<>();

}
