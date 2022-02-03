package Model;

import Model.Documents.Cours;
import Model.Documents.Module;
import Model.Documents.Questionnaire;
import Model.Documents.Ressource;

import java.util.ArrayList;

public class ActionsManager {
    static private ArrayList<Module> modules;

    public ActionsManager() {
        modules = new ArrayList<>();
    }

    public void createModule(String name, String id){
        modules.add(new Module(name,id));
    }

    public void addElementToModule(String id, String title){
        for(Module module:modules){
            if(module.id.equals(id)){
                module.ressources.add(new Questionnaire(title));
            }
        }
    }

    public void addQuestionaireToModule(String id, String title){
        for(Module module:modules){
            if(module.id.equals(id)){
                module.ressources.add(new Questionnaire(title));
            }
        }
    }

    public void modifyTextModule(String id, String name, String text ){
        for(Module module:modules){
            if(module.id == id){
                module.ressources.add(new Cours(name,text));
            }
        }
    }

    static public ArrayList<Module> getModules(){
        return modules;
    }

}
