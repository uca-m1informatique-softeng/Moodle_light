package Model.Documents;

import javax.persistence.*;
import java.util.Set;

//@Entity
public class Cours extends Ressource{
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Set<String> text;


    public Cours() { }

    public Cours(String name_a) {
        super(name_a);
    }

}
