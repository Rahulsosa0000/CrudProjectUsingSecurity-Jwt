package com.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.model.Users;
import com.crud.service.UserService;



@RestController
@RequestMapping("/crud/users")
public class UserController {
	
	

    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return userService.register(user);

    }

    @PostMapping("/login")
    public String login(@RequestBody Users user) {

        return userService.verify(user);
    }
    
    @GetMapping("/")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();  // Calls the service layer to fetch all users
    }
    
}