import Model.Controllers.AuthController;
import Model.Documents.Cours;
import Model.Documents.Question;
import Model.Documents.Ressource;
import Model.Payload.request.LoginRequest;
import Model.Payload.request.SignupRequest;
import Model.Repositories.RessourcesRepository;
import Model.Repositories.RoleRepository;
import Model.Repositories.UserRepository;
import Model.User.ERole;
import Model.User.Role;
import Model.User.User;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.messages.internal.com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CourseTest extends SpringIntegration {
    Cours cours;
    User teacher;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RessourcesRepository ressourcesRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    private static final String PASSWORD = "password";

    @Given("a teacher with username {string}")
    public void givenATeacher(String username) throws IOException {
        SignupRequest signup = new SignupRequest();
        signup.setUsername(username);
        signup.setEmail(username + "@gmail.com");
        signup.setPassword(encoder.encode(PASSWORD));
        Set roles = new HashSet<ERole>();
        roles.add(ERole.ROLE_TEACHER);
        signup.setRole(roles);
        Gson g = new Gson();
        String s = g.toJson(signup);

        executePostObj("http://localhost:8080/api/auth/signup",s);
    }

    @And("a resource of type cours with id {long}")
    public void andACours(Long id){
        cours = new Cours();
        cours.name = "Gestion";
        cours.id = id;
    }

    @And("and list of texts:")
    public void withTextList(List<String> listoftexts){
        cours.text = listoftexts;
    }

    @When("{string} adds text {string} to the cours")
    public void addsText(String arg0,String text) throws IOException {

        User user = userRepository.findByUsername(arg0).get();
        LoginRequest signin = new LoginRequest();
        signin.setUsername(user.getUsername());
        signin.setPassword(user.getPassword());
        Gson g = new Gson();
        String s = g.toJson(signin);

        boolean courseAdded = executePost("http://localhost:8080/api/module/course/add/" + cours.name, s);
        assertTrue(courseAdded);

        Cours registeredCours = (Cours) ressourcesRepository.findByName(cours.name).get();
        Long coursID = registeredCours.id;
        System.out.println(registeredCours);
        boolean textAdded = executePost("http://localhost:8080/api/module/course/"+ coursID +"/content/"+text+"/", s);
        assertTrue(textAdded);
    }

}
