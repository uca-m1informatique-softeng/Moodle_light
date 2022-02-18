package Model.Controllers;


import Model.Documents.*;
import Model.Payload.request.AddRessourceRequest;
import Model.Payload.response.JwtResponse;
import Model.Payload.response.MessageResponse;
import Model.Repositories.*;
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
    NoteRepository noteRepository;

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
    @GetMapping("/{username}/{nameQuestionnaire}")
    @PreAuthorize("hasRole('TEACHER')")
    public String getsModule(@PathVariable String username, @PathVariable String nameQuestionnaire){
        System.out.println(" GET QUESTIONNAIRE ");
        Optional<Ressource> oquestionaire = ressourcesRepository.findByName(nameQuestionnaire);
        Questionnaire questionnaire = (Questionnaire) oquestionaire.get();
        if( questionnaire != null ){
            Gson g = new Gson();
            return g.toJson(questionnaire);
        } else {
            System.out.println("DEBUG :: QUESTIONNAIRE doesn't exist ");
            return "{}";
        }
    }


    /**
     *
     */
    @GetMapping("reponse/{username}/{questionairename}")
    @PreAuthorize("hasRole('TEACHER')")
    public int getreponseStudent(@PathVariable String username , @PathVariable String questionairename){
        System.out.println("get passe ici");
        Optional<Ressource> oquestionnaire = ressourcesRepository.findByName(questionairename);
        if(!oquestionnaire.isPresent()&&oquestionnaire.get().getClass().equals(Questionnaire.class)){
            return -1;
        }
        System.out.println("Questionaire trouver");
        Questionnaire questionnaire = (Questionnaire) oquestionnaire.get();
        Optional<User> ouser = userRepository.findByUsername(username);
        if(!ouser.isPresent()){
            return -1;
        }
        User user = ouser.get();
        for (Note note:questionnaire.notes){
            System.out.println("note" + note.username);
            if(user.getUsername().equals(note.username)){
                return note.note;
            }
        }
        return -1;
    }

    /***
     * valider un questionnaire
     * @return int represent reponse correcte dans questionaire
     */
    @GetMapping("{username}/validate/{questionairename}")
    public int validateQuestionnaire(@PathVariable String username , @PathVariable String questionairename ){
        System.out.println("Valideate function");
        Optional<Ressource> oquestionnaire = ressourcesRepository.findByName(questionairename);
        if(!oquestionnaire.isPresent()&&oquestionnaire.get().getClass().equals(Questionnaire.class)){
            return -1;
        }
        System.out.println("Questionaire trouver");
        Questionnaire questionnaire = (Questionnaire) oquestionnaire.get();
        Optional<User> ouser = userRepository.findByUsername(username);
        if(!ouser.isPresent()){
            return -1;
        }
        User user = ouser.get();
        for (Note note:questionnaire.notes) {
            if(note.username.equals(username)){
                return -1;
            }
        }
        int reponsevalide = 0;
        for (Question question:questionnaire.ListeQuestions) {
            System.out.println(question.enonce);
            Reponse reponse = findReponse(question,username);
            if(reponse == null){
                System.out.println("reponse de youser not found");
                return -1;
            }
            System.out.println("test reponce");
            if(question.reponse(reponse)) {
                System.out.println("reponse valide");
                reponsevalide++;
            }
            System.out.println("finis test");
        }
        System.out.println("return object");
        Note note = new Note(reponsevalide, username);
        noteRepository.save(note);
        questionnaire.notes.add(note);
        ressourcesRepository.save(questionnaire);
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
            System.out.println(reponse.username);
            if(reponse.username.equals(name)){
                System.out.println("reponse found");
                return reponse;
            }
        }
        System.out.println("reponse not found");
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
    public ResponseEntity<?> addRessource(@PathVariable String questionarename, @PathVariable final long questionid){
        System.out.println("rajoute Question");
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
