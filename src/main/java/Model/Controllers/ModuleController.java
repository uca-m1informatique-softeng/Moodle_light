package Model.Controllers;

import Model.Documents.Questionnaire;
import Model.Documents.Ressource;
import Model.Security.jwt.JwtUtils;
import Model.User.ERole;
import Model.User.Role;
import Model.User.User;
import Model.Documents.Module;
import Model.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import Model.Payload.response.MessageResponse;


import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module")
public class ModuleController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ModuleRepository moduleRepository;

	@Autowired
	RessourcesRepository ressourcesRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;


	@GetMapping("/{name}/getModules")
	public ArrayList<String> getmodules(@PathVariable String name){
		ArrayList<String> strings = new ArrayList<>();
		Optional<User> ouser = userRepository.findByUsername(name);
		if (!ouser.isPresent()) {
			return null;
		}
		User user= ouser.get();

		Set<Module> modules = user.getModules();
		for (Module module:modules) {
			strings.add(module.name + "/" + module.id);
		}
		return strings;
	}


	@PutMapping("/{id}/ressource/{ressourceid}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> addRessource(Principal principal,@PathVariable long id, @PathVariable long questionaireid){
		Optional<Module> omodule = moduleRepository.findById(id);
		Optional<Ressource> oressource = ressourcesRepository.findById(questionaireid);
		if (!omodule.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: No such module!"));
		}
		if (!oressource.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: No such ressource!"));
		}

		Module module = omodule.get();
		Ressource ressource = oressource.get();
		Set<Ressource> ressources = module.getRessources();
		if(!ressources.contains(ressource)) {
			ressources.add(ressource);
		}else{
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Ressource y apartient deja !"));
		}
		moduleRepository.save(module);
		return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
	}

	@DeleteMapping("/{id}/ressource/{ressourceid}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> deleteRessource(Principal principal,@PathVariable long id, @PathVariable long questionaireid){
		Optional<Module> omodule = moduleRepository.findById(id);
		Optional<Ressource> oressource = ressourcesRepository.findById(questionaireid);
		if (!omodule.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: No such module!"));
		}
		if (!oressource.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: No such ressource!"));
		}

		Module module = omodule.get();
		Ressource ressource = oressource.get();
		Set<Ressource> ressources = module.getRessources();
		if(ressources.contains(ressource)) {
			ressources.remove(ressource);
		}else{
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Ressource n'apartient pas a module!"));
		}
		moduleRepository.save(module);
		return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
	}

	@PostMapping("/{id}/participants/{userid}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> addUser(Principal principal, @PathVariable long id, @PathVariable long userid) {
		Optional<Module> omodule = moduleRepository.findById(id);
		Optional<User> ouser = userRepository.findById(userid);
		if (!omodule.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: No such module!"));
		}
		if (!ouser.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: No such user!"));
		}

		Module module = omodule.get();
		User user = ouser.get();
		User actor = userRepository.findByUsername(principal.getName()).get();

		Set<User> participants = module.getParticipants();
		if ((participants.isEmpty() && actor.equals(user))
				|| participants.contains(actor)) {
			// verifie si user n'apartient pas déjà à participants
			if(!participants.contains(user)) {
				participants.add(user);
			}else{
				return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: User y apartient deja !"));
			}
		} else {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Not allowed to add user!"));
		}
		moduleRepository.save(module);
		return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
	}

	@DeleteMapping("/{id}/participants/{userid}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> deleteUser(Principal principal, @PathVariable long id, @PathVariable long userid) {
		Optional<Module> omodule = moduleRepository.findById(id);
		Optional<User> ouser = userRepository.findById(userid);
		if (!omodule.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: No such module!"));
		}
		if (!ouser.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: No such user!"));
		}

		Module module = omodule.get();
		User user = ouser.get();
		User actor = userRepository.findByUsername(principal.getName()).get();

		Set<User> participants = module.getParticipants();
		if(participants.contains(user)) {
			participants.add(user);
		}else{
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: User n'apartient pas au module !"));
		}
		moduleRepository.save(module);
		return ResponseEntity.ok(new MessageResponse("User successfully remouved from module!"));
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
					case "mod":
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
