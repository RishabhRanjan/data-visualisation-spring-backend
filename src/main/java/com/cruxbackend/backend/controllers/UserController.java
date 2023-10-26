package com.cruxbackend.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cruxbackend.backend.model.User;
import com.cruxbackend.backend.service.UserService;

@RestController
public class UserController {
  @Autowired
  private UserService service;

  @PostMapping("/addUser")
  public User addUser(@RequestBody User user ){
    System.out.println(user);
    return service.creatUser(user);
  }

  @GetMapping("/users")
  public List<User> getUser(){
    return service.getUsers();
  }
}
