
import Model.ActionsManager;
import Model.Documents.Cours;
import Model.Documents.Module;
import Model.user.Student;
import Model.user.Teacher;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActionManagerTest {
    Teacher marcel;
    Teacher jean;
    Cours cours1;
    Cours cours2;
    Student std1;
    Student std2;
    ActionsManager actionsManager = new ActionsManager();

    @Given("a teacher of name {string} and with teacher id {string}")
    public void giveATeacher(String name, String id){
        marcel = new Teacher(name, id);
    }

    @Given("a course of name {string} and with course id {string}")
    public void giveACourse(String name, String id){
        cours1 = new Cours(name, id);
    }

    @Given("a student of name {string} and with student id {string}")
    public void giveAStudent(String name, String id){
        std1 = new Student(name, id);
    }

    @And("a teacher of name {string} and with teacher id {string}")
    public void andTeacher(String teacherName, String teacherId){
        jean = new Teacher(teacherName, teacherId);
    }

    @And("a course of name {string} and with course id {string}")
    public void andCourse(String courseName, String courseId){
        cours2 = new Cours(courseName, courseId);
    }

    @And("a student of name {string} and with student id {string}")
    public void andStudent(String studentName, String studentId){
        std2 = new Student(studentName, studentId);
    }

    @When("{string} creates the course {string} in the module {string}")
    public void WhenCreateCourse(String teacherName, String courseName, String moduleName){
        actionsManager.createModule(moduleName, courseName);
    }

    @When("{string} creates the questionnaire {string} in the module {string}")
    public void WhenCreateQuestionnaire(String teacherName, String questionnaireName, String moduleName){
        actionsManager.addQuestionaireToModule(moduleName, questionnaireName);
    }

    @When("{string} add the question {string} : {string} to the questionnaire {string}, in the module {string}")
    public void WhenAddingAQuestionToAQuestionnaire(String teacherName, String question, String answer, String questionnaireName, String moduleName){
        actionsManager.addQuestToQuestionaire(moduleName, questionnaireName, question, answer);
    }

    @When("{string} register to the course {string}")
    public void WhenRegisterToCourse(String teacherName, String courseName){
        actionsManager.assigneTeacher(courseName, teacherName);
    }

    @Then("there is the course {string} in the right module")
    public void ThenCourse(String name){
        //assertEquals(, );
    }

    @Then("there is the questionnaire {string} in the right module")
    public void ThenQuestionnaire(String name){
        //assertEquals(, );
    }

    @Then("there is the question {string} : {string} in the questionnaire {string}")
    public void ThenAddQuestionToQuestionnaire(String question, String answer, String questionnaireName){

    }
}