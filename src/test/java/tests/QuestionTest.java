package tests;

import Model.Documents.Question;
import io.cucumber.java.en.Given;

import io.cucumber.java.en.Then;
import org.junit.Assert;

public class QuestionTest {
    Question question_;

    @Given("A question built with a enonce {string} with a list of ennonce {string} and a reponse {int} ")
    public void giveAQuestion(String enonce_a, String listReponse_a, int validRep){
        question_= new Question(enonce_a,listReponse_a,validRep);
    }
    @Then("The type of reponse is {string} ")
    public void TheQuestionTypeIs(String type_a){
        Assert.assertEquals(question_.typeQuestion, type_a);
    }

}