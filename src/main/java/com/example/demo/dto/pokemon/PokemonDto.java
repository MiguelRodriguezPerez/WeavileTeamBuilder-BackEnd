package com.example.demo.dto.pokemon;

import java.util.Set;

import com.example.demo.domain.pokemon.PokemonType;
import com.example.demo.dto.MoveDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/* Necesitas esta clase para que tu proyecto sea un poco más fácil de entender, pero la razón real
de esta clase es porque la necesitas para el contexto del componente SelectedTeamMember y move_list
esta declarada con FetchType.EAGER, provocando que no se cargue con el resto de datos

La alternativa a esta clase sería llamar al getter de move_list
antes de devolverla al cliente y eso no es mantenible ni responsable */

@Getter
@Setter
@Builder
public class PokemonDto {

    private String name;

    private byte[] pc_sprite;
    private byte[] front_default_sprite;

    private int base_hp;
    private int base_attack;
    private int base_defense;
    private int base_special_attack;
    private int base_special_defense;
    private int base_speed;

    private Set<PokemonType> type_list;
    private Set<AbilityDto> ability_list;
    private Set<MoveDto> move_list;

}
