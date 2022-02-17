package Model.Documents;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(	name = "Note")
public class Note {

    @Range(min=0, max=20)
    public int note;

    @NotBlank
    public String username = "";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Note(){}

    public Note(int note, String username) {
        this.note = note;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note rep = (Note) o;
        return id.equals(rep.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
