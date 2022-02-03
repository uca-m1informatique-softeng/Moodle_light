
import Model.ActionsManager;
import Model.Documents.Module;
import Model.user.Teacher;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActionManagerTest {
    Teacher marcel;
    ActionsManager actionsManager = new ActionsManager();

    @Given("a teacher of name {string} and with teacher id {string}")
    public void giveATeacher(String name, String id){
        marcel = new Teacher(name, id, actionsManager);
    }

    @When("{string} creates the course {string} in the module {string}")
    public void WhenCreateCourse(String teacherName, String courseName, String moduleName){
        actionsManager.addElementToModule(moduleName, courseName);
    }

    @When("{string} creates the questionnaire {string} in the module {string}")
    public void WhenCreateQuestionnaire(String teacherName, String questionnaireName, String moduleName){
        actionsManager.addElementToModule(moduleName, questionnaireName);
    }

    @When("{string} add a question to the questionnaire {string}")
    public void WhenAddingAQuestionToAQuestionnaire(String teacherName, String questionnaireName){
        //actionsManager.
    }

    @Then("there is the course {string} in the right module")
    public void ThenCourse(String name){
        //assertEquals(, );
    }

    @Then("there is the questionnaire {string} in the right module")
    public void ThenQuestionnaire(String name){
        //assertEquals(, );
    }

    @Then("there is the question {string} in the questionnaire {string}")
    public void ThenAddQuestionToQuestionnaire(String questionName, String questionnaireName){

    }
}