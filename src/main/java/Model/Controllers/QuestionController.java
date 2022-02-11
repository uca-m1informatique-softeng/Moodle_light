package Model.Controllers;


import Model.Documents.*;
import Model.Payload.request.CreateQuestionRequest;
import Model.Payload.response.MessageResponse;
import Model.Repositories.QuestionRepository;
import Model.Repositories.ReponsesRepository;
import Model.Repositories.RessourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module/question")
public class QuestionController {
    @Autowired
    RessourcesRepository ressourcesRepository;

    @Autowired
    QuestionRepository questionRepository ;

    @Autowired
    ReponsesRepository reponsesRepository ;

    @PutMapping("/name/repondre/question/{quetionid}")
    public ResponseEntity<?> addRessource(String reponse, @PathVariable String name, @PathVariable long questionid){
        Optional<Question> question = questionRepository.findById(questionid);
        return  ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: No such ressource!"));
    }

    /**
     * Method for a teacher to create a question
     * @param createQuestionRequest_a
     * @return
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createQuestion(@Valid @RequestBody CreateQuestionRequest createQuestionRequest_a){
        Question questionToAdd = null ;

        Optional<Question> oquestion = questionRepository.findByEnonce(createQuestionRequest_a.getEnonce());
        if (oquestion.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Question Already exist"));
        }
        switch (createQuestionRequest_a.getQuestionType()){
            case "text":
                questionToAdd= new Question(createQuestionRequest_a.getEnonce(),createQuestionRequest_a.getReponse());
                break;
            default:
        }
        if (questionToAdd != null ) {
            questionRepository.save(questionToAdd);
            return ResponseEntity.ok(new MessageResponse("Question successfully "));
        }
        else
            return  ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error: No such ressource!"));
    }

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



    @DeleteMapping("/{id}/delquestion/{ressourceid}")
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
    @GetMapping("/get/questions/{idQuestion}")
    public Optional<Question> getQuestion(final Long idQuestion){

        return questionRepository.findById(idQuestion);

    }


    /***
     *
     *Les étudiants peuvent saisir une réponse à une question
     *
     * @return
     */

    @PostMapping("/api/module/savereponse/{idQuestion}")
    public Reponse saverep(@RequestBody Reponse reponse){
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
     * @return
     *
     * Les étudiants peuvent soummetre une réponse à une question
     */

    @PutMapping("/answer/{id question}")
    public ResponseEntity<?> updateAnswer(@RequestBody Reponse reponse,@PathVariable final long idquestion) {
        Optional<Question> oquestion = questionRepository.findById(idquestion);
        if (!oquestion.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Question doesn't exist "));
        }
        Question question = oquestion.get();
        for (Reponse reponses:question.reponses) {
            if(reponses.username == reponse.username) {
                question.reponses.remove(reponses);
            }
        }
        if(reponse.typeReponse != question.typeQuestion) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Given reponse is not same type of questioon  "));
        }

        question.reponses.add(reponse);
        return ResponseEntity.ok(new MessageResponse("Question successfully submited "));
    }

    /**
     * Get for all modules Id
     * @return
     */
    @GetMapping(value="/a", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> listQuestion(){
        List<Question> questions = questionRepository.findAll();
        if(questions.isEmpty()){
            return  ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }
        else{
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteQuestion(@PathVariable long id) {
        Optional<Question> questToDel = questionRepository.findById(id);
        System.out.println("==========================================");
        System.out.println(questToDel.get().typeQuestion);
        System.out.println("==============DEBUG============================");
       if(questToDel.isEmpty()) {

           return  ResponseEntity
                   .badRequest()
                   .body(new MessageResponse("Error: No such ressource!"));

       }
       else{
           questionRepository.delete(questToDel.get());

           return new ResponseEntity<>("The question id :"+ id+ " Has been succesfully deleted", HttpStatus.OK);
       }
    }












}
