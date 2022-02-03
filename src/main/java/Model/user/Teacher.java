package Model.user;
import Model.ActionsManager;

public class Teacher {
    public String name;
    public String id;

    public Teacher(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName(){
        return this.name;
    }
}
