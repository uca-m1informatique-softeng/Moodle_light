package tests;

import Model.Controllers.AuthController;
import Model.Payload.request.LoginRequest;
import Model.Payload.request.SignupRequest;
import Model.Repositories.UserRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        executePost("http://localhost:8080/api/auth/signup",null,s);
    }




    @When("user {string} with password {string} sign in")
    public void aPersonTrySignin(String arg0,String arg1) throws IOException {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(arg0);
        loginRequest.setPassword(arg1);
        Gson g = new Gson();
        String s = g.toJson(loginRequest);
        executePost("http://localhost:8080/api/auth/signin",null,s);
    }

    @Then("{string} is registered like user")
    public void registered(String arg0) throws IOException {
        assertTrue(userRepository.existsByUsername(arg0));
    }


}
