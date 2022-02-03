import java.util.ArrayList;

public class ActionsManager {
    ArrayList<Module> modules;

    public ActionsManager() {
        modules = new ArrayList<>();
    }

    public void createModule(String name, String id){
        modules.add(new Module(name,id));
    }



}
