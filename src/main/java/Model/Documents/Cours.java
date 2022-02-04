package Model.Documents;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(	name = "cours")
public class Cours extends Ressource{


    public Set<String> text;


    public Cours() { }

    public Cours(String name_a) {
        super(name_a);
    }

}
