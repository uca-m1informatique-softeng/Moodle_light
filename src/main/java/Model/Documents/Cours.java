package Model.Documents;

public class Cours extends Ressource {
    public String text;

    public Cours(String name_a,String text) {
        super(name_a);
        this.text = text;
    }
}
