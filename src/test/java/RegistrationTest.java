import Model.Payload.request.SignupRequest;
import Model.User.ERole;
import Model.User.Role;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.springframework.http.HttpEntity;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTest extends SpringIntegration {
    //HttpResponse latestHttpResponse;
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



}
