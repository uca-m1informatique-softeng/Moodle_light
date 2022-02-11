package Model.Controllers;


import Model.Documents.Cours;
import Model.Documents.Module;
import Model.Documents.Questionnaire;
import Model.Repositories.ModuleRepository;
import Model.Repositories.QuestionnaireRepository;
import Model.Repositories.UserRepository;
import Model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module/questionaire")
public class QuestionnaireController {

    @Autowired
    QuestionnaireRepository questionnaireRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    UserRepository userRepository;


    /**
     * Read - Get all questions of a questionnaire of  a student
     *
     * @return - An Iterable object of Questions
     * *
     * <p>
     * Les utilisateurs peuvent connaitre la liste des questions
     * à ajouter:
     * verif that the student have acces to the list of questions  in this module
     */
    @GetMapping("/api/{idStudent}/module/{idModule}/questionnaires/{idQuestionnaire}")
    public Optional<Questionnaire> getQuestionnaire(final Long idQuestionnaire, final Long idModule, final Long idUser) {

        if (isHaveAcces(idModule, idUser)) {
            return questionnaireRepository.findById(idQuestionnaire);

        }
        return null;


    }


    boolean isHaveAcces(Long idModule, Long idUser) {

        boolean find = false;
        Optional<Module> m = moduleRepository.findById(idModule);
        if (m.isPresent()) {
            Module moduleCurr = m.get();
            for (User participant : moduleCurr.getParticipants()) {

                if (participant.getId() == idUser) {
                    find = true;
                }

            }

        }
        return find;


    }

}
