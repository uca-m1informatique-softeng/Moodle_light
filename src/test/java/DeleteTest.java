import Model.Controllers.AuthController;
import Model.Repositories.UserRepository;
import Model.User.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DeleteTest extends SpringIntegration{
    @Autowired
    AuthController authController;

    @Autowired
    UserRepository userRepository;

    private static final String PASSWORD = "password";


    @Given("{string} not Existe")
    public void  notExistePerson(String arg0) throws IOException {
        Optional<User> ouser = userRepository.findByUsername(arg0);
        if(ouser.isPresent()){
            userRepository.delete(ouser.get());
        }
    }

    @When("{string} delete {string}")
    public void  deletePerson(String arg0, String arg1) throws IOException {
        Optional<User> ouser = userRepository.findByUsername(arg1);
        User user = ouser.get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executeDelete( "http://localhost:8080/api/auth/delete/"+user.getId(), jwt);
    }

    @Then("{string} is not a student")
    public void isStudent(String arg0) {
        Optional<User> ouser = userRepository.findByUsername(arg0);
        assertFalse(ouser.isPresent());
    }
}
