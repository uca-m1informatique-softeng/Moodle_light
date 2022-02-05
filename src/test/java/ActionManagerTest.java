
import Model.Documents.Cours;
import Model.Documents.Module;
import Model.User.User;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActionManagerTest {
    Module m1;
    Module m2;
    User marcel;
    User jean;
    Cours cours1;
    Cours cours2;
    User std1;
    User std2;
    //ActionsManager actionsManager = new ActionsManager();

    @Given("a module of name {string} with module id {string}")
    public void giveAModule(String moduleName, String moduleId){
        m1 = new Module(moduleName);
    }

    @Given("a teacher of name {string} and with teacher id {string}")
    public void giveATeacher(String name, String id){
        marcel = new User(name,name + ".fr","06");
    }

    @Given("a course of name {string} and with course id {string}")
    public void giveACourse(String name, String id){
        cours1 = new Cours(name);
    }

    @Given("a student of name {string} and with student id {string}")
    public void giveAStudent(String name, String id){
        std1 = new User(name,name + ".fr","06");
    }

    @And("a module of name {string} with module id {string}")
    public void andModule(String moduleName, String moduleId){
        m2 = new Module(moduleName);
    }

    @And("a teacher of name {string} and with teacher id {string}")
    public void andTeacher(String teacherName, String teacherId){
        jean = new User(teacherName,teacherName + ".fr","06");
    }

    @And("a course of name {string} and with course id {string}")
    public void andCourse(String courseName, String courseId){
        cours2 = new Cours(courseName);
    }

    @And("a student of name {string} and with student id {string}")
    public void andStudent(String studentName, String studentId){
        std2 = new User(studentName, studentName + ".fr",studentId);
    }

    @When("{string} creates the course {string} in the module {string}")
    public void WhenCreateCourse(String teacherName, String courseName, String moduleId){
        //actionsManager.addCourse(moduleId, courseName);
    }

    @When("{string} creates the questionnaire {string} in the module {string}")
    public void WhenCreateQuestionnaire(String teacherName, String questionnaireName, String moduleId){
        //actionsManager.addQuestionnaireToModule(moduleId, questionnaireName);
    }

    @When("{string} add the question {string} : {string} to the questionnaire {string}, in the module {string}")
    public void WhenAddingAQuestionToAQuestionnaire(String teacherName, String question, String answer, String questionnaireName, String moduleName){
        //actionsManager.addQuestToQuestionaire(moduleName, questionnaireName, question, answer);
    }

    @When("{string} register to the course {string}")
    public void WhenRegisterToCourse(String teacherName, String courseName){
        //actionsManager.assigneTeacher(courseName, teacherName);
        //actionsManager.setTeacher(courseName, teacherName);
    }

    @Then("there is the course {string} in the right module")
    public void ThenCourse(String name){

    }

    @Then("there is the questionnaire {string} in the right module")
    public void ThenQuestionnaire(String name){
        //assertEquals(, );
    }

    @Then("there is the question {string} : {string} in the questionnaire {string}")
    public void ThenAddQuestionToQuestionnaire(String question, String answer, String questionnaireName){

    }

    @Then("there is the text {string} in the course {string}")
    public void ThenAddTextToCourse(String texte, String courseName){

    }
}