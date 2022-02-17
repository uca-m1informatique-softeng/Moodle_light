package test;

import Model.Controllers.AuthController;
import Model.Documents.Module;
import Model.Repositories.ModuleRepository;
import Model.Repositories.QuestionRepository;
import Model.Repositories.RessourcesRepository;
import Model.Repositories.UserRepository;
import Model.User.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DeleteTest extends SpringIntegration{
    @Autowired
    AuthController authController;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    RessourcesRepository ressourcesRepository;

    private static final String PASSWORD = "password";


    @Given("{string} not exist")
    public void  notExistePerson(String arg0) throws IOException {
        Optional<User> ouser = userRepository.findByUsername(arg0);
        System.out.println(arg0);
        if(ouser.isPresent()){
            User user = ouser.get();
            for (Module module:moduleRepository.findAll()) {
                System.out.println(module.name);
                for (User user1: module.users) {
                    System.out.println(user1.getUsername());
                }

            }
            System.out.println("found in "+ user.getModules());
            for(Module module : user.getModules()){
                module.users.remove(user);
                moduleRepository.save(module);
            }
            userRepository.delete(user);
        }
    }

    @When("{string} delete {string}")
    public void  deletePerson(String arg0, String arg1) throws IOException {
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeDelete( "http://localhost:8080/api/auth/"+arg1, jwt);
    }

    @When("{string} delete questionnaire {string}")
    public void deleteQuestionaire(String arg0, String arg1) throws  IOException {
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeDelete( "http://localhost:8080/api/questionnaire/"+arg1, jwt);
    }

    @Then("{string} is not a student")
    public void isStudent(String arg0) {
        Optional<User> ouser = userRepository.findByUsername(arg0);
        assertFalse(ouser.isPresent());
    }

    @Then("deleteTest last request status is {int}")
    public void isRegisteredToModule(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Given("clean")
    public void clean(){
        /*for(Module module: moduleRepository.findAll()){
            module.users = null;
            module.ressources = null;
            moduleRepository.save(module);
        }
        questionRepository.deleteAll();
        ressourcesRepository.deleteAll();
        userRepository.deleteAll();*/
    }
}
