package com.example.fitfolio.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.fitfolio.entities.Activite;
import com.example.fitfolio.entities.Role;
import com.example.fitfolio.entities.Utilisateur;
import com.example.fitfolio.entities.Planning;
import com.example.fitfolio.entities.Repas;
import com.example.fitfolio.repo.ActiviteRepository;
import com.example.fitfolio.repo.RoleRepository;
import com.example.fitfolio.repo.UserRepository;
import com.example.fitfolio.service.UserService;
import com.example.fitfolio.repo.PlanningRepository;
import com.example.fitfolio.repo.RepasRepository;

@Configuration
public class DataInitializer {
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder(); // Password encoder              
	    }
    @Bean
    CommandLineRunner initData(UserRepository userRepository,
    		RoleRepository roleRepository,
    		RepasRepository repasRepository,
    		ActiviteRepository activiteRepository,
    		PlanningRepository planningRepository,UserService userService
    		) {
        return args -> {

            Role adminRole = new Role(null, "ADMIN");adminRole = roleRepository.save(adminRole);
            Role adminRole1 = new Role(null, "SYSTEM");adminRole1 = roleRepository.save(adminRole1);
            Role adminRole2 = new Role(null, "STOCK");adminRole2 = roleRepository.save(adminRole2);
            Role userRole = new Role(null, "USERS");userRole = roleRepository.save(userRole);
            Utilisateur admin = new Utilisateur(null, "admin", "admin", List.of(adminRole, userRole),24);
            userService.addNewUser(admin);  	
            Utilisateur ali = new Utilisateur(null, "ali","ali", List.of(userRole,adminRole1),21);
            	userService.addNewUser(ali);
            	Utilisateur mekiani = new Utilisateur(null, "mekiani", "mekiani", List.of(userRole,adminRole1),22);
            	userService.addNewUser(mekiani);
				Utilisateur leila = new Utilisateur(null,"leila", "mekiani",List.of(userRole,adminRole1),22);
				userService.addNewUser(leila);
			Utilisateur utilisateur = new Utilisateur(
					"2024-12-15",
					null,
					"oumaima",
					"oumaima",
					List.of(userRole, adminRole2),
					"Leila",
					"leila@gmail.com",
					"mekiani",
					"0661248878",
					24,
					64.00,
					160.0,
					"Feminin",
					"PERTE DE POIDS",
					List.of()
			);


			userService.addNewUser(utilisateur);
            	Repas dejeuner = new Repas(null, "Déjeuner équilibré", "Riche en protéines.", "Petit-déjeuner", 300.0, "Matin", "PERTEPOID", "image-petit-dejeuner.jpg");
            	Repas diner =new Repas(null, "Diner diététique", "Poulet grillé avec légumes.", "Déjeuner", 500.0, "Midi", "MAINTIEN", "image-dejeuner.jpg");	
            	repasRepository.save(dejeuner);
            	repasRepository.save(diner);
            	
            	Repas petitDejeuner = new Repas(
            		    null,
            		    "Petit Déjeuner",
            		    "Un repas équilibré avec des œufs et du pain complet.",
            		    "Petit Déjeuner",
            		    350.0,
            		    "Matin",
            		    "PERTEPOID",
            		    "image_url.jpg"
            		);
            	repasRepository.save(petitDejeuner);
            	
            	Activite courseapied = new Activite(
            		    null,
            		    "Course à pied",
            		    "Une activité physique pour brûler des calories.",
            		    "Sport",
            		    400.0,
            		    "Cardio",
            		    "PERTEPOID",
            		    "image-course.jpg",
						100.00,
						"speed"

            		);
            	activiteRepository.save(courseapied);
            		Activite relaxation = new Activite(
            		    null,
            		    "Yoga",
            		    "Exercice de relaxation et de souplesse.",
            		    "Bien-être",
            		    200.0,
            		    "Relaxation",
            		    "MAINTIEN",
            		    "image-yoga.jpg",
						100.00,
						"speed"
            		);
            		activiteRepository.save(relaxation);
            		
            		
            		Planning lundi = new Planning(
            		    null,
            		    "PL123",
            		    "2024-12-15",
            		    List.of(
            		        dejeuner,
            		        diner
            		    ),
            		    List.of(
            		        courseapied,
            		        relaxation
            		    ),utilisateur
						
            		);
					Planning mardi= new Planning(
            		    null,
            		    "PL124",
            		    "2024-12-14",
            		    List.of(
            		        dejeuner,
            		        diner
            		    ),
            		    List.of(
            		        courseapied,
            		        relaxation
            		    ),utilisateur
						
            		);
            		
            		planningRepository.save(lundi);
					planningRepository.save(mardi);


            		
            		
            System.out.println("Initialisation des données terminée. admin ali et mekiani");
            
        };
    }
}
