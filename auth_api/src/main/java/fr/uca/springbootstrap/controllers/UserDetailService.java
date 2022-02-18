package fr.uca.springbootstrap.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.util.JSONPObject;
import fr.uca.springbootstrap.models.ERole;
import fr.uca.springbootstrap.models.Role;
import fr.uca.springbootstrap.models.User;
import fr.uca.springbootstrap.payload.request.LoginRequest;
import fr.uca.springbootstrap.payload.request.SignupRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.uca.springbootstrap.repository.RoleRepository;
import fr.uca.springbootstrap.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/userservice")
public class UserDetailService {
	@Valid

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@GetMapping("")
	public ResponseEntity<String> getStatus(){
		System.out.println("TeaPot");
		return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("I am Up ! ");
	}
	@GetMapping(value = "/username/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getUserInformationByLogin( @PathVariable String login ) throws JSONException {
		System.out.println("GetUserInformation for login ["+login+"]");
		Optional<User> optionalUser = userRepository.findByUsername(login);

		if(! optionalUser.isPresent()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username doesn't exit");
		}
		JSONObject person = new JSONObject();
		person.put("username",optionalUser.get().getUsername());
		person.put("email", optionalUser.get().getUsername());
		person.put("id", optionalUser.get().getId());
		person.put("role",optionalUser.get().getRoles());


		return ResponseEntity.status(HttpStatus.ACCEPTED).body(person.toString());
	}


	@PostMapping(value = "/username")
	public ResponseEntity registerUser(@Valid @RequestBody SignupRequest signUpRequest){
		System.out.println(" register User Function");
		Optional<User> optionalUser = userRepository.findByUsername(signUpRequest.getUsername()) ;
		if( userRepository.existsByUsername(signUpRequest.getUsername())){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Username Already exist ");
		}
		if(userRepository.existsByEmail(signUpRequest.getEmail())){
			return ResponseEntity.status(HttpStatus.CONFLICT).body(" Email Already used ");
		}

		User newUser = createUser(	signUpRequest.getUsername(),
									signUpRequest.getEmail(),
									signUpRequest.getPassword(),
									signUpRequest.getRole() );

		userRepository.save(newUser);

		return ResponseEntity.status(HttpStatus.CONFLICT).body(" Username Already Exist ");
	}
	User createUser(String userName, String email, String password, Set<String> strRoles) {
		User user = new User(userName, email, password);
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "teacher":
						Role modRole = roleRepository.findByName(ERole.ROLE_TEACHER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(modRole);

						break;
					default:
						Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
				}
			});
		}
		user.setRoles(roles);
		return user;
	}

}
