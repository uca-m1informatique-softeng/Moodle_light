package Documents;

import Model.Documents.TypeReponse;

import java.util.ArrayList;

public class QuestionText extends Question {
    private TypeReponse<String> reponse_;
    public QuestionText(String enonce_a, TypeReponse<String> reponse_a){
        super((java.lang.String) enonce_a);
        this.reponse_=reponse_a;
    }
    public ArrayList<java.lang.String> getChoix(){
        return null;
    }
    public TypeReponse<String> getReponse() {
        return this.reponse_;
    }
}
