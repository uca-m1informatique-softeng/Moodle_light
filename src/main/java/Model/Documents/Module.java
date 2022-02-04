package Model.Documents;

import Model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(	name = "modules")
public class Module {
    @NotBlank
    public String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "ressources_module",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "ressource_id"))
    public Set<Ressource> ressources = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_module",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    public Set<User> user;

    public Module() {
    }

    public Module(String name) {
        this.name = name;
    }


}
