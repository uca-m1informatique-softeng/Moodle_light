package Documents;

import Documents.Question;

import java.util.ArrayList;

public class Questionnaire extends Ressource {
    ArrayList<Question> ListeQuestions = new ArrayList<>();
    public  Questionnaire(String name){
        super(name);
    }
    public void addQuestion(Question quest_a){
        this.addQuestion(quest_a);
    }
    public void displayQuestionnaire(){
        System.out.println("Questionnaire : "+ this.name);
        for (Question quest : this.ListeQuestions){
            System.out.println("Question "+ quest.getEnonce());
            System.out.println("Reponses : ");

        }
    }
}
