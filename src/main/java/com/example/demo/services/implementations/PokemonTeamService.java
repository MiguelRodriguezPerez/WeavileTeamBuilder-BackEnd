package com.example.demo.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.team.PokemonTeam;
import com.example.demo.domain.team.PokemonTeamMember;
import com.example.demo.services.interfaces.PokemonTeamInterface;

@Service
public class PokemonTeamService implements PokemonTeamInterface {

    @Autowired
    NatureDataService nature_service;

    @Override
    public PokemonTeam generateNewTeam() {
        PokemonTeam team = new PokemonTeam();

        final int numMembers = 6; // No soportar distintos tipos de equipo

        for (int i = 0; i < numMembers; i++) {
            PokemonTeamMember member = new PokemonTeamMember();
            /*
             * Tienes que fijar el id del miembro basándote en su posición en el array
             * para que el cliente no tenga problemas para manipularlo. Sospechoso de fallar
             */

            member.setTeam_index_id(i);
            member.setPokemon_data_id(0);
            member.setNature(nature_service.getNatureByName("hardy"));
            team.getTeamMembers().add(member);
        }

        return team;
    }

}
