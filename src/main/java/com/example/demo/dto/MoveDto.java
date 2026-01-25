package com.example.demo.dto;


import com.example.demo.domain.movements.MoveType;
import com.example.demo.dto.pokemon.PokemonTypeDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MoveDto {

    private Long id;
    private String name;
    private MoveType move_type;
    private PokemonTypeDto pokemon_type;
    private int power;
    private int accuracy;
    private String description;
    private int pp;

}
