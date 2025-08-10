package com.example.demo.domain.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WeavileUser implements UserDetails {
    
    @GeneratedValue
    @Id
    private Long id;

    @NotNull
    @Size(min = 1, max = 25, message = "El nombre debe tener entre 1 y 25 caracteres")
    @Column(unique = true)
    private String username;

    @NotNull
    /* Combina al menos una mayúscula, una minúscula, un número y un carácter especial.
    Mínima longitud 14, máximo 30 */
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{14,30}$", 
        message = "La contraseña debe combinar mayúsculas, minúsculas y caracteres especiales"
    )
    private String password;

    @NotNull
    @Email(message = "Por favor, proporciona un correo electrónico válido")
    @Size(min = 1, max = 35, message = "El email debe tener como máximo 35 caracteres")
    private String email;

    @Override 
    public Collection<? extends GrantedAuthority> getAuthorities() { 
        return List.of(); 
    } 

    @Override 
    public boolean isAccountNonExpired() { 
        return true; 
    } 

    @Override 
    public boolean isAccountNonLocked() { 
        return true; 
    } 
    
    @Override 
    public boolean isCredentialsNonExpired() { 
        return true; 
    } 

    @Override 
    public boolean isEnabled() { 
        return true; 
    }
    
}
