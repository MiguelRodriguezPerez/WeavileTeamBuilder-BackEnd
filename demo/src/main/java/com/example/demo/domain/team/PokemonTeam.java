package com.example.demo.domain.team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class PokemonTeam {
    
    // TODO: Persistir en lado servidor

    private int id;
    private String name;
    private TeamType teamType;

    /* Este campo originalmente era un Set pero al crear la entidad PokemonTeam 
    necesitas que te añada objetos PokemonTeamMember con campos vacíos y la colección Set lo impedía.
    
    Mi solución fue cambiar el Set por un List y validar que no haya nombres repetidos con el método de esta clase
    validateTeam */
    private List<PokemonTeamMember> teamMembers = new ArrayList<>();

    /* Para decidir la cantidad de pokemón que tiene un equipo según su modalidad
    creas un constructor que recibe un enum que será el tipo del equipo (Individual, 1vs1 ...)

    En el constructor hay una función que recibe el enum del tipo del equipo y que devuelve
    un int que decidirá cuantos miembros hay en ese equipo */


    public PokemonTeam (TeamType teamType) {
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

    /* Tienes que validar que los nombres de los pokemón no se repitan, pero si debes admitir nombres repetidos
    en caso de que sean null o "" */
    public List<PokemonTeamMember> validateTeam(List<PokemonTeamMember> listArg) {
        Set<String> nombresVistos = new HashSet<>();

        /* .filter(p -> nombresVistos.add(p.getName()) en teoría añade un nombre y a la vez válida
        que el nombre no sea repetido al comprobar los nombres de la lista (supuestamente, ya que es una
        ocurrencia de ChatGPT)
        TODO: Comprobar si este método válida bien y si existe una anotación que haga esto */

        List<PokemonTeamMember> filtrado = listArg.stream()
            .filter(p -> p.getName() != null && !p.getName().isEmpty()) // Elimina null y ""
            .filter(p -> nombresVistos.add(p.getName())) // Deja pasar solo el primer nombre único
            .collect(Collectors.toList());

        /* Si el largo del filtrado no coincide con el largo del equipo esperado basado en su tipo de equipo
        lanzará una excepción */
        if (this.setTeamSize(this.teamType) != filtrado.size()) throw new RuntimeException("Validación fallida. Has introducido dos nombres iguales");
        else {
            this.teamMembers = filtrado;
            return filtrado;
        }

    }
}
