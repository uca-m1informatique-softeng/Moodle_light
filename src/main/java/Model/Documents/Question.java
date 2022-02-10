package Model.Documents;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @Brief Classe represente une question
 */

@Entity
@Table(	name = "Question")
public class Question {
    @NotBlank
    public  String enonce_;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @NotBlank
    @Size(max = 120)
    public  String listeEnonces_;

    @NotBlank
    @Size(max = 120)
    public  String typeQuestion;
    @NotBlank
    @Size(max = 120)
    public  int [] reponsesMultiples;
    @NotBlank
    @Size(max = 120)
    public  int reponseQcm;
    @NotBlank
    @Size(max = 120)
    public  String reponseText;

    public Question() {}

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_reponse",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "reponse_id"))
    public Set<Reponse> reponses = new HashSet<>();


 /**
  * Constructor choix parmis list
  * @param enonce_
  */
 public Question(String enonce_, String listrReponses_a, int reponse_a) {
  this.listeEnonces_ = listrReponses_a;
  this.typeQuestion="qcm";
  this.enonce_ = enonce_;
  this.reponseQcm = reponseQcm;
 }

 public boolean reponse(Reponse reponse){
     switch(reponse.typeReponse) {
         case "qcm":
             return reponse.reponseQcm == this.reponseQcm;

         case "choixmult":
             if ( reponse.reponsesMultiples.length != reponse.reponsesMultiples.length)
                 return  false ;
             for (int i = 0 ; i < reponse.reponsesMultiples.length; i++){
                 if(reponse.reponsesMultiples[i] == this.reponsesMultiples[i]) {
                     return false;
                 }
             }
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
  this.enonce_ = enonce_a;
  this.reponsesMultiples = reponse_a;
 }

  public Question(String enonce_a, String reponse_a){
    this.typeQuestion="text";
    this.enonce_=enonce_a;
    this.reponseText=reponse_a;
  }
}
