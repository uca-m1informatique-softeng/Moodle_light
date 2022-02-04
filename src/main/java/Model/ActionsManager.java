package Model;

import Model.Documents.*;
import Model.Documents.Module;

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

    ///////////////////       Teacher methodes       ///////////////////

    public void addQuestionnaireToModule(String moduleId, String title){
        Module module = findModule(moduleId);
        module.ressources.add(new Questionnaire(title,getid()));
    }

    public void addCourse(String moduleId, String name){
        Module module = findModule(moduleId);
        module.ressources.add(new Cours(name));
    }

    public void addtextToCourse(String moduleId, String textId, String text){
        Module module = findModule(moduleId);
        Ressource ressource = findResource(textId, module.ressources);
        if(ressource.getClass().equals(Cours.class)){
            ((Cours)ressource).text.add(text);
        }
    }

    /**
     * trouve le modul puis le questionaire du module correct
     * @param modulid
     * @param ressourceid
     * @param enoncer
     * @param reponse
     */
    public void addQuestToQuestionaire(String modulid,String ressourceid, String enoncer, String reponse){
        Module module = findModule(modulid);
        Ressource ressource = findResource(ressourceid,module.ressources);
        if(ressource.getClass().equals(Questionnaire.class)){
            //((Questionnaire)ressource).addQuestion(new Question(enoncer,));
        }
    }

    /**
     * set the teacher of a course
     */
    public void setTeacher(String course, String teacher){

    }

    /**
     * Assigne un teacher a un module
     * @param modulid
     * @param teacherid
     */
    public void assigneTeacher(String modulid, String teacherid){
        Module module = findModule(modulid);
        module.teachers.add(findTeacher(teacherid));
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



    ///////////////////       Student methodes       ///////////////////


    /**
     * renvois la liste des nom id des modules apartenant un teacher ou student
     * @param personId
     * @return
     */
    public ArrayList<String> getmodulesSuscriped(String personId){
        ArrayList<String> personmodules = new ArrayList<>();
        for (Module module:modules) {
            for(Teacher teacher: module.teachers){
                if(teacher.id == personId){
                    personmodules.add(module.id);
                }
            }
            for(Student student: module.students){
                if(student.id == personId){
                    personmodules.add(module.name +" "+module.id);
                }
            }
        }
        return personmodules;
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
