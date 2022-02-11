package Model.Controllers;
import Model.Documents.Cours;
import Model.Documents.Module;
import Model.Documents.Ressource;
import Model.Payload.response.MessageResponse;
import Model.Repositories.CoursesRepository;
import Model.Repositories.RessourcesRepository;
import Model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    RessourcesRepository ressourcesRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @PutMapping("/{name}/content/{text}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> modifierCours(Principal principal, @PathVariable String name, @PathVariable String text){
        // Vérifier si ce resource existe
        Optional<Ressource> oressource = ressourcesRepository.findByName(name);
        if (!oressource.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }
        Ressource ressource = oressource.get();
        Cours cours;
        //S'il existe et il est de type Cours on le cast a un objet cours
        if(ressource.getClass().equals(Cours.class)){
            cours = (Cours)ressource;
        }else{
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Ressource n'est pas un cours!"));
        }

        //Si le text passé dans les paramètre existe dans le text dans le cours on renvoie une erreur
        List<String> textes = cours.text;
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

    @PostMapping("create/{name}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> creerCours(Principal principal,@PathVariable String name){
        Ressource cours = ressourcesRepository.findByName(name).
                orElse(new Cours(name));

        ressourcesRepository.save(cours);
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
        List<String> textes = cours.text;
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

    @GetMapping("/{name}")
    public List<String> getCourseContent(@PathVariable String name){
        ArrayList<String> content = new ArrayList<>();
        Optional<Ressource> courseRes = ressourcesRepository.findByName(name);
        if (!courseRes.isPresent()) {
            return null;
        }
        Cours cours= (Cours) courseRes.get();

        return cours.text;
    }



    /**
     * Read - Get all courses of  a student
     * @return - An Iterable object of courses full filled
     *
     *
     * Les utilisateurs peuvent connaitre la liste des cours sur lesquels ils sont inscrits
     * à ajouter:
     * verif that the student have acces to the courses in this module
     *
     */
   @GetMapping("/api/{idStudent}/module/courses")
    public List<Cours> getCourses(){

       return coursesRepository.findAll();

   }



    /**
     * Read - Get a course  of  a student
     * @return - An course object
     *
     * Les utilisateurs peuvent connaitre la liste des cours sur lesquels ils sont inscrits
     * à ajouter:
     * verif that the student have acces to the courses in this module
     *
     *
     *
     */
    @GetMapping("/api/{idStudent}/module/courses/{idCourse}")
    public Optional<Cours> getCourse(final Long idCourse){

        return coursesRepository.findById(idCourse);

    }

    @DeleteMapping("delete/{name}")
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
        return ResponseEntity.ok(new MessageResponse("User successfully delete cours!"));
    }



}
