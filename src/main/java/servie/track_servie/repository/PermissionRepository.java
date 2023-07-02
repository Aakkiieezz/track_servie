package servie.track_servie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import servie.track_servie.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer>
{}
