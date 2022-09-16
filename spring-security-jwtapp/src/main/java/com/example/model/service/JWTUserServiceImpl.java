package com.example.model.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.model.JWTUser;
import com.example.repository.JWTUserRepository;

public class JWTUserServiceImpl implements UserDetailsService{

	@Autowired
	private JWTUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		JWTUser user=userRepository.findByUsername(username);
		String uname=user.getUsername();
		String pass=user.getPassword();
		SimpleGrantedAuthority authority1=new SimpleGrantedAuthority("USER");
		SimpleGrantedAuthority authority2=new SimpleGrantedAuthority("ADMIN");
		List<GrantedAuthority> grantedAuthority=Arrays.asList(authority1,authority2);
		UserDetails newuser=new User(uname,pass,grantedAuthority);
		return newuser;
	}
	public void addUser(JWTUser user) {
		userRepository.save(user);
	}

}
