package Model.Documents;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.reflect.Array;
import java.util.ArrayList;

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
