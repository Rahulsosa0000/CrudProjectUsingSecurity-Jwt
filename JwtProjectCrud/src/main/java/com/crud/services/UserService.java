package com.crud.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.crud.entity.Users;
import com.crud.repo.UsersRepo;


@Service
public class UserService {
	
	
	@Autowired
	private UsersRepo usersrepo;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwtService;
	
	 public Users register(Users user) {
		    user.getPassword();
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
	            return "invalid User";
	        }
	    }

	    

		public List<Users> getAllUsers() {
			// TODO Auto-generated method stub
			return usersrepo.findAll();
		}

}
