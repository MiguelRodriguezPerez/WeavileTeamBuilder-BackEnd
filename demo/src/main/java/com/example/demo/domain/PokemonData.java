package com.example.demo.domain;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.domain.movements.MoveData;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Entity
@ToString(exclude = { "front_default_sprite", "pc_sprite", "ability_list", "move_list" })
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

    @Lob // Indica que es un campo grande (BLOB)
    @Column(columnDefinition = "MEDIUMBLOB") // Para MySQL
    private byte[] front_default_sprite;

    @Lob // Indica que es un campo grande (BLOB)
    @Column(columnDefinition = "MEDIUMBLOB") // Para MySQL
    private byte[] pc_sprite;

    private Set<PokemonType> type_list = new HashSet<>();

    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
        name = "pokemonData-abilityData", 
        joinColumns = @JoinColumn(name = "pokemonData_id"), 
        inverseJoinColumns = @JoinColumn(name = "abilityData_id")
    )
    private Set<AbilityData> ability_list = new HashSet<>();

    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
        name = "pokemonData-moveData", 
        joinColumns = @JoinColumn(name = "pokemonData_id"), 
        inverseJoinColumns = @JoinColumn(name = "moveData_id")
    )
    private Set<MoveData> move_list = new HashSet<>();

}
