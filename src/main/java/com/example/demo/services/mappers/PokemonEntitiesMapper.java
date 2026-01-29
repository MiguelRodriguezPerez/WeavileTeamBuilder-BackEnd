package com.example.demo.services.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.domain.AbilityData;
import com.example.demo.domain.movements.MoveData;
import com.example.demo.domain.pokemon.PokemonData;
import com.example.demo.domain.pokemon.PokemonType;
import com.example.demo.dto.MoveDto;
import com.example.demo.dto.pokemon.AbilityDto;
import com.example.demo.dto.pokemon.PokemonDataDto;
import com.example.demo.dto.pokemon.PokemonTypeDto;

@Component
public class PokemonEntitiesMapper {

    public PokemonDataDto mapPokemonDataToDto(PokemonData data) {
        return PokemonDataDto.builder()
                .id(data.getId())
                .name(data.getName())
                .front_default_sprite(data.getFront_default_sprite())
                .pc_sprite(data.getPc_sprite())
                .base_hp(data.getBase_hp())
                .base_attack(data.getBase_attack())
                .base_defense(data.getBase_defense())
                .base_special_attack(data.getBase_special_attack())
                .base_special_defense(data.getBase_special_defense())
                .base_speed(data.getBase_speed())
                .type_list(data.getType_list())
                .ability_list(
                        data.getAbility_list().stream()
                                .map(ability -> {
                                    return this.mapAbilityEntityToDto(ability);
                                }).collect(Collectors.toSet()))
                .move_list(
                        data.getMove_list().stream()
                                .map(move -> {
                                    return this.mapMoveEntityToDto(move);
                                }).collect(Collectors.toSet()))
                .build();
    }

    public AbilityDto mapAbilityEntityToDto (AbilityData abilityData) {
        return AbilityDto.builder()
                .name(abilityData.getName())
                .description(abilityData.getDescription())
                .build();
    }

    public MoveDto mapMoveEntityToDto (MoveData moveData) {
        return MoveDto.builder()
            .id(moveData.getId())
            .name(moveData.getName())
            .move_type(moveData.getMove_type())
            .power(moveData.getPower())
            .pp(moveData.getPp())
            .accuracy(moveData.getAccuracy())
            .description(moveData.getDescription())
            .pokemon_type(
                this.mapPokemonTypeEntityToDto(
                    moveData.getPokemon_type()
                )
            )
            .build();
    }
    
    public PokemonTypeDto mapPokemonTypeEntityToDto (PokemonType type) {
        return PokemonTypeDto.builder()
            .id(type.getId())
            .name(type.getName())
            .sprite(type.getSprite())
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
