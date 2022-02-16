package Model.Controllers;

import Model.Documents.Ressource;
import Model.Security.jwt.JwtUtils;
import Model.User.User;
import Model.Documents.Module;
import Model.Repositories.*;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.JsonArray;
import io.cucumber.messages.internal.com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import Model.Payload.response.MessageResponse;


import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;


/**
 *		Description commande
 *
 * Get /api/modules								:	return module
 * GET /api/modules/who/{modulename}   			:	return the name of the teacher of module
 * GET /api/modules/{nameUser}					:   return the modules of a User
 * GET /api/modules/ressources/{modulename}		:	return the ressources a module
 *
 * PUT /api/modules/{name}/ressource/{name2} 	:	rajoute ressource dans un Module
 * POST /api/modules/{id}/participants/{userid} :	inscrit user dans module
 *
 * DELETE /api/modules/{id}/participants/{userid}:	enleve un user d'un Module
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/modules")
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


	//////////////////////        GET     //////////////////////


	/**
	 * renvois le module
	 * @param modulename
	 * @return module
	 */
	@GetMapping("/{modulename}")
	public String getModule(@PathVariable String modulename) {
		return null;
	}

	/**
	 * renvois le Teacher du module : modulename
	 * @param modulename
	 * @return Teacher
	 */
	@GetMapping("/who/{modulename}")
	public ArrayList<String> getPersonneModule(@PathVariable String modulename) {
		ArrayList<String> data = new ArrayList<>();
		Optional<Module> omodule =	moduleRepository.findByName(modulename);
		if(!omodule.isPresent()){
			return null;
		}
		Module module = omodule.get();
		data.add("la personne connectée est " );
		return data ;

	}

	/**
	 * renvois les ressources de user : nameUser
	 * @param nameUser
	 * @return ensemble de ressource
	 */
	@GetMapping(value="ressources/{nameUser}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getressourcesofmodules(@PathVariable String nameUser){
		ArrayList<String> userModules = new ArrayList<>();
		Optional<User> ouser = userRepository.findByUsername(nameUser);
		if (!ouser.isPresent()) {
			return "{}";
		}
		User user= ouser.get();

		Set<Module> modules = user.getModules();

		for (Module module:modules) {
			userModules.add("module name : "+module.name + "| id : " + module.id);
		}
		Gson g = new Gson();
		return g.toJson(userModules);
	}


	/**
	 * Renvois les ressources d'un module specifique
	 * @param modulename
	 * @return	ensemble de ressource
	 */
	@GetMapping(value = "/getressources/{modulename}",produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('TEACHER')")
	public String getRessources(@PathVariable String modulename) throws JSONException {
		ArrayList<String> strings = new ArrayList<>();
		JSONArray ressourceArray = new JSONArray();
		Optional<Module> omodule = moduleRepository.findByName(modulename);
		JSONObject elem ;
		if (!omodule.isPresent()) {
			strings.add("Error: No such module!");
			return "{}";
		}
		Module module = omodule.get();
		for (Ressource rsrc :
			module.ressources) {
			elem = new JSONObject();
			elem.put("name",rsrc.name);
			elem.put("id", rsrc.id);
			ressourceArray.put(elem);
		}
		return ressourceArray.toString();
	}



	//////////////////////      Post  PUT     //////////////////////


	/**
	 * rajoute une ressource
	 * @param name
	 * @param name2
	 * @return ResponseEntity
	 */
	@PutMapping("/{name}/ressource/{name2}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> addRessource(@PathVariable String name, @PathVariable String name2){
		Optional<Module> omodule = moduleRepository.findByName(name);
		Optional<Ressource> oressource = ressourcesRepository.findByName(name2);
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
			ressource.module = module;
		}else{
			return ResponseEntity
					.ok()
					.body(new MessageResponse("Ressource y apartient deja !"));
		}
		moduleRepository.save(module);
		return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
	}

	/**
	 * rajoute user: userid to module : id
	 * @param principal
	 * @param id
	 * @param userid
	 * @return ResponseEntity
	 */
	@PostMapping("/{id}/participants/{userid}")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?> addUser(Principal principal, @PathVariable long id, @PathVariable long userid) {
		System.out.println("passe ici");
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
		System.out.println("passe ici 2");
		Set<User> participants = module.getParticipants();
		if ((participants.isEmpty() && actor.equals(user)) ||
			participants.contains(actor)) {
			System.out.println("passe ici 3");
			// verifie si user n'apartient pas déjà à participants
			if(!participants.contains(user)) {
				System.out.println("passe ici 4");
				participants.add(user);
				user.getModules().add(module);
			}else{
				return ResponseEntity.ok(new MessageResponse("is created before"));
			}
		} else {
		return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Not allowed to add user!"));
		}
		System.out.println("User realtion creer");
		moduleRepository.save(module);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
	}


	//////////////////////        Delete     //////////////////////

	/**
	 * enleve user : userid  from module : id
	 * @param principal
	 * @param id
	 * @param userid
	 * @return ResponseEntity
	 */
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
			user.getModules().remove(module);
			participants.remove(user);
		}else{
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: User n'apartient pas au module !"));
		}
		moduleRepository.save(module);
		return ResponseEntity.ok(new MessageResponse("User successfully remouved from module!"));
	}
}
