package Model.Controllers;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import Model.Payload.request.LoginRequest;
import Model.Payload.request.SignupRequest;
import Model.Payload.response.JwtResponse;
import Model.Payload.response.MessageResponse;
import Model.Repositories.RoleRepository;
import Model.Repositories.UserRepository;
import Model.Security.jwt.JwtUtils;
import Model.Security.services.UserDetailsImpl;
import Model.User.ERole;
import Model.User.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import Model.User.Role;
import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    private Authentication authentication;

    public String generateJwt(String userName, String password) {
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }


    @PostMapping("/a")
    public ResponseEntity<?> authenticateUser(@RequestBody SignupRequest signupRequest){

        return ResponseEntity.ok(new MessageResponse("finded!" + signupRequest.getUsername()));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateuser( @RequestBody LoginRequest loginRequest) {

        String jwt = generateJwt(loginRequest.getUsername(), loginRequest.getPassword());
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        System.out.println("found" + signUpRequest.getUsername() +signUpRequest.getPassword());
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        System.out.println("verifier");
        // Create new user's account
        User user = createUser(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getRole());
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deletUser( @PathVariable long id) {
        System.out.println("passer ksldjlqj,qlks,dlkqd");
        Optional<User>ouser = userRepository.findById(id);
        if (!ouser.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such module!"));
        }
        User user = ouser.get();
        userRepository.delete(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
