import Model.Controllers.AuthController;
import Model.Documents.Module;
import Model.Documents.Questionnaire;
import Model.Payload.request.CreateQuestionRequest;
import Model.Repositories.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionnaireTest extends SpringIntegration {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RessourcesRepository ressourcesRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    private static final String PASSWORD = "password";

    @And("a questionnaire named {string} in the module {string} does not exist")
    public void uniqueQuestionnaire(String QuestionnaireName, String moduleName){
       Module module = moduleRepository.findByName(moduleName).get();
       Questionnaire quest = questionnaireRepository.findByName(QuestionnaireName).get();
       if( module.ressources.contains(quest))
       {
           module.ressources.remove(quest);
       }
    }

    @Given("a questionnaire named {string}")
    public void coursGestion(String QuestionnaireName){
        Questionnaire questionnaire = (Questionnaire) ressourcesRepository.findByName(QuestionnaireName).
                orElse(new Questionnaire(QuestionnaireName));
        ressourcesRepository.save(questionnaire);
    }


    @When("{string} creer questionnaire {string}")
    public void creerQuestionnaire(String username, String questionnaireName) throws IOException {
        String token = authController.generateJwt(username, PASSWORD);
        executePost("http://localhost:8080/api/questionnaire/create/" + questionnaireName, token);
    }


    @Then("QuestionnaireTest last request status is {int} or {int}")
    public void isRegisteredToModule(int status, int status2) {
        boolean testValid = false ;
        if(
            latestHttpResponse.getStatusLine().getStatusCode() == status ||
            latestHttpResponse.getStatusLine().getStatusCode() == status2
        ){
            assertTrue(true);
        }
        else {
            assertTrue(false);
        }
    }


    @And("Questionnaire {string} has been added")
    public void questionnairExist(String courseName){
        assertTrue(questionnaireRepository.findByName(courseName).isPresent());
    }
   @When("user {string} create {string} question with enonce {string} and with answer {string}")
   public void userCreateTextQuestion(String user_a, String questionType, String enonce_a , String answer_a) throws UnsupportedEncodingException {
       CreateQuestionRequest textQuestionRequest = new CreateQuestionRequest();
       textQuestionRequest.setEnonce(enonce_a);
       textQuestionRequest.setReponse(answer_a);
       textQuestionRequest.setQuestionType("text");
       Gson g = new Gson();
       String s = g.toJson(textQuestionRequest);
       String jwt = authController.generateJwt(user_a, PASSWORD);
       executePostObj("http://localhost:8080/api/module/question/create",s,jwt);
       System.out.println(latestHttpResponse.getStatusLine().getStatusCode());

   }
   @Then("Question with enonce {string} exist")
    public void questionExistByEnone(String enonce){
        assertTrue(questionRepository.findByEnonce(enonce).isPresent());
   }
}
