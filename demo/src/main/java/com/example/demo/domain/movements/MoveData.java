package com.example.demo.domain.movements;

import com.example.demo.domain.PokemonType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private byte accuracy;

    private String description;

    // TODO: Calc max pp by multiplying api request * 160%;
    private byte pp;

    public MoveData(String name, MoveType move_Type, byte accuracy, String description, byte pp) {
        this.name = name;
        this.move_type = move_Type;
        this.accuracy = accuracy;
        this.description = description;
        this.pp = pp;
    }

}
