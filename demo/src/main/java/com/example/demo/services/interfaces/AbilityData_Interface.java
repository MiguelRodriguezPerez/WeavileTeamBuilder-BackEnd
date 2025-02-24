package com.example.demo.services.interfaces;

import com.example.demo.domain.AbilityData;

public interface AbilityData_Interface {

    AbilityData saveAbility(AbilityData ab);

    void deleteAllAbilities();

    AbilityData getAbilityByName(String name);

    AbilityData requestAbilityToPokeApi(int number);

    boolean requestAllAbilities();

}
