import Model.User.ERole;
import Model.User.Role;
import Model.User.User;
import Model.Documents.Module;
import Model.Controllers.AuthController;
import Model.Repositories.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

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
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_TEACHER).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        userRepository.save(user);
        System.out.println("user is save in userRepository");
    }


    @Given("a student with login {string}")
    public void aStudentWithLogin(String arg0){
        User user = userRepository.findByUsername(arg0).
                orElse(new User(arg0, arg0 + "@test.fr", encoder.encode(PASSWORD)));
        user.setRoles(new HashSet<Role>(){{ add(roleRepository.findByName(ERole.ROLE_STUDENT).
                orElseThrow(() -> new RuntimeException("Error: Role is not found."))); }});
        userRepository.save(user);
        System.out.println("user is save in userRepository");
    }

    @And("a module named {string}")
    public void aModuleNamed(String arg0) {
        Module module = moduleRepository.findByName(arg0).orElse(new Module(arg0));
        module.setParticipants(new HashSet<>());
        moduleRepository.save(module);
    }

    @When("{string} registers to module {string}")
    public void registersToModule(String arg0, String arg1) throws Exception {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);

       executePost("http://localhost:8080/api/module/"+module.getId()+"/participants/"+user.getId(), jwt);
    }

    @Given( "{string} registers {string} to module {string}")
    public void StudentregisteredToModule(String arg0, String arg1, String arg2) throws Exception{
        Module module = moduleRepository.findByName(arg2).get();
        User user = userRepository.findByUsername(arg1).get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        noexception = executePost("http://localhost:8080/api/module/"+module.getId()+"/participants/"+user.getId(), jwt);
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
    }

    @And("{string} is not registered to module {string}")
    public void isNotRegisteredToModule(String arg0, String arg1) {
        Module module = moduleRepository.findByName(arg1).get();
        User user = userRepository.findByUsername(arg0).get();
        assertFalse(module.getParticipants().contains(user));
    }
}