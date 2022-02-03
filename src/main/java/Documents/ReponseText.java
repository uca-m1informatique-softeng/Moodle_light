package Documents;

public class ReponseText<String> implements TypeReponse {
    private String reponse_;
    private boolean repondu_;
    public ReponseText(){
        this.reponse_="";
        this.repondu_=false;
    }
    @Override
    public String getReponse() {
        return this.reponse_;
    }
}