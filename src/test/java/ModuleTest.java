import Model.Controllers.AuthController;
import Model.Documents.Module;
import Model.Repositories.ModuleRepository;
import Model.Repositories.RessourcesRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ModuleTest extends SpringIntegration{

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    AuthController authController;

    private static final String PASSWORD = "password";

    /*@Given("a module named {string}")
    public void givenModule(String moduleName){
        Module module = moduleRepository.findByName(moduleName)
                .orElse(new Module(moduleName));
    }*/

    @Then("{string} gets his modules, then we get:")
    public void getModulesOfUser(String userName, List<String> content) throws IOException {
        String jwt = authController.generateJwt(userName, PASSWORD);
        executeGet("http://localhost:8080/api/" + userName + "/module/", jwt);

        String response  = EntityUtils.toString(latestHttpResponse.getEntity(), "UTF-8");
        System.out.println("reponse" + response);
        System.out.println("content " + content);

        // TODO : erreur il faut arriver a récupérer seulement les name des modules dans listTexts

        List<String> listTexts = Arrays.asList(response.subSequence(1,response.length()-1).toString().split("\""));
        listTexts.remove(0);

        System.out.println(content);
        System.out.println(listTexts);
        boolean result = true;
        for (int i = 0; i < listTexts.size()-1; i++){
            if (!content.get(i).equals(listTexts.get(i))){
                result = false;
            }
        }
        assertTrue(result);
    }
}
