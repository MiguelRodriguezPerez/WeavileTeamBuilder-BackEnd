package com.example.demo.domain.movements;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.domain.PokemonData;
import com.example.demo.domain.PokemonType;
import com.example.demo.domain.team.PokemonTeamMember;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = "pokemon_list")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Entity
public class MoveData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private MoveType move_type;
    private PokemonType pokemon_type;

    private int accuracy;

    private String description;
    private int pp;

    @JsonBackReference
    @ManyToMany(mappedBy = "move_list", fetch = FetchType.EAGER)
    private Set<PokemonData> pokemon_list = new HashSet<>();

    @JsonBackReference
    @ManyToMany(mappedBy = "pkmn_team_move_list", fetch = FetchType.EAGER)
    private Set<PokemonTeamMember> pokemon_team_list = new HashSet<>();
    
}
