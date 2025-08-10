package com.example.demo.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MoveDto {

    private String name;
    private String move_type;
    private String pokemon_type;
    private int power;
    private int accuracy;
    private String description;
    private int pp;

}
