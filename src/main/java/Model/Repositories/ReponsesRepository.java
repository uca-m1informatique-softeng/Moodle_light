package Model.Repositories;

import Model.Documents.Cours;
import Model.Documents.Reponse;
import Model.Documents.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReponsesRepository extends JpaRepository<Reponse, Long> {
}
