import Model.Controllers.AuthController;
import Model.Documents.Cours;
import Model.Documents.Module;
import Model.Documents.Ressource;
import Model.Payload.request.AddRessourceRequest;
import Model.Payload.request.AddTextRequest;
import Model.Repositories.ModuleRepository;
import Model.Repositories.RessourcesRepository;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.messages.internal.com.google.gson.Gson;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.*;

public class CourseTest extends SpringIntegration {

    @Autowired
    RessourcesRepository ressourcesRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    AuthController authController;

    private static final String PASSWORD = "password";

    @Given("a course named {string}")
    public void coursGestion(String gestion) {
        Cours cours = (Cours) ressourcesRepository.findByName(gestion).
                orElse(new Cours(gestion));
        ressourcesRepository.save(cours);
    }

    @When("{string} create course {string}")
    public void creerCours(String username, String courseName) throws IOException {
        String token = authController.generateJwt(username, PASSWORD);
        AddRessourceRequest ressourceRequest = new AddRessourceRequest();
        ressourceRequest.setName(courseName);
        Gson g = new Gson();
        String obj = g.toJson(ressourceRequest);
        executePost("http://localhost:8080/api/course", token,obj);
    }

    @And("{string} finds the course {string} is in {string}")
    public void coursExist(String username, String courseName, String moduleName) throws IOException {
        Module module = moduleRepository.findByName(moduleName).get();
        Ressource cours = ressourcesRepository.findByName(courseName).get();
        String token = authController.generateJwt(username, PASSWORD);
        Set<Ressource> result = module.getRessources();
        result.contains(cours);
        System.out.println(" RESULT " + result);
        executeGet("http://localhost:8080/api/modules/getressources/" + moduleName, token);
        String response  = EntityUtils.toString(latestHttpResponse.getEntity(), "UTF-8");
        System.out.println(" response " +response);

        List<String> ressources = Arrays.asList(response.subSequence(1,response.length()-1).toString().split(","));

    }

    @When("{string} adds a course with name {string} in module {string}")
    public void andACours(String username, String coursName, String moduleName) throws IOException {
        String jwt = authController.generateJwt(username, PASSWORD);
        executePut("http://localhost:8080/api/modules/" + moduleName + "/ressource/" + coursName, jwt,null);
    }

    @When("{string} deletes a course with name {string}")
    public void andACours(String username, String coursName) throws IOException {
        String jwt = authController.generateJwt(username, PASSWORD);
        executeDelete("http://localhost:8080/api/cours/" + coursName, jwt);
    }

    @When("{string} add to {string} a text {string}")
    public void addsText(String username, String courseName, String text) throws IOException {
        String jwt = authController.generateJwt(username, PASSWORD);
        AddTextRequest textRequest = new AddTextRequest();
        textRequest.setText(text);
        Gson g = new Gson();
        String obj = g.toJson(textRequest);
        executePut("http://localhost:8080/api/course/" + courseName, jwt,obj);
    }

    @Then("CourseTest last request status is {int}")
    public void isRegisteredToModule(int statusOK) {
        System.out.println(latestHttpResponse.getStatusLine().getStatusCode());
        assertTrue(latestHttpResponse.getStatusLine().getStatusCode() == statusOK);
    }

    @And("course {string} has been added")
    public void coursExist(String courseName) {
        assertTrue(ressourcesRepository.findByName(courseName).isPresent());
    }

    @Then("{string} gets the content of the course {string}, then we get:")
    public void contentOfCours(String username, String coursGestion, List<String> content) throws IOException {
        String token = authController.generateJwt(username, PASSWORD);
        executeGet("http://localhost:8080/api/course/" + coursGestion+ "/StudentCours/" +username, token);
        String response  = EntityUtils.toString(latestHttpResponse.getEntity(), "UTF-8");
        System.out.println("reponse" + response);
        System.out.println("content " + content);

        List<String> listTexts = Arrays.asList(response.subSequence(1,response.length()-1).toString().split(","));

        System.out.println(content);
        System.out.println(listTexts);
        boolean result = true;
        for (int i = 0; i < listTexts.size()-1; i++){

            if (!content.contains(listTexts.get(i))){
                result = false;
            }
        }
        assertTrue(result);
        assertTrue(listTexts.size() == content.size());
    }

    @When("{string} deletes the course {string}")
    public void deleteCourse(String username, String courseName) throws IOException {
        String jwt = authController.generateJwt(username, PASSWORD);
        executeDelete("http://localhost:8080/api/course/" + courseName, jwt);
    }

    @Then("course {string} does not exist")
    public void coursDoesNotExist(String courseName) {
        assertFalse(ressourcesRepository.findByName(courseName).isPresent());
    }
    @Given("a user with login {string}")
    public void a_user_with_login(String string) {
        String jwt = authController.generateJwt(string, PASSWORD);
    }

    @When("{string} checks his modules")
    public void checksModules(String string) throws IOException {
        String jwt = authController.generateJwt(string, PASSWORD);
        executeGet("http://localhost:8080/api/modules",jwt);
    }

    @Then("return all modules names")
    public void returnAllModulesNames(List<String> studentModules) throws IOException, JSONException {
        String reponse = EntityUtils.toString(latestHttpResponse.getEntity(),"UTF-8");

        JSONArray result = new JSONArray(reponse);
        ArrayList<String> resultList = new ArrayList<>();

        for (int i = 0; i < result.length(); i++) {
          resultList.add((String)result.getJSONObject(i).get("name"));
        }

        assertTrue(resultList.containsAll(studentModules));
        assertTrue(resultList.size() == studentModules.size());


    }

    @When("{string} checks his module named {string}")
    public void checkParticipants(String nameUser ,String nameModule) throws IOException, JSONException {

        String jwt = authController.generateJwt(nameUser, PASSWORD);
        executeGet("http://localhost:8080/api/modules/"+nameModule+"/students",jwt);


    }

    @Then("return all users names")
    public void returnAllParticipants(List<String> studentNames) throws IOException, JSONException {


        String reponse = EntityUtils.toString(latestHttpResponse.getEntity(),"UTF-8");

        assertTrue(reponse.equalsIgnoreCase(studentNames.get(0)));




    }

}