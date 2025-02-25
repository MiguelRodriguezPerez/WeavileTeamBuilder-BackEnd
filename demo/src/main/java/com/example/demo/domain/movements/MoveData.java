package com.example.demo.domain.movements;


import java.util.HashSet;
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
import lombok.ToString;

@ToString(exclude = "pokemon_list")
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

    /* WARNING: Esta no es la manera correcta de usar un HashSet.
    Usaste este constructor porque java no permite añadir valores a Set nulos.
    Ya lo hiciste antes sin esta "cosa". Averigua como arreglarlo*/
    public MoveData() {
        this.pokemon_list = new HashSet<>();
    }
}
