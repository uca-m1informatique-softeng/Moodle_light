import java.util.ArrayList;

public class ActionsManager {
    ArrayList<Module> modules;

    public ActionsManager() {
        modules = new ArrayList<>();
    }

    public void createModule(String name, String id){
        modules.add(new Module(name,id));
    }

    public void addElementToModule(String id, String titel, String text){
        for(Module module:modules){
            if(module.id == id){
                module.ressources.add();
            }
        }
    }

    public void addQuestionaireToModule(String id, String titel, String text){
        for(Module module:modules){
            if(module.id == id){
                module.ressources.add(new Questionnaire());
            }
        }
    }

    public void modifyTextModule(String id, String ){
        for(Module module:modules){
            if(module.id == id){

            }
        }
    }



}
