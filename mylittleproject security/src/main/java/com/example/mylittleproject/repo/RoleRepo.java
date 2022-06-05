package com.example.mylittleproject.repo;
import java.util.Optional;

import com.example.mylittleproject.enumerator.ERole;
import com.example.mylittleproject.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
	Optional<Role> findByName(ERole name);
}