package Model.Controllers;


import Model.Documents.Cours;
import Model.Documents.Question;
import Model.Documents.Questionnaire;
import Model.Documents.Ressource;
import Model.Payload.response.MessageResponse;
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
@RequestMapping("/api/module/course")
public class CourseController {
    @Autowired
    RessourcesRepository ressourcesRepository;
    @PutMapping("/{id}/question/{text}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addRessource(Principal principal, @PathVariable long id, @PathVariable String text){
        Optional<Ressource> oressource = ressourcesRepository.findById(id);
        if (!oressource.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }
        Ressource ressource = oressource.get();
        Cours cours;
        if(ressource.getClass().equals(Cours.class)){
            cours = (Cours)ressource;
        }else{
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ressource n'est pas un cours!"));
        }
        Set<String> textes = cours.text;
        if(!textes.contains(text)) {
            textes.add(text);
        }else{
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ressource y apartient deja !"));
        }
        ressourcesRepository.save(ressource);
        return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
    }


    @DeleteMapping("/{id}/text/{text}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deletRessource(Principal principal, @PathVariable long id, @PathVariable String text){
        Optional<Ressource> oressource = ressourcesRepository.findById(id);
        if (!oressource.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }
        Ressource ressource = oressource.get();
        Cours cours;
        if(ressource.getClass().equals(Cours.class)){
            cours = (Cours)ressource;
        }else{
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ressource n'est pas un cours!"));
        }
        Set<String> textes = cours.text;
        if(textes.contains(text)) {
            textes.remove(text);
        }else{
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Text ne apartient pas a Cours !"));
        }
        ressourcesRepository.save(ressource);
        return ResponseEntity.ok(new MessageResponse("User successfully added to cours!"));
    }


}
