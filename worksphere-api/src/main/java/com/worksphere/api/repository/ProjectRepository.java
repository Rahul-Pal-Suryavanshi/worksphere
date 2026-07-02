package com.worksphere.api.repository;

import com.worksphere.api.entity.Project;
import com.worksphere.api.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByWorkspace(Workspace workspace);
}
