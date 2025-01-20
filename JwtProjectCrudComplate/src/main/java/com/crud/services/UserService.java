package com.crud.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.entity.Users;
import com.crud.repo.UsersRepo;

@Service
public class UserService {

    @Autowired
    private UsersRepo usersrepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // Register a new user
    public Users register(Users user) {
        // Encrypt the user's password before saving
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        // Save the user in the database
        return usersrepo.save(user);
    }

    // Verify user credentials
    public String verify(Users user) {
        try {
            // Fetch the user from the database
            Users dbUser = usersrepo.findByusername(user.getUsername());
            if (dbUser == null) {
                return "User not found.";
            }

            // Validate the password
            if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
                return "Invalid credentials.";
            }

            // Generate a JWT token
            String token = jwtService.generateToken(dbUser.getUsername());

            // Update and save the user with the token
            dbUser.setToken(token);
            usersrepo.save(dbUser);

            return token;
        } catch (Exception ex) {
            // Handle any unexpected errors gracefully
            return "An error occurred during verification: " + ex.getMessage();
        }
    }

    // Get all users from the database
    public List<Users> getAllUsers() {
        return usersrepo.findAll();
    }
}
