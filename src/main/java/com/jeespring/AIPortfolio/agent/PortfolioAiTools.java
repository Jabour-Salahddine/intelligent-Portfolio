package com.jeespring.AIPortfolio.agent;


import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import com.jeespring.AIPortfolio.entity.*;
import com.jeespring.AIPortfolio.repository.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class PortfolioAiTools {

    private final ProfileRepository profileRepository;
    private final SkillRepository skillRepository;
    private final ProjectRepository projectRepository;
    private final ExperienceRepository experienceRepository;
    private final EducationRepository educationRepository;

    public PortfolioAiTools(ProfileRepository profileRepository,
                            SkillRepository skillRepository,
                            ProjectRepository projectRepository,
                            ExperienceRepository experienceRepository,
                            EducationRepository educationRepository) {
        this.profileRepository = profileRepository;
        this.skillRepository = skillRepository;
        this.projectRepository = projectRepository;
        this.experienceRepository = experienceRepository;
        this.educationRepository = educationRepository;
    }

    // --- PROFILE ---
    @Tool("Get the profile information of Salahddine")
    public Profile getProfile() {
        return profileRepository.findAll().stream().findFirst().orElse(null);
    }

    @Tool("Update the bio or title of the profile")
    public Profile updateProfile(String title, String bio) {
        Profile profile = profileRepository.findAll().stream().findFirst().orElse(null);
        if (profile == null) return null;
        profile.setTitle(title);
        profile.setBio(bio);
        return profileRepository.save(profile);
    }

    // --- SKILLS ---
    @Tool("Get all skills")
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Tool("Add a new skill to the portfolio")
    public Skill addSkill(String name, String level) {
        Skill skill = new Skill();
        skill.setName(name);
        skill.setLevel(level);
        return skillRepository.save(skill);
    }

    @Tool("Remove a skill by its name")
    public void deleteSkillByName(String name) {
        skillRepository.findAll().stream()
                .filter(skill -> skill.getName().equalsIgnoreCase(name))
                .findFirst()
                .ifPresent(skillRepository::delete);
    }

    // --- PROJECTS ---
    @Tool("List all projects")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Tool("Find project by name")
    public Project getProjectByName(String name) {
        return projectRepository.findAll().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Tool("Add a new project")
    public Project addProject(String name, String description, String technologies, String link) {
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setTechnologies(technologies);
        project.setLink(link);
        return projectRepository.save(project);
    }

    // --- EXPERIENCE ---
    @Tool("Get all experiences")
    public List<Experience> getAllExperiences() {
        return experienceRepository.findAll();
    }

    @Tool("Add a new experience")
    public Experience addExperience(String company, String role, String startDate, String endDate, String description) {
        Experience exp = new Experience();
        exp.setCompany(company);
        exp.setRole(role);
        exp.setStartDate(startDate);
        exp.setEndDate(endDate);
        exp.setDescription(description);
        return experienceRepository.save(exp);
    }

    // --- EDUCATION ---
    @Tool("Get all education records")
    public List<Education> getAllEducations() {
        return educationRepository.findAll();
    }

    @Tool("Add a new education record")
    public Education addEducation(String school, String degree, String field, String startDate, String endDate) {
        Education edu = new Education();
        edu.setSchool(school);
        edu.setDegree(degree);
        edu.setField(field);
        edu.setStartDate(startDate);
        edu.setEndDate(endDate);
        return educationRepository.save(edu);
    }
}