package com.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.model.Users;

public interface UsersRepo extends JpaRepository<Users, Integer>  {

	Users findByusername(String username);
	  //Optional<Users> findByToken(String token); // Fetch user by token

}
