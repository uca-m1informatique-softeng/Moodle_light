package Model.Controllers;
import Model.Documents.Cours;
import Model.Documents.Module;
import Model.Documents.Ressource;
import Model.Payload.request.AddRessourceRequest;
import Model.Payload.request.AddTextRequest;
import Model.Payload.response.MessageResponse;
import Model.Repositories.ModuleRepository;
import Model.Repositories.RessourcesRepository;
import Model.Repositories.UserRepository;
import Model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 *              Description commande :
 *
 *  GET  /api/course/{namecours}/StudentCours/{namestudent}     :   Get the textes of a cours if user is in module of cours
 *  GET   /api/course/{coursename}                              :   Get cour text
 *
 *  POST /api/course                                            :   Creer cour in ressourceRepository
 *  PUT  /api/course/{courname}                                 :   Rajoute du texte dans cour
 *
 *  DELETE  /api/course/{courname}                              :   Delete un cour
 *  DELETE  /api/course/{courname}/text/{text}                  :   Delete du text d'un cour
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    RessourcesRepository ressourcesRepository;

    @Autowired
    UserRepository userRepository;
    private ModuleRepository moduleRepository;


    //////////////////////      GET     //////////////////////






    /**
     * Read - Get all textes of  a cours of a student
     * @param namecours
     * @param namestudent
     * @return - An Iterable object of courses full filled
     *
     *
     * Les utilisateurs peuvent connaitre la liste des textes sur le cours ou ils sont inscrits
     *
     */




    @GetMapping("{namecours}/StudentCours/{namestudent}")
    public List<String> getCourses(@PathVariable String namecours,@PathVariable String namestudent){
        System.out.println("In function Get courses");
        Optional<Ressource> ocours = ressourcesRepository.findByName(namecours);
        Optional<User> ouser = userRepository.findByUsername(namestudent);

        // not a coursfind userfind or cour is not a cour
        if(!ocours.isPresent()||!ouser.isPresent()||!ocours.get().getClass().equals(Cours.class)){
            System.out.println("In function Get courses SOMETHING IS WRONG in ocours || ouser \\ class cours ");
            return null;
        }
        Cours cours = (Cours) ocours.get();
        User user = ouser.get();
        if(!cours.module.users.contains(user)){
            return null;
        }
        return cours.text;
    }

    /**
     * Get the content of a course
     * @param coursename
     * @return List<String>
     */
    @GetMapping(value = "/{coursename}")
    public List<String> getCourseTexts(@PathVariable String coursename){
        Optional<Ressource> courseRes = ressourcesRepository.findByName(coursename);
        if (!courseRes.isPresent()) {
            return new ArrayList<>();
        }
        Cours cours= (Cours) courseRes.get();
        return cours.text;
    }

    //////////////////////      Post Put     //////////////////////


    /**
     * Create a cours in ressourceRepository
     * @return http reponse info
     */
    @PostMapping("")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> creerCours(@Valid @RequestBody AddRessourceRequest addRessourceRequest){
        if(ressourcesRepository.existsByName(addRessourceRequest.getName())){
            return ResponseEntity.ok(new MessageResponse("user existe!"));
        }
        ressourcesRepository.save(new Cours(addRessourceRequest.getName()));
        return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
    }

    /**
     * rajoute text a cour
     * @param courname
     * @param addTextRequest
     * @return http reponse info
     */
    @PutMapping("/{courname}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> modifierCours(@Valid @RequestBody AddTextRequest addTextRequest, @PathVariable String courname) throws Exception{
        // V??rifier si ce resource existe
        Optional<Ressource> oressource = ressourcesRepository.findByName(courname);
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
        //Si le text pass?? dans les param??tre existe dans le text dans le cours on renvoie une erreur

        String text = addTextRequest.getText();
        try{
            cours.text.contains(text);
        }catch(Exception e){
            System.out.println(e);
        }
        if(!cours.text.contains(text)) {
            try{
                cours.text.add(text);
            }catch (Exception e){
                System.out.println(e);
            }

        }else{
            return ResponseEntity
                    .ok(new MessageResponse("A eter dejat rajouter!"));
        }
        ressourcesRepository.save(ressource);
        return ResponseEntity.ok(new MessageResponse("User successfully added to module!"));
    }

    //////////////////////      Delete     //////////////////////
    /**
     * Delete a cour
     * @param courname
     * @return ResponseEntity
     */
    @DeleteMapping("/{courname}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> delete(@PathVariable String courname){
        System.out.println("delete cours 1");
        Optional<Ressource> oressource = ressourcesRepository.findByName(courname);
        if(!oressource.isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No such ressource!"));
        }

        Ressource ressource = oressource.get();
        if(ressource.module!=null && ressource.module.ressources.contains(ressource)){
            ressource.module.ressources.remove(ressource);
        }
        ressourcesRepository.delete(ressource);
        System.out.println("delete cours 3");
        return ResponseEntity.ok(new MessageResponse("User successfully delete cours!"));
    }

    /**
     * Delete a text in cour
     * @param courname
     * @return ResponseEntity
     */
    @DeleteMapping("/{courname}/text/{text}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deletRessource(@PathVariable String courname, @PathVariable String text){
        System.out.println("Cours est Delete");
        Optional<Ressource> oressource = ressourcesRepository.findByName(courname);
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
                    .ok(new MessageResponse("A eter dejat creer!"));
        }
        ressourcesRepository.save(cours);
        return ResponseEntity.ok(new MessageResponse("User successfully added to cours!"));
    }

  }
