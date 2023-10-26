package com.cruxbackend.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cruxbackend.backend.model.User;
import com.cruxbackend.backend.repo.UserRepository;

@Service
public class UserService {
  @Autowired
  private UserRepository repo;

  public User creatUser(User user){
    return repo.save(user);
  }

  public java.util.List<User> getUsers(){
    return repo.findAll();
  }
}
