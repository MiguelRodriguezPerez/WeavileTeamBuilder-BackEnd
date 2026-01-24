package com.example.demo.services.mappers;

import org.springframework.stereotype.Component;

import com.example.demo.domain.AbilityData;
import com.example.demo.domain.pokemon.PokemonType;
import com.example.demo.dto.pokemon.AbilityDto;
import com.example.demo.dto.pokemon.PokemonTypeDto;

@Component
public class PokemonEntitiesMapper {
    
    public PokemonTypeDto mapPokemonTypeEntityToDto (PokemonType type) {
        return PokemonTypeDto.builder()
            .id(type.getId())
            .name(type.getName())
            .sprite(type.getSprite())
            .build();
    }

    public AbilityDto mapAbilityEntityToDto (AbilityData abilityData) {
        return AbilityDto.builder()
                .name(abilityData.getName())
                .description(abilityData.getDescription())
                .build();
    }

    /* No se si al obtener el id recreará la entidad, de momento usa los mappers solo para convertir 
    entidades a dto */
    // public PokemonType mapPokemonTypeDtoToEntity (PokemonTypeDto dto) {
    //     return PokemonType.builder()
    //             .id(dto.getId())
    //             .name(dto.getName())
    //             .sprite(dto.getSprite())
    //             .build();
    // }
}
