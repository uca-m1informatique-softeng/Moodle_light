package Model.Documents;

import Model.user.Teacher;

import java.util.ArrayList;

public class Cours extends Ressource {
    public ArrayList<String> text;

    public Cours(String name_a, String id) {
        super(name_a,id);
    }
}
