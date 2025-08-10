package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class DemoApplication {

	/* NOTA: Ejecuta este proyecto desde demo y no la carpeta WeavileTeamBuilder-Backend */

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();

        /* Por si solo Spring no recibe bien las variables. Concretamente:
		 * - SERVER_PORT la recibe pero no la castea a string
		 * - JWT_KEY ni siquiera la lee
		 */
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		SpringApplication.run(DemoApplication.class, args);
	}

}
