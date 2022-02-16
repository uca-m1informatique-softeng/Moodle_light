import Model.Controllers.AuthController;
import Model.Documents.*;
import Model.Documents.Module;
import Model.Payload.request.AddRessourceRequest;
import Model.Payload.request.CreateQuestionRequest;
import Model.Repositories.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.gson.Gson;
import io.cucumber.messages.internal.com.google.gson.JsonParser;
import org.apache.commons.logging.Log;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static Model.Documents.EQuestion.*;
import static org.junit.jupiter.api.Assertions.*;

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
           moduleRepository.save(module);
       }
    }

    @Given("a questionnaire named {string}")
    public void coursGestion(String QuestionnaireName){
        if(!ressourcesRepository.existsByName(QuestionnaireName)){
            Questionnaire questionnaire = new Questionnaire(QuestionnaireName);
            ressourcesRepository.save(questionnaire);
        }
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
   public void userCreateTextQuestion(String user_a, String questionType, String enonce_a , String answer_a) throws IOException {
       CreateQuestionRequest textQuestionRequest = new CreateQuestionRequest();
       textQuestionRequest.setEnonce(enonce_a);
       textQuestionRequest.setReponse(answer_a);
       textQuestionRequest.setQuestionType(TEXT);
       Gson g = new Gson();
       String s = g.toJson(textQuestionRequest);
       String jwt = authController.generateJwt(user_a, PASSWORD);
       executePost("http://localhost:8080/api/question",jwt,s);
   }

   @When("user {string} creates {string} question with content {string}, answers {string} and with answer {int}")
   public void userCreateQcmQuestion(String user_a, String questionType, String enonce_a , String possibleanswer,int rep) throws IOException {
       CreateQuestionRequest textQuestionRequest = new CreateQuestionRequest();
       textQuestionRequest.setEnonce(enonce_a);
       textQuestionRequest.listeEnonces_= possibleanswer;
       textQuestionRequest.reponseQcm = rep;
       textQuestionRequest.setQuestionType(QCM);
       Gson g = new Gson();
       String s = g.toJson(textQuestionRequest);
       String jwt = authController.generateJwt(user_a, PASSWORD);
       executePost("http://localhost:8080/api/question",jwt,s);
    }

    @When("user {string} creates {string} multi question with content {string}, answers {string} and with answer")
    public void userCreateMultiQcmQuestion(String user_a, String questionType, String enonce_a , String possibleanswer, List<Integer> content) throws IOException  {
        CreateQuestionRequest textQuestionRequest = new CreateQuestionRequest();
        textQuestionRequest.setEnonce(enonce_a);
        textQuestionRequest.listeEnonces_= possibleanswer;
        int[] lrep = content.stream().mapToInt(i->i).toArray();
        textQuestionRequest.reponsesMultiples = lrep;
        textQuestionRequest.setQuestionType(CHOIXMULTIPLE);
        Gson g = new Gson();
        String s = g.toJson(textQuestionRequest);
        String jwt = authController.generateJwt(user_a, PASSWORD);
        executePost("http://localhost:8080/api/question",jwt,s);
    }

    @When("user {string} add question {string} to {string}")
    public void addquestiontoquestionaire(String username, String questionenonce, String questionairename) throws IOException{
        String jwt = authController.generateJwt(username, PASSWORD);
        Optional<Question> oquestion = questionRepository.findByEnonce(questionenonce);
        if(oquestion.isEmpty())return;
        long questionid = oquestion.get().id;
        System.out.println("questionid : " + questionid);
        executePut("http://localhost:8080/api/questionnaire/"+questionairename+"/question/"+questionid,jwt,null);
    }

    @Then("{string} finds question {string} is in questionaire {string}")
    public void isquestioninQuestionaire(String username, String questionenonce,String questionairename) throws IOException {
        String token = authController.generateJwt(username, PASSWORD);
        Question question = questionRepository.findByEnonce(questionenonce).get();

        executeGet("http://localhost:8080/api/questionnaire/"+username+"/questionnaires/"+questionairename, token);

    }


   @Then("Question with content {string} exist")
    public void questionExistByEnone(String enonce){
        assertTrue(questionRepository.findByEnonce(enonce).isPresent());
   }

    @And("{string} adds questionnaire {string} to module {string}")
    public void addsQuestionToModule(String username, String questionnaireName, String moduleName) throws IOException {
        String jwt = authController.generateJwt(username, PASSWORD);
        System.out.println(" BEGIN PUT MODULE ");
        executePut("http://localhost:8080/api/modules/"+moduleName+"/ressource/"+questionnaireName,jwt,null);
        System.out.println(" END PUT MODULE ");

    }

    @Then("{string} finds questionnaire {string} is in module {string}")
    public void findsQuestionnaireIsInModule(String username, String questionairename, String moduleName) throws IOException, JSONException {
        String jwt = authController.generateJwt(username, PASSWORD);
        ArrayList<String> lstRessource = (ArrayList<String>) executeGetReturnObject("http://localhost:8080/api/modules/getressources/"+moduleName, jwt);
        boolean ressourcesFinded = false ;
        System.out.println("DEBUG FUNC ACTUAL"+lstRessource);
        for (String str :
                lstRessource) {
            try {

                System.out.println("DEBUG FUNC ACTUAL: str :"+ str);
                JSONArray jsonArr = new JSONArray(str);

                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    System.out.println(jsonObj.getString("name"));
                    if(jsonObj.getString("name").equals(null)){
                        assertTrue(false);
                    }
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            System.out.println("Out : "+str);
/*
           // obj= new JSONObject(str);
            System.out.println("Value of name : " + obj.get("name"));

            if(g.fromJson(str,Ressource.class).name.equals(questionairename)){
                if(ressourcesFinded){// finde multiples identical ressource

                    assertTrue(false);
                }
                else{
                    ressourcesFinded=true;
                }
            }*/
        }
        //assertTrue(ques.module.name == moduleName);
        assertTrue(ressourcesFinded);
    }

    @When("{string} sends a get request for questionnaire {string}")
    public void sendsAGetRequestForQuestionnaire(String username, String questionairename) throws IOException {
        String token = authController.generateJwt(username, PASSWORD);
        executeGet("http://localhost:8080/api/questionnaire/"+username+"/"+questionairename, token);
        System.out.println(" end get quest ");
    }

    @Then("{string} gets the questionnaire with name {string}")
    public void getsTheQuestionnaireWithName(String arg0, String arg1) throws IOException {
        String responseQuestionnaire  = EntityUtils.toString(latestHttpResponse.getEntity(), "UTF-8");
        List<String> listOfQuestions = Arrays.asList(responseQuestionnaire.subSequence(1,responseQuestionnaire.length()-1).toString().split(","));
        System.out.println("LIST OF QUES " + responseQuestionnaire);
    }

    @When("user {string} answer {string} with {string}")
    public void answerQuestionText(String user_a, String enonce_a , String answer_a) throws IOException {
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

    @When("user {string} answer {string} with {int}")
    public void answerQuestionQcm(String user_a, String enonce_a , int answer_a) throws IOException {
        Question question = questionRepository.findByEnonce(enonce_a).get();
        Reponse reponse = new Reponse();
        reponse.typeReponse = QCM;
        reponse.username = user_a;
        reponse.reponseQcm = answer_a;
        Gson g = new Gson();
        String s = g.toJson(reponse);
        String jwt = authController.generateJwt(user_a, PASSWORD);
        System.out.println(question.id);
        executePut("http://localhost:8080/api/question/answer/"+question.id,jwt,s);
        System.out.println(latestHttpResponse.getStatusLine().getStatusCode());
    }

    @When("user {string} answer multi {string} with")
    public void answerQuestionMultiQcm(String user_a, String enonce_a , List<Integer> content) throws IOException{
        Question question = questionRepository.findByEnonce(enonce_a).get();
        int[] lrep = content.stream().mapToInt(i->i).toArray();
        Reponse reponse = new Reponse();
        reponse.typeReponse = CHOIXMULTIPLE;
        reponse.username = user_a;
        reponse.reponsesMultiples = lrep;
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
        System.out.println("find question reponses" + question.reponses.isEmpty());
        boolean find = false;
        for (Reponse rep:question.reponses) {
            System.out.println("sol " + rep.username + "my name " + name);
            if(rep.username.equals(name)){
                find =true;
            }
        }
        assertTrue(find);
    }

    @Then("user {string} validate {string} and get {int} points")
    public void userValidateAndGetPoints(String username, String questionnaireName, int points) throws IOException {
        String jwt = authController.generateJwt(username, PASSWORD);
        int validation = (int) executeGetReturnObject("http://localhost:8080/api/questionnaire/"+username+"/validate/"+questionnaireName, jwt);
        //String response  = EntityUtils.toString(latestHttpResponse.getEntity(), "UTF-8");
        System.out.println("REPONSE " + validation);
    }
}
