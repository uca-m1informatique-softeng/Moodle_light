package Model.Repositories;

import Model.Documents.Module;
import Model.Documents.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
}
