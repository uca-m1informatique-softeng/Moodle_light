import Model.Controllers.AuthController;
import Model.Repositories.UserRepository;
import Model.User.User;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Optional;

public class DeleteTest extends SpringIntegration{

    AuthController authController = new AuthController();
    @Autowired
    UserRepository userRepository;

    private static final String PASSWORD = "password";

    @When("{string} delete {string}")
    public void  deletePerson(String arg0, String arg1) throws IOException {
        Optional<User> ouser = userRepository.findByUsername(arg0);
        User user = ouser.get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePost( "http://localhost:8080/api/auth/"+user.getId(), jwt);
    }
}
