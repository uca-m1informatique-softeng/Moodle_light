import Model.Controllers.AuthController;
import Model.Payload.request.LoginRequest;
import Model.Payload.request.SignupRequest;
import Model.Repositories.UserRepository;
import Model.User.ERole;
import Model.User.Role;
import Model.User.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTest extends SpringIntegration {
    //HttpResponse latestHttpResponse;
    private static final String PASSWORD = "password";

    AuthController authController = new AuthController();
    @Autowired
    UserRepository userRepository;


    @Then("2 last request status is {int}")
    public void isRegisteredToModule(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @When("{string} with email {string} and code {string} and is {string} signup")
    public void aPersonTrySignup(String arg0,String arg1,String arg2,String arg3) throws IOException {
        SignupRequest signup = new SignupRequest();
        signup.setUsername(arg0);
        signup.setEmail(arg1);
        signup.setPassword(arg2);
        Set<String> mytype = new HashSet<String>();
        mytype.add(arg3);
        signup.setRole(mytype);
        Gson g = new Gson();
        String s = g.toJson(signup);
        executePostObj("http://localhost:8080/api/auth/signup",s);
        /*HttpPost request = new HttpPost("http://localhost:8080/api/auth/a");
        request.addHeader("content-type", "application/json");
        StringEntity entity = new StringEntity(s);
        request.setEntity(entity);
        latestHttpResponse = httpClient.execute(request);
        */
    }

    @When("{string} delete {string}")
    public void  deletePerson(String arg0, String arg1) throws IOException {
        Optional<User> ouser = userRepository.findByUsername(arg0);
        User user = ouser.get();
        String jwt = authController.generateJwt(arg0, PASSWORD);
        executePost( "http://localhost:8080/api/auth/"+user.getId(), jwt);
    }


    @When("{string} and code {string} signin")
    public void aPersonTrySignin(String arg0,String arg1) throws IOException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(arg0);
        loginRequest.setPassword(arg1);
        Gson g = new Gson();
        String s = g.toJson(loginRequest);
        executePostObj("http://localhost:8080/api/auth/signin",s);
        /*HttpPost request = new HttpPost("http://localhost:8080/api/auth/a");
        request.addHeader("content-type", "application/json");
        StringEntity entity = new StringEntity(s);
        request.setEntity(entity);
        latestHttpResponse = httpClient.execute(request);
        */
    }



}
