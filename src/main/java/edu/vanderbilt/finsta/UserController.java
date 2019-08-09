package edu.vanderbilt.finsta;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.vanderbilt.finsta.security.Authority;
import edu.vanderbilt.finsta.security.User;
import edu.vanderbilt.finsta.security.UserRepository;

// Fun for later!
// ' or pu.user like '%
// %27%20or%20pu.user%20like%20%27%25

@RestController
public class UserController {

	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@RequestMapping("/user")
	public boolean addUser(String login, String email, String first, String last, String pass) {
		User user = new User();
		user.setActivated(true);
		user.setEmail(email);
		user.setLogin(login);
		user.setFirstName(first);
		user.setLastName(last);
		user.setPassword(this.passwordEncoder.encode(pass));

		Set<Authority> authorities = new HashSet<>();
		user.setAuthorities(authorities);
		
		this.userRepository.save(user);
		
		return true;
	}
	
	
	
}
