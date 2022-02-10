import Model.Controllers.AuthController;
import Model.Documents.Cours;
import Model.Documents.Module;
import Model.Documents.Question;
import Model.Documents.Ressource;
import Model.Payload.request.LoginRequest;
import Model.Payload.request.SignupRequest;
import Model.Repositories.ModuleRepository;
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
import java.util.Optional;
import java.util.Set;

public class CourseTest extends SpringIntegration {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RessourcesRepository ressourcesRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    private static final String PASSWORD = "password";

    @Given("a cours named {string}")
    public void coursGestion(String gestion){
        Cours cours = (Cours) ressourcesRepository.findByName(gestion).
                orElse(new Cours(gestion));
        ressourcesRepository.save(cours);
    }

    @When("{string} creer cours {string}")
    public void creerCours(String username, String courseName) throws IOException {
        String token = authController.generateJwt(username, PASSWORD);
        executePost("http://localhost:8080/api/course/create/" + courseName, token);
    }

    @When("{string} adds a course with name {string} in module {string}")
    public void andACours(String username,String coursName, String moduleName) throws IOException {

        String jwt = authController.generateJwt(username, PASSWORD);
        executePut("http://localhost:8080/api/module/"+ moduleName +"/ressource/"+coursName, jwt);
    }

    @When("{string} add to {string} a text {string}")
    public void addsText(String username,String courseName, String text) throws IOException {

        String jwt = authController.generateJwt(username, PASSWORD);

        executePut("http://localhost:8080/api/course/"+ courseName +"/content/"+text, jwt);
    }

    @Then("CourseTest last request status is {int}")
    public void isRegisteredToModule(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @And("cours {string} has been added")
    public void coursExist(String courseName){
        assertTrue(ressourcesRepository.findByName(courseName).isPresent());
    }
}
