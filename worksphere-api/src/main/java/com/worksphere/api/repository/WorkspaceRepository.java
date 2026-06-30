package com.worksphere.api.repository;

import com.worksphere.api.entity.User;
import com.worksphere.api.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {

    List<Workspace> findByOwner(User owner);
}
