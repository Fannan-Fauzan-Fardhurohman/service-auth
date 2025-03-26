package com.fanxan.serviceauth.repository;

import com.fanxan.serviceauth.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByRoleNameIn(Collection<String> roleName);
}