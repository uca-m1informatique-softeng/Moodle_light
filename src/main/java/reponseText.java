public class reponseText implements TypeReponse{
    private String reponse_;
    private boolean repondu_;
    public reponseText(){
        this.reponse_="";
        this.repondu_=false;
    }
    @Override
    public String getReponse() {
        return this.reponse_;
    }
}
