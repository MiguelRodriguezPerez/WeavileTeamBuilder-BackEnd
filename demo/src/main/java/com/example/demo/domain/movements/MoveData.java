package com.example.demo.domain.movements;


import java.util.Set;

import com.example.demo.domain.PokemonData;
import com.example.demo.domain.PokemonType;

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

    // TODO: Calc max pp by multiplying api request * 160%;
    private int pp;

    @ManyToMany(mappedBy = "move_list", fetch = FetchType.EAGER)
    private Set<PokemonData> pokemon_list;


    public MoveData(String name, MoveType move_Type, byte accuracy, String description, int pp) {
        this.name = name;
        this.move_type = move_Type;
        this.accuracy = accuracy;
        this.description = description;
        this.pp = pp;
    }

}
