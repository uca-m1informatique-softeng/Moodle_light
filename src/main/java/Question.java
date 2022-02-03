import java.util.ArrayList;

/**
 * @Brief Classe represente une question
 */
public abstract class Question {
    private String enonce_;

    public Question(String enonce_arg){
        this.enonce_=enonce_arg;
    }
    public String getEnonce(){return null; }
    public ArrayList<String> getChoix(){ return null; }
}
