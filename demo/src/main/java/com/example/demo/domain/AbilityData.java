package com.example.demo.domain;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.collection.spi.PersistentSet;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = "pokemon_list")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@Entity
public class AbilityData {
    // TODO: Realizar relaciones entre entidades (movs,pokemon...)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    /* WARNING: Probablemente esta no es la manera correcta de usar un PersistentSet.
    Usaste este constructor porque java no permite añadir valores a Set nulos.
    Ya lo hiciste antes sin esta "cosa". Averigua como arreglarlo*/
    @ManyToMany(mappedBy = "ability_list", fetch = FetchType.EAGER)
    private Set<PokemonData> pokemon_list = new PersistentSet<>();

}
