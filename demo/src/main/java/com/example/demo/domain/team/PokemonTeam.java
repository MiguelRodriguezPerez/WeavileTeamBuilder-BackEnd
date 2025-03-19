package com.example.demo.domain.team;

import java.util.Set;

import lombok.Data;

@Data
public class PokemonTeam {
    
    // TODO: Persistir en lado servidor

    private int id;
    private String nombre;
    private TeamType teamType;
    private Set<PokemonTeamMember> teamMembers;

    /* Para decidir la cantidad de pokemón que tiene un equipo según su modalidad
    creas un constructor que recibe un enum que será el tipo del equipo (Individual, 1vs1 ...)

    En el constructor hay una función que recibe el enum del tipo del equipo y que devuelve
    un int que decidirá cuantos miembros hay en ese equipo */

    // TODO: Validar que los pokemón no se repitan

    public PokemonTeam(TeamType teamType) {

        this.teamType = teamType;

        final int numMembers = this.setTeamSize(teamType);

        for(int i = 0; i < numMembers; i++){
            this.teamMembers.add(new PokemonTeamMember());
        }

    }

    private int setTeamSize(TeamType teamType) {

        String choosenType = teamType.toString();

        if (choosenType.equalsIgnoreCase("INDIVIDUAL")
                || choosenType.equalsIgnoreCase("DOUBLE")    
                || choosenType.equalsIgnoreCase("VGC")
                || choosenType.equalsIgnoreCase("MONOTYPE")) return 6;
        
        if (choosenType.equalsIgnoreCase("1VS1")) return 1;

        else throw new RuntimeException("TeamType unhandled: " + teamType.toString());
    }
}
