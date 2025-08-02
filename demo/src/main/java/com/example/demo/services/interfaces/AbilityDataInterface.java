package com.example.demo.services.interfaces;

import java.util.Set;

import com.example.demo.domain.AbilityData;
import com.example.demo.dto.pokemon.AbilityDto;

public interface AbilityDataInterface {

    AbilityData saveAbility(AbilityData ab);
    void deleteAllAbilities();
    AbilityData getAbilityByName(String name);
    AbilityData requestAbilityToPokeApi(int number);
    boolean requestAllAbilitiesToApi();
    Set<AbilityDto> getAllAbilityDto();
    AbilityDto convertAbilityEntityToDto(AbilityData ab);

}
