package Model.Controllers;


import Model.Documents.*;
import Model.Payload.response.MessageResponse;
import Model.Repositories.QuestionRepository;
import Model.Repositories.RessourcesRepository;
import Model.Repositories.UserRepository;
import Model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 *
 *          Description commande
 *
 * GET  /api/questionnaire/{username}/questionnaires/{nameQuestionnaire}    :   return question de questionaires
 * GET  /api/questionnaire/{username}/validate/{idQuestionnaire}            :   evalue questionaire et return points
 *
 * POST /api/questionnaire/{name}     :    creer questionaire
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

    /***
     * valider un questionnaire
     * @return int represent reponse correcte dans questionaire
     */
    @GetMapping("{username}/validate/{idQuestionnaire}")
    public int validateQuestionnaire(@PathVariable String name, @PathVariable String userName ){
        Optional<Ressource> oquestionnaire = ressourcesRepository.findByName(name);
        if(!oquestionnaire.isPresent()||oquestionnaire.get().getClass().equals(Questionnaire.class)){
            return -1;
        }
        Questionnaire questionnaire = (Questionnaire) oquestionnaire.get();
        int reponsevalide =0;
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
     * create the questionaire with name : name
     * @param name
     * @return ResponseEntity
     */
    @PostMapping("/{name}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> creerQuestionnaire(@PathVariable String name){
        Ressource questionnaire = ressourcesRepository.findByName(name).
                orElse(new Questionnaire());

        ressourcesRepository.save(questionnaire);
        return ResponseEntity.ok(new MessageResponse("Questionnaire successfully created"));
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
