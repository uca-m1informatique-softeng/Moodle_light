package Model.Payload.request;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateQuestionRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String enonce;

    @NotBlank
    @Size(min = 0, max = 20)
    String questionType;

    @Size(min = 3, max = 20)
    private String reponse;

    @Size(max = 120)
    public  String listeEnonces_="";

    @Size(max = 120)
    public  String typeQuestion="";
   /* @Range(min=1, max=20)
    public  int [] reponsesMultiples={};*/
    @Range(min=0, max=20)
    public  int reponseQcm=0;
    @Size(max = 120)
    public  String reponseText="";


    public String getReponse(){
        return this.reponse;
    }
    public void  setReponse(String reponseText){this.reponse=reponseText;}
    public CreateQuestionRequest(){
        this.enonce="[DEFAULT ENONCE]";
        this.questionType="[DEFAULT QUESTION TYPE]";
    }

    public String getQuestionType(){
        return  this.questionType;
    }

    public void setQuestionType(String questionType_a){
        this.questionType = questionType_a;
    }
    public  String getEnonce(){
        return this.enonce;
    }
    public void setEnonce(String enonce_a){
        this.enonce=enonce_a;
    }


}