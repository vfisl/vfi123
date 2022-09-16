package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.JWTUser;
import com.example.model.service.JWTUserServiceImpl;
import com.example.repository.JWTUserRepository;
import com.example.util.JwtTokenUtil;

@RestController
public class JWTAuthController {

	@Autowired
	private JWTUserServiceImpl userServiceImpl;

	@Autowired
	private JWTUserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenUtil tokenUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/authenticate")
	public ResponseEntity<String> authenticate(@RequestBody JWTUser jwtUser) {
		String username = jwtUser.getUsername();
		String password = jwtUser.getPassword();
		authenticate(username, password);
		// check if name exists
		UserDetails user = userServiceImpl.loadUserByUsername(username);
		// generate the token and return it
		String token = tokenUtil.generateToken(user);
		return ResponseEntity.ok(token);

	}

	private void authenticate(String username, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			System.out.println("user disabled");
		} catch (BadCredentialsException e) {
			System.out.println("Invalid credentials");
		}

	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody JWTUser jwtUser) {
		String password = jwtUser.getPassword();
		String newpass = passwordEncoder.encode(password);
		String uname = jwtUser.getUsername();
		JWTUser newUser = new JWTUser(uname, newpass);
		userServiceImpl.addUser(newUser);
		return ResponseEntity.ok("registered");

	}

	public void addUser(JWTUser user) {
		userRepository.save(user);
	}
}
