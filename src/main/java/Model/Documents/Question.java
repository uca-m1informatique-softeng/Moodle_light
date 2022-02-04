package Model.Documents;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @Brief Classe represente une question
 */
public class Question<T> {
    String enonce_;
    ArrayList<String> listeEnonces_;
    String typeQuestion;
    int [] reponsesMultiples;
    int reponseQcm;
    String reponseText;

    private TypeReponse reponses_;

    /**
     * Constructor choix parmis list
     * @param enonce_
     */
    public Question(String enonce_, ArrayList<String> listrReponses_a, int reponse_a) {
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
    public Question(String enonce_a, ArrayList<String> listrReponses_a, int [] reponse_a) {
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
