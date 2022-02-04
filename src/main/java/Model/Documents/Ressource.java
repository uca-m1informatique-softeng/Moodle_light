package Model.Documents;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(	name = "Ressource")
public class Ressource {
    @NotBlank
    public String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Ressource(){}
    public Ressource(String name){
        this.name = name;
    }
}
