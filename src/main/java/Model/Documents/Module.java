package Model.Documents;

import Model.user.Student;
import Model.user.Teacher;

import java.util.ArrayList;

public class Module {
    public String name;
    public String id;
    public ArrayList<Ressource> ressources = new ArrayList<Ressource>();
    public ArrayList<Teacher> teachers = null;
    public ArrayList<Student> students = new ArrayList<>();

    public Module(String name, String id) {
        this.name = name;
        this.id = id;
    }


}
