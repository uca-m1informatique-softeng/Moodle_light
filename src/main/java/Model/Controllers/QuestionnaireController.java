package Model.Controllers;


import Model.Documents.*;
import Model.Payload.request.AddRessourceRequest;
import Model.Payload.response.MessageResponse;
import Model.Repositories.ModuleRepository;
import Model.Repositories.QuestionRepository;
import Model.Repositories.RessourcesRepository;
import Model.Repositories.UserRepository;
import Model.User.User;
import io.cucumber.messages.internal.com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.Set;


/**
 *
 *          Description commande
 *
 * GET  /api/questionnaire/{username}/questionnaires/{nameQuestionnaire}    :   return question de questionaires
 * GET  /api/questionnaire/{username}/validate/{questionairename}            :   evalue questionaire et return points
 * GET /api/questionnaire/{username}/{nameQuestionnaire}/   : get the questionnaire object
 * POST /api/questionnaire    :    creer questionaire
 * PUT  /api/questionnaire/{questionarename}/question/{questionid}   :   rajoute une question dans questionaire
 * PUT api/questionnaire/{questionarename}/module/{moduleName}  : attache un module à une questionnaire
 *
 * DELETE /api/questionnaire/{name}   :    delete questionaire
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/questionnaire")
public class QuestionnaireController {

    @Autowired
    RessourcesRepository ressourcesRepository ;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    QuestionRepository questionRepository;

    //////////////////////        GET     //////////////////////

    /**
     * Get all questions of a questionnaire of a student
     * @return - An Iterable object of Questions
     *
     */
    @GetMapping("/{username}/questionnaires/{nameQuestionnaire}")
    public Set<Question> getQuestionnaire(@PathVariable String username, @PathVariable String nameQuestionnaire){
        Optional<Ressource> oquestionaire = ressourcesRepository.findByName(nameQuestionnaire);
        Optional<User> ouser = userRepository.findByUsername(username);
        if(!oquestionaire.isPresent()||!ouser.isPresent()||!oquestionaire.get().getClass().equals(Questionnaire.class)){
            return null;
        }
        Questionnaire questionnaire = (Questionnaire) oquestionaire.get();
        User user = ouser.get();
        if(!questionnaire.module.users.contains(user)){
            return null;
        }
        return questionnaire.ListeQuestions ;
    }

    /**
     * Get the questionnaire object
     * @param nameQuestionnaire
     * @return ResponseEntity
     */
    @GetMapping(value="/{username}/{nameQuestionnaire}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TEACHER')")
    public String getsModule(@PathVariable String username, @PathVariable String nameQuestionnaire){
        System.out.println(" GET QUESTIONNAIRE ");
        Optional<Ressource> oquestionaire = ressourcesRepository.findByName(nameQuestionnaire);
        Questionnaire questionnaire = (Questionnaire) oquestionaire.get();
        System.out.println("=========== DEBUG =========\n"+questionnaire.toString());
        if( questionnaire != null ){
            Gson g = new Gson();
            return g.toJson(questionnaire);
        }
        else
        {
            System.out.println("DEBUG :: QUESTIONNAIRE doesn't exist ");
            return "";
        }
        /* Optional<User> ouser = userRepository.findByUsername(username);
        if(!oquestionaire.isPresent()||!ouser.isPresent()||!oquestionaire.get().getClass().equals(Questionnaire.class)){
            return null;
        }
        System.out.println(" GET QUESTIONNAIRE 2");

        Questionnaire questionnaire = (Questionnaire) oquestionaire.get();
        User user = ouser.get();
        if(!questionnaire.module.users.contains(user)){
            System.out.println(" GET QUESTIONNAIRE 3");

            return null;
        }

        System.out.println(" GET QUESTIONNAIRE 4");
        */
    }

    /***
     * valider un questionnaire
     * @return int represent reponse correcte dans questionaire
     */
    @GetMapping("{username}/validate/{questionairename}")
    public int validateQuestionnaire(@PathVariable String userName , @PathVariable String questionairename ){
        Optional<Ressource> oquestionnaire = ressourcesRepository.findByName(questionairename);
        if(!oquestionnaire.isPresent()||oquestionnaire.get().getClass().equals(Questionnaire.class)){
            return -1;
        }

        Questionnaire questionnaire = (Questionnaire) oquestionnaire.get();
        int reponsevalide = 0;
        for (Question question:questionnaire.ListeQuestions) {
            Reponse reponse = findReponse(question,userName);
            if(reponse == null){
                return -1;
            }
            if(question.reponse(reponse)) {
                reponsevalide++;
            }
        }
        return reponsevalide;
    }

    /**
     * fonction utilitaire pour trouver la Reponse d'un user
     * @param question
     * @param name
     * @return Reponse
     */
    public Reponse findReponse(Question question, String name){
        for (Reponse reponse :question.reponses) {
            if(reponse.username == name){
                return reponse;
            }
        }
        return null;
    }


    //////////////////////        POST PUT     //////////////////////

    /**
     * save new questionaire in body
     * @return ResponseEntity
     */
    @PostMapping("")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> creerQuestionnaire(@Valid @RequestBody AddRessourceRequest addRessourceRequest){
        if(ressourcesRepository.existsByName(addRessourceRequest.getName())){
            return ResponseEntity.ok(new MessageResponse("est deja creer"));
        }
        Questionnaire questionnaire = new Questionnaire(addRessourceRequest.getName());
        ressourcesRepository.save(questionnaire);
        return ResponseEntity.ok(new MessageResponse("Questionnaire successfully created"));
    }

    /**
     * rajoute une question a un questionaire
     * @param questionarename
     * @param questionid
     * @return ResponseEntity
     */
    @PutMapping("/{questionarename}/question/{questionid}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addRessource(@PathVariable String questionarename, @PathVariable long questionid){
        Optional<Ressource> oressource = ressourcesRepository.findByName(questionarename);
        Optional<Question> oquestion = questionRepository.findById(questionid);
        if (!oressource.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }

        if (!oquestion.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such question!"));
        }
        Ressource ressource = oressource.get();
        Questionnaire questionnaire;
        if(ressource.getClass().equals(Questionnaire.class)){
            questionnaire = (Questionnaire)ressource;
        }else{
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ressource n'est pas un questionaire!"));
        }
        Question question = oquestion.get();
        Set<Question> ressources = questionnaire.ListeQuestions;
        if(ressources.contains(ressource)) {
            return ResponseEntity.ok(new MessageResponse("Question was added before!"));
        }
        //pour remonter a la source questionaire lorsqu'on suprime question ou en get
        question.questionnaire=questionnaire;
        ressources.add(question);
        ressourcesRepository.save(ressource);
        System.out.println("rajouter question a questionaire");
        return ResponseEntity.ok(new MessageResponse("User successfully added to questionaire!"));
    }


    //////////////////////        DELETE     //////////////////////


    /**
     * Delete questionaire with name : name
     * @param name
     * @return ResponseEntity
     */
    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> delete(@PathVariable String name){
        Optional<Ressource> oressource = ressourcesRepository.findByName(name);
        if(!oressource.isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }
        Ressource ressource = oressource.get();
        ressourcesRepository.delete(ressource);
        return ResponseEntity.ok(new MessageResponse("User successfully delete questionnaire!"));
    }
}
