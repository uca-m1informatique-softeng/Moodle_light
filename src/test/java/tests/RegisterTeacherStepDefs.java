package tests;

import Model.User.ERole;
import Model.User.User;
import Model.Documents.Module;
import Model.Controllers.AuthController;
import Model.Repositories.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTeacherStepDefs extends SpringIntegration {
    private static final String PASSWORD = "password";

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    boolean noexception =true;

    @Given("a teacher with login {string}")
    public void aTeacherWithLogin(String arg0) {
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>(){{ add(roleRepository.findByName(ERole.ROLE_TEACHER).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        userRepository.save(user);
        System.out.println("user is save in userRepository");
    }

    @Given("a student with login {string}")
    public void aStudentWithLogin(String arg0){
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<>(){{ add(roleRepository.findByName(ERole.ROLE_STUDENT).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        System.out.println("!!!!!!!!!!!!!!!!!!!!User " + user.getUsername() + " has no module " +user.getModules().isEmpty());
        userRepository.save(user);
        System.out.println("user is save in userRepository");
    }

    @And("a module named {string}")
    public void aModuleNamed(String arg0) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        for(User user : module.users){
            user.getModules().clear();
        }
        module.setParticipants(new HashSet<>());
        moduleRepository.save(module);
    }

    @And("{string} student in {string}")
    public void studentIn(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        module.getParticipants().add(userRepository.findByUsername(arg0).get());
        moduleRepository.save(module);

    }

    @When("{string} checks his module named {string}")
    public void checkParticipants(String nameUser ,String nameModule) throws IOException, JSONException {

        String jwt = authController.generateJwt(nameUser, PASSWORD);
        executeGet("http://localhost:8080/api/modules/"+nameModule+"/students",jwt);


    }





    @Then("return all users names")
    public void returnAllParticipants(List<String> studentNames) throws IOException, JSONException {


        String reponse = EntityUtils.toString(latestHttpResponse.getEntity(), "UTF-8");

        assertTrue(reponse.equalsIgnoreCase(studentNames.get(0)));
    }


    @When("{string} registers to module {string}")
    public void registersToModule(String arg0, String arg1) throws Exception {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);

        executePost("http://localhost:8080/api/modules/"+module.getId()+"/participants/"+user.getId(), jwt,null);
    }

    @Given( "{string} registers {string} to module {string}")
    public void StudentregisteredToModule(String arg0, String arg1, String arg2) throws Exception{
        Module module = moduleRepository.findByName(arg2).get();
        User user = userRepository.findByUsername(arg1).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        noexception = executePost("http://localhost:8080/api/modules/"+module.getId()+"/participants/"+user.getId(), jwt,null);
        User nuser = userRepository.findById(user.getId()).get();
        System.out.println("User" + nuser.getModules().isEmpty());
    }

    @Then("exception in request occurs")
    public void exceptionrequest(){
        assertEquals(noexception,false);
    }

    @Then("last request status is {int}")
    public void isRegisteredToModule(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Then("{string} is registered to module {string}")
    public void isRegisteredToModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        module.getParticipants().contains(user);
        assertTrue(true);
        System.out.println("User" + user.getModules().isEmpty());
    }

    @And("{string} is not registered to module {string}")
    public void isNotRegisteredToModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }
}