

import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class EmpruntLivreStepdefs { // implements En si vos scénarios sont écrits en anglais

    public EmpruntLivreStepdefs() { }

    @Etantdonné("un etudiant de nom {string} et de noEtudiant {int}")
    public void etantDonneUnEtudiant(String nomEtudiant, Integer noEtudiant)  // besoin de refactorer int en Integer car utilisation de la généricité par Cucumber Java 8
    {
    };

    @Et("un livre de titre {string}")
    public void eUnLivre(String titreLivre) {
    }


    @Alors("Il y a {int} dans son nombre d'emprunts")
    public void alors(Integer nbEmprunts)  {
        assertEquals(1,1);
    }


    @Quand("{string} demande son nombre d'emprunt")
    public void quandDemandeNbEmprunt(String nomEtudiant) {

    }

    @Quand("{string} emprunte le livre {string}")
    public void quandEmprunte(String nomEtudiant, String titreLivre)  {
    }

    @Et("Il y a le livre {string} dans un emprunt de la liste d'emprunts")
    public void etLivreDejaEmprunte(String titreLivre) {
    }

    @Et("Le livre {string} est indisponible")
    public void etLivreDispo(String titreLivre)  {
    }


}
