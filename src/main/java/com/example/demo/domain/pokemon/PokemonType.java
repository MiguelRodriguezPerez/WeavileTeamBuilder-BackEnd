package com.example.demo.domain.pokemon;

import java.util.HashSet;
import java.util.Set;
import com.example.demo.domain.movements.MoveData;
import com.example.demo.domain.team.PokemonTeamMember;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {"pokemon_list", "move_list"})
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class PokemonType {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;
    /* POR EL AMOR DE DIOS NO BORRES LOS JSON IGNORE. NUNCA VAS A NECESITAR ESAS COLECCIONES */

    @JsonIgnore
    @JsonBackReference
    @ManyToMany(mappedBy = "type_list", fetch = FetchType.LAZY)
    private Set<PokemonData> pokemon_list = new HashSet<>();

    @JsonIgnore
    @JsonBackReference
    @ManyToMany(mappedBy = "team_member_type_list", fetch = FetchType.LAZY)
    private Set<PokemonTeamMember> pokemon_team_member_list = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "pokemon_type", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<MoveData> move_list = new HashSet<>();

}
