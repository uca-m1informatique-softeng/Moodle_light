import Model.Controllers.AuthController;
import Model.Documents.EQuestion;
import Model.Documents.Module;
import Model.Documents.Questionnaire;
import Model.Payload.request.AddRessourceRequest;
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

import static Model.Documents.EQuestion.TEXT;
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
    QuestionRepository questionRepository;

    @Autowired
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    private static final String PASSWORD = "password";

    @And("a questionnaire named {string} in the module {string} does not exist")
    public void uniqueQuestionnaire(String QuestionnaireName, String moduleName){
       Module module = moduleRepository.findByName(moduleName).get();
       Questionnaire quest = (Questionnaire) ressourcesRepository.findByName(QuestionnaireName).get();
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


    @When("{string} creates questionnaire {string}")
    public void creerQuestionnaire(String username, String questionnaireName) throws IOException {
        String token = authController.generateJwt(username, PASSWORD);
        AddRessourceRequest ressourceRequest = new AddRessourceRequest(questionnaireName);
        Gson g = new Gson();
        String obj = g.toJson(ressourceRequest);
        executePost("http://localhost:8080/api/questionnaire" , token,obj);
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
        assertTrue(ressourcesRepository.findByName(courseName).isPresent());
    }

   @When("user {string} creates {string} question with content {string} and with answer {string}")
   public void userCreateTextQuestion(String user_a, String questionType, String enonce_a , String answer_a) throws UnsupportedEncodingException {
       CreateQuestionRequest textQuestionRequest = new CreateQuestionRequest();
       textQuestionRequest.setEnonce(enonce_a);
       textQuestionRequest.setReponse(answer_a);
       textQuestionRequest.setQuestionType(TEXT);
       Gson g = new Gson();
       String s = g.toJson(textQuestionRequest);
       String jwt = authController.generateJwt(user_a, PASSWORD);
       executePost("http://localhost:8080/api/question",jwt,s);
       System.out.println(latestHttpResponse.getStatusLine().getStatusCode());

   }
   @Then("Question with content {string} exist")
    public void questionExistByEnone(String enonce){
        assertTrue(questionRepository.findByEnonce(enonce).isPresent());
   }
}
