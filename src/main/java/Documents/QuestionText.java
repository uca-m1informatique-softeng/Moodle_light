import java.util.ArrayList;

public class QuestionText extends Question {
    private String reponse_;
    public QuestionText(String enonce_a, String reponse_a){
        super(enonce_a);
        this.reponse_=reponse_a;
    }
    public ArrayList<String> getChoix(){
        return null;
    }
    public String getReponse() {
        return this.reponse_;
    }
}
