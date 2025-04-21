package com.example.demo.domain.pokemon;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.domain.AbilityData;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "name")
public class MissignoGridDTO {

    private Long id;
    private String name;

    private int base_hp;
    private int base_attack;
    private int base_defense;
    private int base_special_attack;
    private int base_special_defense;
    private int base_speed;

    private byte[] pc_sprite;

    private Set<PokemonType> type_list = new HashSet<>();
    private Set<AbilityData> ability_list = new HashSet<>();
    
}
