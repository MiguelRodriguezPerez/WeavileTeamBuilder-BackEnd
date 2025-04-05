package com.example.demo.domain.pokemon;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;

/* Necesitas esta clase para que tu proyecto sea un poco más fácil de entender, pero la razón real
de esta clase es porque la necesitas para el contexto del componente SelectedTeamMember y move_list
esta declarada con FetchType.EAGER, provocando que no se cargue con el resto de datos

La alternativa a esta clase sería llamar al getter de move_list
antes de devolverla al cliente y eso no es mantenible ni responsable */

@Data
@AllArgsConstructor
public class PokemonDataDTO {
    
    private String name;

    private int base_hp;
    private int base_attack;
    private int base_defense;
    private int base_special_attack;
    private int base_special_defense;
    private int base_speed;

    private Set<String> types;
    private Set<String> abilities;
    private Set<String> moves;

     public PokemonDataDTO(PokemonData entity) {
        this.name = entity.getName();
        this.base_hp = entity.getBase_hp();
        this.base_attack = entity.getBase_attack();
        this.base_defense = entity.getBase_defense();
        this.base_special_attack = entity.getBase_special_attack();
        this.base_special_defense = entity.getBase_special_defense();
        this.base_speed = entity.getBase_speed();

        this.types = entity.getType_list().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        this.abilities = entity.getAbility_list().stream()
                .map(ability -> ability.getName())
                .collect(Collectors.toSet());

        this.moves = entity.getMove_list().stream()
                .map(move -> move.getName()) 
                .collect(Collectors.toSet());
    }

}
