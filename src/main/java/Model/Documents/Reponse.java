package Model.Documents;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(	name = "Reponse")
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    public String contenu;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
