package com.example.demo.domain;

import java.util.Set;

import com.example.demo.domain.movements.MoveData;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Entity
public class PokemonData {
    
    @GeneratedValue
    @Id
    private Long id;

    private String name;

    private Short base_hp;
    private Short base_attack;
    private Short base_defense;
    private Short base_special_attack;
    private Short base_special_defense;
    private Short base_speed;

    private Set<PokemonType> type_list;

    private Set<AbilityData> ability_list;
    private Set<MoveData> move_list;

    private byte[] front_default_sprite;
    private byte[] pc_sprite;
}
