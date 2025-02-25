package com.example.demo.domain;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.domain.movements.MoveData;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@ToString(exclude = "id")
public class PokemonData {
    
    @GeneratedValue
    @Id
    private Long id;

    private String name;

    private int base_hp;
    private int base_attack;
    private int base_defense;
    private int base_special_attack;
    private int base_special_defense;
    private int base_speed;


    @Lob // Indica que es un campo grande (BLOB)
    @Column(columnDefinition = "MEDIUMBLOB") // Para MySQL
    private byte[] front_default_sprite;

    @Lob // Indica que es un campo grande (BLOB)
    @Column(columnDefinition = "MEDIUMBLOB") // Para MySQL
    private byte[] pc_sprite;

    private Set<PokemonType> type_list;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "pokemonData-abilityData", 
        joinColumns = @JoinColumn(name = "pokemonData_id"), inverseJoinColumns = @JoinColumn(name = "abilityData_id"))
    private Set<AbilityData> ability_list;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "pokemonData-moveData",
        joinColumns = @JoinColumn(name = "pokemonData_id"), inverseJoinColumns = @JoinColumn(name = "moveData_id"))
    private Set<MoveData> move_list;

    /* WARNING: Esta no es la manera correcta de usar un HashSet.
    Usaste este constructor porque java no permite añadir valores a Set nulos.
    Ya lo hiciste antes sin esta "cosa". Averigua como arreglarlo*/
    public PokemonData() {
        this.type_list = new HashSet<>();
        this.move_list = new HashSet<>();
        this.ability_list = new HashSet<>();
    }


}
