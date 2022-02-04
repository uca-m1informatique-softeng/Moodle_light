package Model.Documents;

import Model.user.Student;
import Model.user.Teacher;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
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
    @JoinTable(	name = "teachers_module",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "teachers_id"))
    public Set<Teacher> teachers;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "students_module",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "students_id"))
    public Set<Student> students;

    public Module() {
    }


}
