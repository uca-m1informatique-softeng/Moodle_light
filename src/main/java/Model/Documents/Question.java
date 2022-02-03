package Model.Documents;

/**
 * @Brief Classe represente une question
 */
public class Question {
    private String enonce_;
    private TypeReponse reponses_;

    public Question(String enonce_arg, TypeReponse typeReponse_arg){
        this.enonce_=enonce_arg;
        this.reponses_= typeReponse_arg;
    }
}
