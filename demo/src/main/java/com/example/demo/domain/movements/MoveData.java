package com.example.demo.domain.movements;

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

    private MoveType move_Type;

    private byte accuracy;

    private String description;

    public MoveData(String name, MoveType move_Type, byte accuracy, String description) {
        this.name = name;
        this.move_Type = move_Type;
        this.accuracy = accuracy;
        this.description = description;
    }

}
