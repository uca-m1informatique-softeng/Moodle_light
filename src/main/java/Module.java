import java.util.ArrayList;

public class Module {
    String name;
    String id;
    ArrayList<Ressource> ressources = new ArrayList<Ressource>();

    public Module(String name, String id) {
        this.name = name;
        this.id = id;
    }


}
