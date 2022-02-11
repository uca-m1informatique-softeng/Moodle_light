package Model.Documents;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@DiscriminatorValue("C")
public class Cours extends Ressource{
    @ElementCollection
    @CollectionTable(name="cour_text", joinColumns=@JoinColumn(name="cour_id"))
    @Column(name="text_name")
    public List<String>  text = new ArrayList<>();

    public Cours() { }

    public Cours(String name_a) {
        super(name_a);
    }

}
