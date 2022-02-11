package Model.Controllers;


import Model.Documents.*;
import Model.Payload.request.CreateQuestionRequest;
import Model.Payload.response.MessageResponse;
import Model.Repositories.QuestionRepository;
import Model.Repositories.ReponsesRepository;
import Model.Repositories.RessourcesRepository;
import Model.Repositories.UserRepository;
import Model.User.User;
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


/**
 *          Description commande
 *
 * GET /api/question/{username}/reponse/{idReponse}            :   return reponse
 * GET /api/question/{username}/question/{idQuestion}           :   return question
 *
 * POST /api/question                                           :   creer question
 * PUT  /api/question/{questionarename}/question/{questionid}   :   rajoute une question dans questionaire
 * PUT  /api/question/answer/{id question}                      :   rajoute une reponse a une question
 *
 * DELETE /api/question/{id}                                    :   delete une question
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/question")
public class QuestionController {
    @Autowired
    RessourcesRepository ressourcesRepository;

    @Autowired
    QuestionRepository questionRepository ;

    @Autowired
    ReponsesRepository reponsesRepository ;

    @Autowired
    UserRepository userRepository;


    //////////////////////        GET     //////////////////////


    /**
     * Get reponse of user
     * @return ResponseEntity
     * @param username
     * @param idReponse
     * @return ResponseEntity
     */
    @GetMapping("/{username}/reponse/{idReponse}")
    public ResponseEntity<?> getAnswer(@PathVariable String username,@PathVariable final long idReponse) {
        Optional<Reponse> oreponse = reponsesRepository.findById(idReponse);
        Optional<User> ouser = userRepository.findByUsername(username);
        if(oreponse.isEmpty()||ouser.isEmpty()){
            return  ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such reponse or username!"));
        }
        Reponse reponse = oreponse.get();
        User user = ouser.get();
        if(!(reponse.username == user.getUsername())){
            return  ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: it is not the reponse of user!"));
        }
        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }

    /**
     * Get question of questionaire
     * @return ResponseEntity
     * @param username
     * @param idQuestion
     * @return ResponseEntity
     */
    @GetMapping("/{username}/question/{idQuestion}")
    public ResponseEntity<?> getQuestion(@PathVariable String username,@PathVariable final long idQuestion){
        Optional<Question> oquestion = questionRepository.findById(idQuestion);
        Optional<User> ouser = userRepository.findByUsername(username);
        if(oquestion.isEmpty()||ouser.isEmpty()){
            return  ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource or username!"));
        }
        Question question = oquestion.get();
        User user = ouser.get();
        if(!question.questionnaire.module.users.contains(user)){
            return  ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: user is not in the module!"));
        }
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    //////////////////////        PUT POST    //////////////////////


    /**
     * Method for a teacher to create a question
     * @param createQuestionRequest_a
     * @return ResponseEntity
     */
    @PostMapping("")
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

    /**
     * rajoute une question a un questionaire
     * @param questionarename
     * @param questionid
     * @return ResponseEntity
     */
    @PutMapping("/{questionarename}/question/{questionid}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addRessource(@PathVariable String questionarename, @PathVariable long questionid){
        Optional<Ressource> oressource = ressourcesRepository.findById(questionarename);
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

    /**
     * Les étudiants peuvent soummetre une réponse à une question
     * @param reponse
     * @param idquestion
     * @return
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


    //////////////////////        DELETE    //////////////////////

    /**
     * Delete une question
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
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
       questionRepository.delete(questToDel.get());
       return new ResponseEntity<>("The question id :"+ id+ " Has been succesfully deleted", HttpStatus.OK);

    }

}
