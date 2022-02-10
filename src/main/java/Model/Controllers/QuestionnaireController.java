package Model.Controllers;


import Model.Documents.*;
import Model.Payload.response.MessageResponse;
import Model.Repositories.QuestionnaireRepository;
import Model.Repositories.RessourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/questionaire")
public class QuestionnaireController {

    @Autowired
    QuestionnaireRepository  questionnaireRepository ;

    @Autowired
    RessourcesRepository ressourcesRepository ;

    /**
     * Read - Get all questions of a questionnaire of  a student
     * @return - An Iterable object of Questions
     * *
     *
     * Les utilisateurs peuvent connaitre la liste des questions
     * à ajouter:
     * verif that the student have acces to the list of questions  in this module
     *
     */
    @GetMapping("/api/{idStudent}/module/questionnaires/{idQuestionnaire}")
    public Optional<Questionnaire> getQuestionnaire(final Long idQuestionnaire){

        return questionnaireRepository.findById(idQuestionnaire) ;
    }

    /***
     * valider un questionnaire
     * @return
     */
    @GetMapping("{username}/validate/{idQuestionnaire}")
    public int validateQuestionnaire(@PathVariable String name, @PathVariable String userName ){
        Optional<Questionnaire> oquestionnaire = questionnaireRepository.findByName(name);
        if(!oquestionnaire.isPresent()){
            return -1;
        }
        Questionnaire questionnaire = oquestionnaire.get();
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

    public Reponse findReponse(Question question, String name){
        for (Reponse reponse :question.reponses) {
            if(reponse.username == name){
                return reponse;
            }
        }
        return null;
    }


    @PostMapping("create/{name}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> creerQuestionnaire(Principal principal, @PathVariable String name){
        Ressource questionnaire = ressourcesRepository.findByName(name).
                orElse(new Questionnaire());

        ressourcesRepository.save(questionnaire);
        return ResponseEntity.ok(new MessageResponse("Questionnaire successfully created"));
    }
}
