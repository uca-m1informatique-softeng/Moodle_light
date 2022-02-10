package Model.Controllers;


import Model.Documents.Questionnaire;
import Model.Repositories.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/module/questionaire")
public class QuestionnaireController {

    @Autowired
    QuestionnaireRepository  questionnaireRepository ;


    /**
     * Read - Get all questions of a questionnaire of  a student
     * @return - An Iterable object of Questions
     * *
     *
     * Les utilisateurs peuvent connaitre la liste des questions
     * à ajouter:
     * verif that the student have acces to the list of questions  in this module
     *
     */
    @GetMapping("/api/{idStudent}/module/questionnaires/{idQuestionnaire}")
    public Optional<Questionnaire> getQuestionnaire(final Long idQuestionnaire){

        return questionnaireRepository.findById(idQuestionnaire) ;

    }

    /***
     * valider un questionnaire
     * @param idQuestionnaire
     * @return
     */


    @PostMapping("/api/{idStudent}/module/questionnaires/{idQuestionnaire}")
    public Optional<Questionnaire> validateQuestionnaire(final Long idQuestionnaire){

        return questionnaireRepository.findById(idQuestionnaire) ;

    }


}
