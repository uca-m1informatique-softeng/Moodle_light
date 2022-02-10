package Model.Controllers;


import Model.Documents.*;
import Model.Payload.response.MessageResponse;
import Model.Repositories.QuestionRepository;
import Model.Repositories.ReponsesRepository;
import Model.Repositories.RessourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module/questionaire")
public class QuestionController {
    @Autowired
    RessourcesRepository ressourcesRepository;

    @Autowired
    QuestionRepository questionRepository ;

    @Autowired
    ReponsesRepository reponsesRepository ;

    @PutMapping("/{id}/question/{ressourceid}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addRessource(Principal principal, @PathVariable long id, @PathVariable long questionid){
        Optional<Ressource> oressource = ressourcesRepository.findById(id);
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
        if(!ressources.contains(ressource)) {
            ressources.add(question);
        }else{
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ressource y apartient deja !"));
        }
        ressourcesRepository.save(ressource);
        return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
    }


    @DeleteMapping("/{id}/question/{ressourceid}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteQuestion(Principal principal, @PathVariable long id, @PathVariable long questionid){
        Optional<Ressource> oressource = ressourcesRepository.findById(id);
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
            ressources.remove(question);
        }else{
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Non trouver question dans questionaire y apartient deja !"));
        }
        ressourcesRepository.save(ressource);
        return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
    }


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
    @GetMapping("/api/{idStudent}/module/questions")
    public List<Question> getQuestions(){

        return questionRepository.findAll();

    }



    /**
     * Read - Get a question  of  a student
     * @return - An course object
     *
     *
     * à ajouter:
     * verif that the student have acces to the courses in this question
     *
     *
     *
     */
    @GetMapping("/api/{idStudent}/module/questions/{idQuestion}")
    public Optional<Question> getQuestion(final Long idQuestion){

        return questionRepository.findById(idQuestion);

    }


    /***
     *
     *Les étudiants peuvent saisir une réponse à une question
     *
     * @return
     */

    @PostMapping("/api/{idStudent}/module/questions/{idQuestion}")
    public Reponse getQuestion(@RequestBody Reponse reponse){
        Reponse savedReponse = reponsesRepository.save(reponse);
        return savedReponse;

    }


    /***
     *
     *Les étudiants peuvent saisir une réponse à une question
     *
     * @return
     */

    @PostMapping("/api/{idStudent}/module/questions/{idQuestion}")
    public Reponse answerQuestion(@RequestBody Reponse reponse){
        Reponse savedReponse = reponsesRepository.save(reponse);
        return savedReponse;

    }

    /**
     *
     * @param idQuestion
     * @param idAnswer
     * @param reponse
     * @return
     *
     * Les étudiants peuvent modifier une réponse à une question
     */

    @PutMapping("/api/{idStudent}/module/questions/{idQuestion}/{idAnswer}")
    public Reponse updateAnswer(final Long idQuestion,final Long idAnswer,@RequestBody Reponse reponse) {
        Optional<Reponse> r = reponsesRepository.findById(idAnswer);
        if(r.isPresent()) {
            Reponse currentReponse = r.get();

            String contenu = reponse.getContenu();
            if(contenu != null) {
                currentReponse.setContenu(contenu);;
            }
            reponsesRepository.save(currentReponse);
            return currentReponse;
        } else {
            return null;
        }
    }









}
