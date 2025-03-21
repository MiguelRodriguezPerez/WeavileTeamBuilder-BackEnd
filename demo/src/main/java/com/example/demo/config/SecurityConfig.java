package com.example.demo.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.config.jwt.JwtAuthenticationFilter;
import com.example.demo.config.jwt.WeavileUserDetailsImpl;

/* Tuviste que definir obligatoriamente la seguridad de spring porque cors rechazaba solicitudes del cliente
Además spring te obligo a definir usuarios logueados para poder implementar la seguridad */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    /* Este "bean" es el encargado de gestionar la autenticación del usuario. En concreto recibe las solicitudes
    de login y decide en que AuthenticationProvider delegar dicha tarea */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    /* AuthenticationProvider recibe la tarea delegada de AuthenticationManager de identificar 
    al usuario por las credenciales del login usando la base de datos */    
    @Bean
    AuthenticationProvider authenticationProvider(WeavileUserDetailsImpl weavileUserDetailsImpl) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(weavileUserDetailsImpl);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /* JWT y Oauth2 son stateless. Una aplicación es stateless cuando el lado servidor no mantiene 
    el estado de la sesión del usuario y en su lugar, se realiza a través de tokens que recibe del lado cliente.
    
    SecurityContextRepository es un componente de spring que almacena y recupera el "contexto de seguridad" 
    (los datos y permisos del usuario logueado) a lo largo de las solicitudes del usuario.
    ¿Recuerdas las líneas de 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Contrata currentUsuario = this.obtenerPorNombre(authentication.getName());
        return currentUsuario; 
    ? Este bean es el que provee el SecurityContextHolder que a través del getter de su propiedad "context"
    y "authentication" te permite obtener el usuario logueado.

    NOTA: Por defecto SecurityContextRepository esta configurado para funcionar con cookies de sesión, es decir
    que no sería stateless por defecto. Eres tú el que tiene cambiarlo

    En teoría no necesitas definir el bean SecurityContextRepository para definir la aplicación como stateless,
    pero con esto te aseguras de definir la seguridad como stateless */
    @Bean 
    SecurityContextRepository securityContextRepository() { 
        return new NullSecurityContextRepository();
    }

    /* Este es el encriptador de contraseñas */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        /* Estas líneas crean una configuración personalizada de CORS */
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        // ¿Realmente necesitas permitir credenciales en jwt? ¿Y el header?
        configuration.setAllowCredentials(false);
        configuration.addAllowedHeader("*");

        /* Estas líneas aplican dicha configuración personalizade de CORS al ámbito de "pattern",
        en este caso a toda la aplicación */
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

    /* En SecurityFilterChain le debes mandar como argumento tu AuthenticationProvider personalizado para decirle a 
    Spring que use tu AuthenticationProvider para validar usuarios */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationProvider authenticationProvider,
        JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception{

            http.headers(headersConfigurer -> headersConfigurer.frameOptions(frameOptions -> frameOptions.sameOrigin()));

            http.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            http.sessionManagement(httpSecuritySessionManagementConfigurer -> 
            // Recuerda que a día de hoy solo sabes borrar jwtToken en el lado cliente
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/nonLoggedUsers/**").permitAll()
                .anyRequest().authenticated());

            http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

            /* En teoría es seguro desactivar csrf si llevas a cabo el login a través de jwt u oauth2,
            a no ser que uses "cookies de sesión?" */
            http.csrf(csrf -> csrf.disable());

            return http.build();
        }
}
