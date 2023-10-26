package com.cruxbackend.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cruxbackend.backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
  
}
