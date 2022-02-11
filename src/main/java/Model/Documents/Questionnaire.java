package Model.Documents;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@DiscriminatorValue("Q")
public class Questionnaire extends Ressource{

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="questionaireid")
    public Set<Question> ListeQuestions = new HashSet<>();
    public  Questionnaire(){}

    public  Questionnaire(String questName_arg){
        super(questName_arg);
        this.name = questName_arg;
    }
    public void addQuestion(Question quest_a){
        this.ListeQuestions.add(quest_a);
    }
    public void displayQuestion(){
        for (Question quest : this.ListeQuestions){
            System.out.println("Model.Model.Documents.Question "+ quest.toString());

        }
    }

}
