package com.bckndapp.service;

import com.bckndapp.entity.Role;
import com.bckndapp.entity.User;
import com.bckndapp.repository.UserRepository;
import com.bckndapp.security.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@AllArgsConstructor
@Service
public class UserService {
	private UserRepository userRepository;
	private PasswordEncoder encoder;

	public ResponseEntity registerUser(User user) {
		if( !doesUserExists(user.getUsername())){
			user.setRole(Role.CLIENT);
			user.setPassword(encoder.encode(user.getPassword()));
			userRepository.save(user);
			return ResponseEntity.ok("user was saved");
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("username is taken");
	}

	private boolean doesUserExists(String username) {
		Optional<User>optionalUser = userRepository.findByUsername(username);
		return optionalUser.isPresent();
	}

	public ResponseEntity changeRole(User user) {
		if(doesUserExists(user.getUsername())){
			User userInDatabase=userRepository.findByUsername(user.getUsername()).get();
			userInDatabase.setRole(user.getRole());
			userRepository.save(userInDatabase);
			return ResponseEntity.status(HttpStatus.CREATED).body("Role was updated");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User was not found in database");
	}
}
