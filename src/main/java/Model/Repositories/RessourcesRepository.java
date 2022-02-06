package Model.Repositories;

import Model.Documents.Module;
import Model.Documents.Questionnaire;
import Model.Documents.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RessourcesRepository extends JpaRepository<Ressource, Long>{
    Optional<Ressource> findByName(String name);
    Optional<Ressource> findById(String name);
}