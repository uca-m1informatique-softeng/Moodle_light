package Model.Documents;

import javax.persistence.*;
import java.util.*;

@Entity
@DiscriminatorValue("C")
public class Cours extends Ressource{
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="cour_text", joinColumns=@JoinColumn(name="cour_id"))
    @Column(name="text_name")
    public List<String>  text = new ArrayList<>();

    public Cours() {}

    public Cours(String name_a) {
        super(name_a);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cours cours = (Cours) o;
        return id.equals(cours.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
