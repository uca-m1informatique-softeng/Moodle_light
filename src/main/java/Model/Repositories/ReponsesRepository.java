package Model.Repositories;

import Model.Documents.Cours;
import Model.Documents.Reponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReponsesRepository extends JpaRepository<Reponse, Long> {
}
