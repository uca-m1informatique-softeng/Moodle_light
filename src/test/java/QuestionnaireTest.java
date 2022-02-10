import Model.Controllers.AuthController;
import Model.Documents.Cours;
import Model.Documents.Questionnaire;
import Model.Repositories.ModuleRepository;
import Model.Repositories.QuestionnaireRepository;
import Model.Repositories.RessourcesRepository;
import Model.Repositories.UserRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

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
    AuthController authController;

    @Autowired
    PasswordEncoder encoder;

    private static final String PASSWORD = "password";

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


    @Then("QuestionnaireTest last request status is {int}")
    public void isRegisteredToModule(int status) {
        assertEquals(status, latestHttpResponse.getStatusLine().getStatusCode());
    }

    @When("{string} adds a course with name {string} in module {string}")
    public void andACours(String username,String coursName, String moduleName) throws IOException {

        String jwt = authController.generateJwt(username, PASSWORD);
        executePut("http://localhost:8080/api/module/"+ moduleName +"/ressource/"+coursName, jwt);
    }

    @When("{string} add to {string} a text {string}")
    public void addsText(String username,String courseName, String text) throws IOException {

        String jwt = authController.generateJwt(username, PASSWORD);

        executePut("http://localhost:8080/api/course/"+ courseName +"/content/"+text, jwt);
    }

    @And("Questionnaire {string} has been added")
    public void questionnairExist(String courseName){
        assertTrue(questionnaireRepository.findByName(courseName).isPresent());
    }
}
