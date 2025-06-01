package com.example.demo.domain.team;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Range;

import com.example.demo.domain.AbilityData;
import com.example.demo.domain.ItemData;
import com.example.demo.domain.movements.MoveData;
import com.example.demo.domain.pokemon.PokemonType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/*Esta entidad representa un posible pokemón en un equipo */

@Entity
@NoArgsConstructor
@Data
public class PokemonTeamMember {

    @GeneratedValue
    @Id
    private Long id;

    private String name;

    /* NOTA: Estos campos se modificaron a posteriori y no se actualizaron las funciones que manipulan esta entidad,
    y por tanto, tampoco los campos de los sprites. Sospechoso de fallar */

    @Lob // Indica que es un campo grande (BLOB)
    @Column(columnDefinition = "MEDIUMBLOB") // Para MySQL
    private byte[] front_default_sprite;

    @Lob // Indica que es un campo grande (BLOB)
    @Column(columnDefinition = "MEDIUMBLOB") // Para MySQL
    private byte[] pc_sprite;


    private int base_hp;
    private int base_attack;
    private int base_defense;
    private int base_special_attack;
    private int base_special_defense;
    private int base_speed;

    @Range(min = 0, max = 252, message = "hp_ev must be between 0 and 252")
    private int hp_ev;
    @Range(min = 0, max = 252, message = "attack_ev must be between 0 and 252")
    private int attack_ev;
    @Range(min = 0, max = 252, message = "defense_ev must be between 0 and 252")
    private int defense_ev;
    @Range(min = 0, max = 252, message = "special_attack_ev must be between 0 and 252")
    private int special_attack_ev;
    @Range(min = 0, max = 252, message = "special_defense_ev must be between 0 and 252")
    private int special_defense_ev;
    @Range(min = 0, max = 252, message = "speed_ev must be between 0 and 252")
    private int speed_ev;


    @Range(min = 0, max = 31, message = "hp_iv must be between 0 and 31")
    private int hp_iv;
    @Range(min = 0, max = 31, message = "attack_iv must be between 0 and 31")
    private int attack_iv;
    @Range(min = 0, max = 31, message = "defense_iv must be between 0 and 31")
    private int defense_iv;
    @Range(min = 0, max = 31, message = "special_attack_iv must be between 0 and 31")
    private int special_attack_iv;
    @Range(min = 0, max = 31, message = "special_defense_iv must be between 0 and 31")
    private int special_defense_iv;
    @Range(min = 0, max = 31, message = "speed_iv must be between 0 and 31")
    private int speed_iv;


    @Size(max = 4, min = 1)
    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "pokemonTeam_moveData", joinColumns = @JoinColumn(name = "pokemonTeam_id"), inverseJoinColumns = @JoinColumn(name = "moveData_id"))
    private Set<MoveData> move_list = new HashSet<MoveData>();

    @ManyToOne
    @JoinColumn(name = "ability_id")
    private AbilityData ability;

    @Nullable
    @ManyToOne
    @JoinColumn(name = "itemData_id")
    private ItemData item;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Size(min = 1, max = 2)
    private Set<PokemonType> type_list = new HashSet<PokemonType>();

    @Nullable
    private PokemonType tera_type;

    @ManyToOne
    @JoinColumn(name = "natureData_id")
    private NatureData nature;

    
}
