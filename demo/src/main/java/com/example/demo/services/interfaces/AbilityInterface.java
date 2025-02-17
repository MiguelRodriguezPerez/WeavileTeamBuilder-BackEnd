package com.example.demo.services.interfaces;

import com.example.demo.domain.abilities.AbilityDTO;
import com.example.demo.domain.abilities.AbilityData;

public interface AbilityInterface {
    
    AbilityData saveAbility(AbilityData ab);
    AbilityData findAbilityByName(String name);
    AbilityData requestAbilityToPokeApi(int number);
    

}
