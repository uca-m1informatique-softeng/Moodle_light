package Model;

import Model.Documents.*;
import Model.Documents.Module;
import Model.user.Student;
import Model.user.Teacher;

import java.util.ArrayList;

public class ActionsManager {
    static private ArrayList<Module> modules;
    private ArrayList<Teacher> teachers;
    private ArrayList<Student> students;
    static private int id = 0;

    public ActionsManager() {
        modules = new ArrayList<>();
    }

    public void createModule(String name, String id){
        modules.add(new Module(name,id));
    }

    public void addQuestionaireToModule(String modulid, String titel){
        Module module = findModule(modulid);
        module.ressources.add(new Questionnaire(titel,getid()));
    }

    public void addTextModule(String modulid, String name, String text ){
        Module module = findModule(modulid);
        module.ressources.add(new Cours(name,text,getid()));
    }

    /**
     * trouve le modul puis le questionaire du module correct
     * @param modulid
     * @param name
     * @param enoncer
     * @param reponse
     */
    public void addQuestToQuestionaire(String modulid,String name, String enoncer, String reponse){
        Module module = findModule(modulid);
        for(Ressource ressource: module.ressources) {
            if(ressource.name == name){
                //((Questionnaire)ressource).addQuestion(new Question(enoncer,));
            }
        }
    }

    /**
     * Assigne un teacher a un module
     * @param modulid
     * @param teacherid
     */
    public void assigneTeacher(String modulid, String teacherid){
        Module module = findModule(modulid);
        module.teacher = findTeacher(teacherid);
    }

    /**
     * Assigne un student à un module from modulid and studentid
     * @param modulid
     * @param studentid
     */
    public void assigneStudent(String modulid, String studentid){
        Module module = findModule(modulid);
        module.students.add(findStudent(studentid));
    }






    ///////////////////       Help methodes       ///////////////////

    /**
     * renvois le module correspendant a modulid
     * si non renvois null.
     * @param modulid
     * @return Module
     */
    private Module findModule(String modulid){
        for(Module module:modules) {
            if (module.id == modulid){
                return module;
            }
        }
        return null;
    }
    /**
     * renvois le teacher correspendant à teahcerid
     * si non renvois null.
     * @param teacherid
     * @return Teacher
     */
    private Teacher findTeacher(String teacherid){
        for (Teacher teacher:teachers) {
            if (teacher.id == teacherid){
                return teacher;
            }
        }
        return null;
    }

    /**
     * renvois le teacher correspendant à teahcerid
     * si non renvois null.
     * @param studentid
     * @return Teacher
     */
    private Student findStudent(String studentid){
        for (Student student:students) {
            if (student.id == studentid){
                return student;
            }
        }
        return null;
    }

    /**
     * renvois la ressource correspendant à resourceid
     * si non renvois null.
     * @param resourceid
     * @return Teacher
     */
    private Ressource findResource(String resourceid, ArrayList<Ressource> ressources){
        for (Ressource ressource:ressources) {
            if (ressource.id == resourceid){
                return ressource;
            }
        }
        return null;
    }

    private String getid(){
        id = id+1;
        return String.valueOf(id);
    }


    ///////////////////       Get/Set methodes       ///////////////////

    static public ArrayList<Module> getModules(){
        return modules;
    }
}
