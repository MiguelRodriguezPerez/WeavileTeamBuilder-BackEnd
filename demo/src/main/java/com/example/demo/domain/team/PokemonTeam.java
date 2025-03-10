package com.example.demo.domain.team;

import java.util.Set;

import com.example.demo.domain.AbilityData;
import com.example.demo.domain.ItemData;
import com.example.demo.domain.PokemonType;
import com.example.demo.domain.movements.MoveData;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

/*Esta entidad representa un posible pokemón en un equipo */

@Entity
@NoArgsConstructor
@Data
public class PokemonTeam {
    
    @GeneratedValue
    @Id
    private Long id;
    
    private String name;

    private int base_hp;
    private int base_attack;
    private int base_defense;
    private int base_special_attack;
    private int base_special_defense;
    private int base_speed;

    private int hp_ev;
    private int attack_ev;
    private int defense_ev;
    private int special_attack_ev;
    private int special_defense_ev;
    private int speed_ev;

    private int hp_iv;
    private int attack_iv;
    private int defense_iv;
    private int special_attack_iv;
    private int special_defense_iv;
    private int speed_iv;

    /* Teóricamente  @ElementCollection y @Embedded 
    sirven para cuando no necesitas que dicha entidad se 
    almacene en otra tabla */

    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
        name = "pokemonTeam_moveData",
        joinColumns = @JoinColumn(name = "pokemonTeam_id"),
        inverseJoinColumns = @JoinColumn(name = "moveData_id")
    )
    private Set<MoveData> pkmn_team_move_list;

    @ManyToOne
    @JoinColumn(name = "ability_id")
    private AbilityData ability;

    @Embedded
    private ItemData item;

    @ElementCollection
    private Set<PokemonType> type_list;

    @Embedded
    private PokemonType tera_type;
    
}
