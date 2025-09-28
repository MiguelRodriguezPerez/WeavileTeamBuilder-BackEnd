package com.example.demo.dto;


import com.example.demo.domain.pokemon.PokemonType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MoveDto {

    private String name;
    private String move_type;
    private PokemonType pokemon_type;
    private int power;
    private int accuracy;
    private String description;
    private int pp;

}
