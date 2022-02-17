package Model.Documents;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ressource_type")
public class Ressource {
    @NotBlank
    public String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Ressource(){}

    @ManyToOne
    @JoinColumn(name="modulid")
    public Module module;

    public Ressource(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return "id : " + this.id + ", name : " + this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ressource res = (Ressource) o;
        return id.equals(res.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
