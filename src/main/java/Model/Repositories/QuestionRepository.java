package Model.Repositories;

import Model.Documents.Module;
import Model.Documents.Question;
import Model.Documents.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface QuestionRepository  extends JpaRepository<Question, Long> {

    Optional<Question> findById(Long name);

    Optional<Question> findByEnonce(String enonce_);
}
