package com.meritcap.service.impl;

import com.meritcap.DTOs.requestDTOs.project.ProjectRequestDto;
import com.meritcap.DTOs.responseDTOs.project.ProjectResponseDto;
import com.meritcap.exception.NotFoundException;
import com.meritcap.model.Project;
import com.meritcap.model.Student;
import com.meritcap.repository.ProjectRepository;
import com.meritcap.repository.StudentRepository;
import com.meritcap.service.ProjectService;
import com.meritcap.transformer.ProjectTransformer;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, StudentRepository studentRepository) {
        this.projectRepository = projectRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public ProjectResponseDto addProject(ProjectRequestDto projectRequestDto, Long studentId) {
        if(studentRepository.findById(studentId).isPresent()){
            Student student = studentRepository.findById(studentId).get();
            Project project =  ProjectTransformer.toEntity(projectRequestDto);
            student.getProjects().add(project);
            project.setStudent(student);
            project = projectRepository.save(project);
            return ProjectTransformer.toDTO(project, student);
        }
        throw new NotFoundException("Student not found");
    }

    @Override
    public ProjectResponseDto getProject(Long id) {
        if(projectRepository.findById(id).isPresent()){
            Project project = projectRepository.findById(id).get();
            Student student = project.getStudent();
            return ProjectTransformer.toDTO(project, student);
        }
        throw new NotFoundException("Project not found");
    }

    @Override
    public List<ProjectResponseDto> getProjects(Long studentId) {
        if(studentRepository.findById(studentId).isPresent()){
            Student student = studentRepository.findById(studentId).get();
            List<Project> projects = student.getProjects();
            return ProjectTransformer.toDTO(projects, student);
        }
        throw new NotFoundException("Student not found");
    }

    @Override
    public ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto, Long id) {
        if(projectRepository.findById(id).isPresent()){
            Project project = projectRepository.findById(id).get();
            ProjectTransformer.updateProject(project, projectRequestDto);
            project = projectRepository.save(project);
            return ProjectTransformer.toDTO(project, project.getStudent());
        }
        throw new NotFoundException("Project not found");
    }

    @Override
    public ProjectResponseDto deleteProject(Long id) {
        if(projectRepository.findById(id).isPresent()){
            Project project = projectRepository.findById(id).get();
            Student student = project.getStudent();
            projectRepository.deleteById(id);
            return ProjectTransformer.toDTO(project, student);
        }
        throw new NotFoundException("Project not found");
    }
}
