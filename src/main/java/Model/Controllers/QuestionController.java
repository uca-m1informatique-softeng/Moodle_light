package Model.Controllers;


import Model.Documents.Module;
import Model.Documents.Question;
import Model.Documents.Questionnaire;
import Model.Documents.Ressource;
import Model.Payload.response.MessageResponse;
import Model.Repositories.QuestionRepository;
import Model.Repositories.RessourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module/questionaire")
public class QuestionController {
    @Autowired
    RessourcesRepository ressourcesRepository;
    @Autowired
    QuestionRepository questionRepository;
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

}
