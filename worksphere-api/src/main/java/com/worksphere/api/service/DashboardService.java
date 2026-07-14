package com.worksphere.api.service;

import com.worksphere.api.dto.DashboardResponse;
import com.worksphere.api.entity.Project;
import com.worksphere.api.entity.User;
import com.worksphere.api.entity.Workspace;
import com.worksphere.api.enums.TaskStatus;
import com.worksphere.api.repository.ProjectRepository;
import com.worksphere.api.repository.TaskRepository;
import com.worksphere.api.repository.UserRepository;
import com.worksphere.api.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final WorkspaceRepository workspaceRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public DashboardResponse getDashboard(
            UUID workspaceId,
            Authentication authentication
    ){
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("User Not Found"));

        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()->
                        new RuntimeException("Workspace not found"));

        if(!workspace.getOwner().getId().equals(loggedInUser.getId())){
            throw new RuntimeException("You are not allowed to access this workspace");
        }

        List<Project> projects = projectRepository.findByWorkspace(workspace);

        long totalProjects = projects.size();

        long totalTasks =0;
        long todoTasks=0;
        long inProgressTasks=0;
        long doneTasks=0;

        for(Project project:projects){

            totalTasks += taskRepository.countByProject(project);

            todoTasks += taskRepository.countByProjectAndStatus(project, TaskStatus.TODO);

            inProgressTasks += taskRepository.countByProjectAndStatus(project, TaskStatus.IN_PROGRESS);

            doneTasks += taskRepository.countByProjectAndStatus(project, TaskStatus.DONE);
        }

        double completionPercentage =0;

        if(totalTasks>0){
            completionPercentage = (doneTasks*100.0)/totalTasks;
        }

        return DashboardResponse.builder()
                .workspaceName(workspace.getName())
                .totalProjects(totalProjects)
                .totalTasks(totalTasks)
                .todoTasks(todoTasks)
                .inProgresstasks(inProgressTasks)
                .doneTasks(doneTasks)
                .completionPercentage(completionPercentage)
                .build();
    }
}
