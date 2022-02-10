package Model.Documents;

import Model.User.User;

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
    @JoinTable(	name = "module_ressources)",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "ressource_id"))
    public Set<Ressource> ressources = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "module_user",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    public Set<User> users;

    public Module() {
    }

    public Module(String name) {
        this.name = name;
    }

    public void setParticipants(Set<User> participants) {
        this.users = participants;
    }

    public Long getId() {
        return id;
    }

    public Set<Ressource> getRessources() {
        return ressources;
    }

    public Set<User> getParticipants() {
        return this.users;
    }
}
