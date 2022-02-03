package Model.Documents;

/**
 * @Brief Classe represente une question
 */
public abstract class Question<T> {
    private String enonce_;
    private T reponses_;

    public Question(String enonce_arg, T typeReponse_arg){
        this.enonce_=enonce_arg;
        this.reponses_= typeReponse_arg;
    }
}
