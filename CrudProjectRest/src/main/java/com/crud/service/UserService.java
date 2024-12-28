package com.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.model.Users;
import com.crud.repository.UsersRepo;



@Service
public class UserService {
	
	@ Autowired
	private UsersRepo usersrepo;
	
	  @Autowired
	   private JWTService jwtService;
	    
	    @Autowired
	    AuthenticationManager authManager;
	    
	    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	    public Users register(Users user) {
	        user.setPassword(encoder.encode(user.getPassword()));
	        usersrepo.save(user);
	        return user;
	    }
	    
//	    public String verify(Users user) {
//	        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//	   if (authentication.isAuthenticated()) {
//	         return  jwtService.generateToken(user.getUsername());
//	        } else {
//	            return "fail";
//	        }
//	    }
	    
	    public String verify(Users user) {
	        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
	        if (authentication.isAuthenticated()) {
	            // Generate the token
	            String token = jwtService.generateToken(user.getUsername());

	            // Fetch the user from the database to update the token
	            Users dbUser = usersrepo.findByusername(user.getUsername());

	            // Set the token in the user object
	            dbUser.setToken(token);

	            // Save the user with the token in the database
	            usersrepo.save(dbUser);

	            // Return the token
	            return token;
	        } else {
	            return "fail";
	        }
	    }

	    

		public List<Users> getAllUsers() {
			// TODO Auto-generated method stub
			return usersrepo.findAll();
		}

}
