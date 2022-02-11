package Model.Controllers;

import Model.Documents.Cours;
import Model.Documents.Module;
import Model.Documents.Question;
import Model.Documents.Ressource;
import Model.Payload.response.MessageResponse;
import Model.Repositories.ModuleRepository;
import Model.Repositories.RessourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 *      Description commande :
 *
 *  GET /api/ressource/{nameRessource}              :   return name id et type of ressource
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/ressource")
public class RessourceController {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    RessourcesRepository ressourcesRepository;

    //////////////////////        GET     //////////////////////

    /**
     * get name | id | type of ressource
     * @param nameRessource
     * @return
     */
    @GetMapping("/{nameRessource}")
    public String getRessource(@PathVariable String  nameRessource){
        Optional<Ressource> oressource = ressourcesRepository.findByName(nameRessource);
        if (!oressource.isPresent()) {
            return null;
        }
        Ressource ressource = oressource.get();
        return ressource.name + " | " + ressource.id + " | " + ressource.getClass().toString();
    }

}
