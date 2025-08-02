package com.example.demo.dto.pokemon;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.domain.pokemon.PokemonType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MissignoGridDto {

    private Long id;
    private String name;

    private int base_hp;
    private int base_attack;
    private int base_defense;
    private int base_special_attack;
    private int base_special_defense;
    private int base_speed;

    private byte[] pc_sprite;

    // Sospechoso de fallar
    private Set<PokemonType> type_list = new HashSet<>();
    private Set<AbilityDto> ability_list = new HashSet<>();

}
