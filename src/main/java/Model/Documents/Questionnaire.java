package Model.Documents;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(	name = "Questionaire")
public class Questionnaire{
    @NotBlank
    public String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "ressources_module",
            joinColumns = @JoinColumn(name = "questionaire_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    public Set<Question> ListeQuestions = new HashSet<>();
    public  Questionnaire(){}

    public void addQuestion(Question quest_a){
        this.addQuestion(quest_a);
    }
    public void displayQuestion(){
        for (Question quest : this.ListeQuestions){
            System.out.println("Model.Model.Documents.Question "+ quest.toString());

        }
    }
}
