package com.worksphere.api.repository;

import com.worksphere.api.entity.Project;
import com.worksphere.api.entity.Task;
import com.worksphere.api.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByProject(Project project);

    long countByProject(Project project);

    long countByProjectAndStatus(Project project, TaskStatus status);
}
