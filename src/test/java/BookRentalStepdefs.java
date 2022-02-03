import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookRentalStepdefs {

    public BookRentalStepdefs() {} // implementation des steps dans le constructeur (aussi possible dans des méthodes)

    @Given("a student of name {string} and with student id {int}")
    public void givenAStudent(String nomEtudiant, Integer noEtudiant)  // besoin de refactorer int en Integer car utilisation de la généricité par Cucumber Java 8
    {
    }

    @And("a book of title {string}")
    public void andABook(String titreLivre)  {
    }


    @Then("There is {int} in his number of rentals")
    public void thenNbRentals(Integer nbEmprunts) {

    }


    @When("{string} requests his number of rentals")
    public void whenRequestsRentals (String nomEtudiant) {
    }

    @When("{string} rents the book {string}")
    public void whenRenting(String nomEtudiant, String titreLivre)  {
    }

    @And("The book {string} is in a rental in the list of rentals")
    public void andNarrowedBook (String titreLivre){
    }

    @And("The book {string} is unavailable")
    public void andUnvailableBook(String titreLivre) {
    }

}
