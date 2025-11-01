package com.jeespring.AIPortfolio;

import com.jeespring.AIPortfolio.entity.*;
import com.jeespring.AIPortfolio.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiPortfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiPortfolioApplication.class, args);
	}


	@Bean
	CommandLineRunner initDatabase(ProfileRepository profileRepository,
								   SkillRepository skillRepository,
								   ProjectRepository projectRepository,
								   ExperienceRepository experienceRepository,
								   EducationRepository educationRepository) {
		return args -> {

			// --- PROFILE ---
			if (profileRepository.count() == 0) {
				Profile profile = new Profile();
				profile.setFullName("Salahddine Jabour");
				profile.setTitle("Data Scientist & Java Developer");
				profile.setBio("Passionné par l'IA, Spring Boot et la data science. "
						+ "J’aime transformer les idées en applications concrètes.");
				profile.setEmail("salah.jabour@example.com");
				profile.setLinkedInUrl("https://linkedin.com/in/salahddine-jabour");
				profile.setGithubUrl("https://github.com/salahddinejabour");
				profileRepository.save(profile);
			}

			// --- SKILLS ---
			if (skillRepository.count() == 0) {
				skillRepository.save(new Skill(null, "Java", "Expert"));
				skillRepository.save(new Skill(null, "Spring Boot", "Advanced"));
				skillRepository.save(new Skill(null, "Python", "Intermediate"));
			}

			// --- PROJECTS ---
			if (projectRepository.count() == 0) {
				projectRepository.save(new Project(null, "AI Portfolio Assistant",
						"Application qui permet à un utilisateur d’interagir avec mon CV via l’IA.",
						"Spring Boot, LangChain4j, PostgreSQL", "https://github.com/salahddinejabour/ai-portfolio"));
				projectRepository.save(new Project(null, "Stock Management System",
						"Gestion de stock avec API REST et base MySQL.", "Spring Boot, JPA, Angular", null));
				projectRepository.save(new Project(null, "Data Analysis Dashboard",
						"Dashboard interactif pour visualiser des données.", "Python, Dash, Pandas", null));
			}

			// --- EXPERIENCES ---
			if (experienceRepository.count() == 0) {
				experienceRepository.save(new Experience(null, "TechCorp", "Backend Developer",
						"2023-01", "2024-06",
						"Développement de microservices en Spring Boot et intégration de services REST."));
				experienceRepository.save(new Experience(null, "DataVision", "Data Analyst",
						"2022-03", "2022-12",
						"Analyse de données clients et génération de rapports automatisés."));
				experienceRepository.save(new Experience(null, "Freelance", "Full Stack Developer",
						"2021-05", "2022-02",
						"Création d’applications web sur mesure pour des PME."));
			}

			// --- EDUCATION ---
			if (educationRepository.count() == 0) {
				educationRepository.save(new Education(null, "Université Hassan II",
						"Licence", "Mathématiques et Informatique", "2019", "2022"));
				educationRepository.save(new Education(null, "YouCode", "Formation", "Développement Web Full Stack", "2022", "2023"));
				educationRepository.save(new Education(null, "OpenAI Academy", "Certification", "AI & Machine Learning", "2024", "2024"));
			}

			System.out.println("✅ Données de test initialisées avec succès !");
		};
	}


}
