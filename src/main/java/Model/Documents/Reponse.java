package Model.Documents;


import org.hibernate.validator.constraints.Range;

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

    @Enumerated()
    public EQuestion typeReponse;

    public  int [] reponsesMultiples = {};

    @Range(min=0, max=20)
    public  int reponseQcm;

    @Size(max = 120)
    public  String reponseText;

    @ManyToOne
    Question question;

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
