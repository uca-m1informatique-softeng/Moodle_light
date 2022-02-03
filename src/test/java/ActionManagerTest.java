
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

public class ActionsManagerTest {
    Teacher marcel;
    ActionsManager actionsManager = new ActionsManager();

    @Given("a teacher of name {string} and with teacher id {string}")
    public void giveATeacher(String name, String id){
        marcel = new Teacher(name, id);
    }

    @When("{string} creates the course {string} in the module {string}")
    public void WhenCreateCourse(String teacherName, String courseName, String moduleName){
        for (Module m : ActionsManager.getModules()) {
            if(m.getName().equals(moduleName)){
                m.createCourse(courseName, teacherName);
            }
        }
    }

    @When("{string} creates the questionnaire {string} in the module {string}")
    public void WhenCreateQuestionnaire(String teacherName, String QuestionnaireName, String moduleName){
        for (Module m : ActionsManager.getModules()) {
            if(m.getName().equals(moduleName)){
                m.(QuestionnaireName, teacherName);
            }
        }
    }

    @Then("There is the course {string} in the right module")
    public void ThenCourse(String name){
        //assertEquals(, );
    }

    @Then("There is the questionnaire {string} in the right module")
    public void ThenQuestionnaire(String name){
        //assertEquals(, );
    }
}