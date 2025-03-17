package com.example.demo.domain;

import java.util.Set;

import com.example.demo.domain.team.PokemonTeamMember;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"pokemon_team_list", "image_sprite"})
@NoArgsConstructor
@Entity
public class ItemData {

    @Id
    @GeneratedValue
    private Long id;

    @Lob // Indica que es un campo grande (BLOB)
    @Column(columnDefinition = "MEDIUMBLOB") // Para MySQL
    private byte[] image_sprite;

    private String name;
    private String description;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = false)
    private Set<PokemonTeamMember> pokemon_team_list;

}