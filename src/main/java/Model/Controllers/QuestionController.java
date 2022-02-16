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
    @GetMapping("/question/{idQuestion}")
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

    /**
     * Get question of questionaire
     * @return ResponseEntity
     * @param idQuestion
     * @return ResponseEntity
     */
    @GetMapping("/{idQuestion}")
    public ResponseEntity<?> getQuestionById(@PathVariable final long idQuestion){
        Optional<Question> oquestion = questionRepository.findById(idQuestion);
        if(oquestion.isEmpty()){
            return  ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource or username!"));
        }
        Question question = oquestion.get();

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
        System.out.println("je passe dans createQuestion");
        Question questionToAdd;
        if (questionRepository.existsByEnonce(createQuestionRequest_a.listeEnonces_)) {
            return ResponseEntity.ok(new MessageResponse("La question existe deja"));
        }
        switch (createQuestionRequest_a.getQuestionType()){
            case TEXT:
                questionToAdd = new Question(createQuestionRequest_a.getEnonce(),createQuestionRequest_a.getReponse());
                break;
            case QCM:
                questionToAdd = new Question(
                        createQuestionRequest_a.getEnonce(),
                        createQuestionRequest_a.listeEnonces_,
                        createQuestionRequest_a.reponseQcm
                );
                break;
            case CHOIXMULTIPLE:
                System.out.println("length" + createQuestionRequest_a.reponsesMultiples.length);
                questionToAdd = new Question(
                        createQuestionRequest_a.getEnonce(),
                        createQuestionRequest_a.listeEnonces_,
                        createQuestionRequest_a.reponsesMultiples
                );
                break;
            default:
                //rajouter plus tard program python string
                questionToAdd = null;
        }
        System.out.println("question to add" + questionToAdd);
        if (questionToAdd != null){
            if(questionRepository.existsByEnonce(questionToAdd.enonce)) {
                System.out.println("return ok");
                return ResponseEntity.ok(new MessageResponse("Question existed before"));
            }
            System.out.println("je passe dans save");
            questionRepository.save(questionToAdd);
            return ResponseEntity.ok(new MessageResponse("Question successfully "));
        }
        else {
            System.out.println("return bad request question");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }
    }



    /**
     * Les étudiants peuvent soummetre une réponse à une question
     * @param reponse
     * @param idquestion
     * @return
     */
    @PutMapping("/answer/{idquestion}")
    public ResponseEntity<?> updateAnswer(@Valid @RequestBody Reponse reponse,@PathVariable long idquestion) {
        System.out.println("Try add answer");
        Optional<Question> oquestion = questionRepository.findById(idquestion);
        if (!oquestion.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Question doesn't exist "));
        }
        Question question = oquestion.get();
        System.out.println("reponse demande: " + reponse.username);
        if(reponse.typeReponse != question.typeQuestion) {
            System.out.println("wrong type");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Given reponse is not same type of questioon  "));
        }
        for (Reponse reponses:question.reponses) {
            System.out.println(reponses.username);
            if(reponses.username.equals(reponse.username)) {
                reponses.reponsesMultiples = reponse.reponsesMultiples;
                reponses.reponseText = reponse.reponseText;
                reponses.reponseQcm = reponse.reponseQcm;
                question.reponses.add(reponse);
                return ResponseEntity.ok(new MessageResponse("Reponse a eter mis a jour "));
            }
        }
        reponsesRepository.save(reponse);
        question.reponses.add(reponse);
        /*System.out.println("reponse " + reponse.getId());
        for (Reponse reponse1:question.reponses) {
            System.out.println(reponse1.getId());
        }
        System.out.println(question.reponses.contains(reponse));
        System.out.println("passe ici2");

         */
        try {
            questionRepository.save(question);

        }catch(Exception e){
            System.out.println(e);
        }

        System.out.println("savec");
        System.out.println("question reponses" + questionRepository.findByEnonce(question.enonce).get().reponses.isEmpty());
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
       if(questToDel.isEmpty()) {
           return  ResponseEntity
                   .badRequest()
                   .body(new MessageResponse("Error: No such ressource!"));
       }
       questionRepository.delete(questToDel.get());
       return new ResponseEntity<>("The question id :"+ id+ " Has been succesfully deleted", HttpStatus.OK);
    }
}