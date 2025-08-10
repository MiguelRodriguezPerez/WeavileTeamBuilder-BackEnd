package com.example.demo.domain.team;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = "pokemon_team_member_list")
@NoArgsConstructor
@Entity
public class NatureData {
    // TODO: Boost and decrease of 10% stat

    @GeneratedValue
    @Id
    private Long id;

    private String name;

    private String increased_stat;
    private String decreased_stat;

    @JsonIgnore
    @OneToMany(mappedBy = "nature", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<PokemonTeamMember> pokemon_team_member_list;

}
