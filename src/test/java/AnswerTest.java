import Model.Controllers.AuthController;
import Model.Documents.Question;
import Model.Documents.Reponse;
import Model.Payload.request.CreateQuestionRequest;
import Model.Repositories.ModuleRepository;
import Model.Repositories.QuestionRepository;
import Model.Repositories.RessourcesRepository;
import Model.Repositories.UserRepository;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;

import static Model.Documents.EQuestion.TEXT;
import static org.junit.jupiter.api.Assertions.*;

public class AnswerTest extends SpringIntegration {

    private static final String PASSWORD = "password";

    @Autowired
    UserRepository userRepository;

    @Autowired
    RessourcesRepository ressourcesRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;


    @When("user {string} answer {string} with {string}")
    public void userCreateTextQuestion(String user_a, String enonce_a , String answer_a) throws UnsupportedEncodingException {
        Question question = questionRepository.findByEnonce(enonce_a).get();
        Reponse reponse = new Reponse();
        reponse.typeReponse = TEXT;
        reponse.username = user_a;
        reponse.reponseText = answer_a;
        Gson g = new Gson();
        String s = g.toJson(reponse);
        String jwt = authController.generateJwt(user_a, PASSWORD);
        System.out.println(question.id);
        executePut("http://localhost:8080/api/question/answer/"+question.id,jwt,s);
        System.out.println(latestHttpResponse.getStatusLine().getStatusCode());
    }

    @Then("Answer of {string} is saved in {string}")
    public void Answersaved(String name, String enonce_a) throws UnsupportedEncodingException {
        Question question = questionRepository.findByEnonce(enonce_a).get();
        boolean find = false;
        for (Reponse rep:question.reponses) {
            System.out.println("sol " + rep.username + "my name " + name);
            if(rep.username.equals(name)){
                find =true;
            }
        }
        assertTrue(find);
    }
}
