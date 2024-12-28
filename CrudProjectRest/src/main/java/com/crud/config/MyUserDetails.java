package com.crud.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.crud.model.Users;

public class MyUserDetails implements UserDetails{
	
	

	public MyUserDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private Users users;
	

	public MyUserDetails(Users users) {
		this.users = users;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority= new SimpleGrantedAuthority("USER");
		return Arrays.asList(authority);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return users.getPassword();
	}
  
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return users.getUsername();
	}
    
}
