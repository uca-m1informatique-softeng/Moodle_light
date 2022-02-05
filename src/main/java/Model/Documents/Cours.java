package Model.Documents;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(	name = "Cours")
public class Cours extends Ressource{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank
    public String name;


    @ElementCollection
    @CollectionTable(name="text", joinColumns=@JoinColumn(name="text_id"))
    @Column(name="text_name")
    private Set<String>  text = new HashSet<>();


    public Cours() { }

    public Cours(String name_a) {
        super(name_a);
    }

}
