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
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

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

    private RestTemplate restTemplate;


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

    @And("Cours {string} is in {string}")
    public void coursExist(String courseName, String moduleName){
        Module module = moduleRepository.findByName(moduleName).get();
        Ressource cours = ressourcesRepository.findByName(courseName).get();

        assertTrue(module.getRessources().contains(cours));
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

    @Then("CourseTest last request status is {int} or {int}")
    public void isRegisteredToModule(int statusOK, int statusServerError) {
        System.out.println(latestHttpResponse.getStatusLine().getStatusCode());
        System.out.println(statusOK);
        System.out.println(statusServerError);

        assertTrue(latestHttpResponse.getStatusLine().getStatusCode() == statusOK || latestHttpResponse.getStatusLine().getStatusCode() == statusServerError);
    }

    @And("cours {string} has been added")
    public void coursExist(String courseName){
        assertTrue(ressourcesRepository.findByName(courseName).isPresent());
    }

    @Then("{string} gets the content of the course {string}, then we get:")
    public void contentOfCours(String username, String coursGestion, List<String> content) throws IOException {
        String token = authController.generateJwt(username, PASSWORD);
        List<String> listTexts = (List<String>) executeGetReturnObject("http://localhost:8080/api/course/"+ coursGestion, List.class, token);

        System.out.println(content);

        System.out.println(listTexts);
        assertTrue(listTexts == content);
    }
}