package com.example.demo.domain;

import java.util.Set;

import com.example.demo.domain.movements.MoveData;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Entity
@ToString(exclude = "id")
public class PokemonData {
    
    @GeneratedValue
    @Id
    private Long id;

    private String name;

    private int base_hp;
    private int base_attack;
    private int base_defense;
    private int base_special_attack;
    private int base_special_defense;
    private int base_speed;


    private byte[] front_default_sprite;
    private byte[] pc_sprite;

    private Set<PokemonType> type_list;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "pokemonData-abilityData", 
        joinColumns = @JoinColumn(name = "pokemonData_id"), inverseJoinColumns = @JoinColumn(name = "abilityData_id"))
    private Set<AbilityData> ability_list;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "pokemonData-moveData",
        joinColumns = @JoinColumn(name = "pokemonData_id"), inverseJoinColumns = @JoinColumn(name = "moveData_id"))
    private Set<MoveData> move_list;

}
