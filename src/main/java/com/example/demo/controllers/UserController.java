package com.example.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserRepository userRepository;
	private final CartRepository cartRepository;
	private final BCryptPasswordEncoder encoder;

	public static final Logger logger = LogManager.getLogger(UserController.class);

	public UserController(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.encoder = encoder;
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {

		Optional<User> user = userRepository.findById(id);

		if (user.isPresent()) {
			logger.info("User found: " + user.get().toString());
			return ResponseEntity.ok(user.get());
		}else{
			logger.info("User Not found: "+id);
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);

		if (user != null) {
			logger.info("User found: " + user.toString());
			return ResponseEntity.ok(user);
		}

		logger.error("User not found: "+username);
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {


		if (createUserRequest.getUsername() == null || createUserRequest.getUsername().trim().isEmpty()) {
			logger.error("Fail to create User, username is mandatory");
			return ResponseEntity.badRequest().build();
		}else if(userRepository.existsByUsername(createUserRequest.getUsername())){
			logger.error("Fail to create User, username already exists");
			return ResponseEntity.badRequest().build();
		}else if(createUserRequest.getPassword().length() < 7 ){
			logger.error("Fail to create User, password length must be greater than 7 characters");
		}else if(createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword()) == false){
			logger.error("Fail to create User, password and confirm password are not same");
			return ResponseEntity.badRequest().build();
		}

		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		user.setPassword(encoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);

		logger.info("User "+user.toString()+" created successfully");

		return ResponseEntity.ok(user);

	}
	
}
