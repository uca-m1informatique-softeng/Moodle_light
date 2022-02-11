package Model.Documents;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @Brief Classe represente une question
 */

@Entity
@Table(	name = "Question")
public class Question {
    @NotBlank
    public  String enonce="";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Size(max = 120)
    public  String listeEnonces_="";


    @ManyToOne
    public Questionnaire questionnaire;

    @Size(max = 120)
    public  String typeQuestion="";
/*    @Range(min=1, max=20)
    public  int [] reponsesMultiples={};*/
    @Range(min=0, max=20)
    public  int reponseQcm=0;
    @Size(max = 120)
    public  String reponseText="";

    public Question() {}

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_reponse",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "reponse_id"))
    public Set<Reponse> reponses = new HashSet<>();


 /**
  * Constructor question QCM
  * @param enonce_
  */
 public Question(String enonce_, String listrReponses_a, int reponse_a) {
  this.listeEnonces_ = listrReponses_a;
  this.typeQuestion="qcm";
  this.enonce = enonce_;
  this.reponseQcm =reponse_a;
 }

 public boolean reponse(Reponse reponse){
     switch(reponse.typeReponse) {
         case "qcm":
             return reponse.reponseQcm == this.reponseQcm;

         case "choixmult":
            /* if ( reponse.reponsesMultiples.length != reponse.reponsesMultiples.length)
                 return  false ;
             for (int i = 0 ; i < reponse.reponsesMultiples.length; i++){
                 if(reponse.reponsesMultiples[i] == this.reponsesMultiples[i]) {
                     return false;
                 }
             }*/
             return true;
         case "text":
             if(reponse.reponseText.equals(this.reponseText))
                 return true;
             else return  false;
         default : return false;
     }
 }

 /**
  * COnstructor question choix multiple
  * @param listrReponses_a
  * @param reponse_a
  */
 public Question(String enonce_a, String listrReponses_a, int [] reponse_a) {
  this.typeQuestion="choixmult";
  this.listeEnonces_ = listrReponses_a;
  this.enonce = enonce_a;
  //this.reponsesMultiples = reponse_a;
 }


    /**
     * Constructor text question
     * @param enonce_a
     * @param reponse_a
     */
  public Question(String enonce_a, String reponse_a){
    this.typeQuestion="text";
    this.enonce =enonce_a;
    this.reponseText=reponse_a;
  }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question ques = (Question) o;
        return id.equals(ques.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
