package Model.Documents;

import java.util.ArrayList;

public class Module {
    public String name;
    public String id;
    public ArrayList<Ressource> ressources = new ArrayList<Ressource>();

    public Module(String name, String id) {
        this.name = name;
        this.id = id;
    }


}
