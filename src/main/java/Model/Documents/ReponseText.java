package Model.Documents;

public class ReponseText implements TypeReponse<S> {
    pString reponse_;



    @Override
    public boolean verifReponse(TypeReponse<S> rep_a) {

        if(rep_a instanceof TypeReponse)
        {

        }
        return false;
    }
}
