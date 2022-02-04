package Model.Documents;

import Model.user.Teacher;

import java.util.ArrayList;
import java.util.Set;

public class Cours extends Ressource {
    public Set<String> text;
    public Teacher teacher;

    public Cours(String name_a, String id) {
        super(name_a,id);
    }

    public void setTeacher(Teacher t){
        this.teacher = t;
    }
}
