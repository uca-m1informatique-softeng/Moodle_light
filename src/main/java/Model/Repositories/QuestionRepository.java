package Model.Repositories;

import Model.Documents.Question;
import Model.Documents.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository  extends JpaRepository<Question, Long> {
    Optional<Question> findById(String name);
}
