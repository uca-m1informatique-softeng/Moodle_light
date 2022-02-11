package Model.Documents;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(	name = "Reponse")
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    public String username;
    @NotBlank
    public String typeReponse;
    @NotBlank
    @Size(max = 120)
    public  int [] reponsesMultiples;
    @NotBlank
    @Size(max = 120)
    public  int reponseQcm;
    @NotBlank
    @Size(max = 120)
    public  String reponseText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reponse rep = (Reponse) o;
        return id.equals(rep.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
