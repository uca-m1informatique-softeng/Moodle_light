package Model.Documents;

import java.util.ArrayList;

public class Questionnaire extends Ressource {
    ArrayList<Question> ListeQuestions = new ArrayList<>();
    public  Questionnaire(String name){
        super(name);
    }
    public void addQuestion(Question quest_a){
        this.addQuestion(quest_a);
    }
    public void displayQuestion(){
        for (Question quest : this.ListeQuestions){
            System.out.println("Model.Model.Documents.Question "+ quest.toString());

        }
    }
}
