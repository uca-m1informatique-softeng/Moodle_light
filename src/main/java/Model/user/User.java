package Model.user;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "id")
        })
public class User {

    public String name;
    public String id;

    @NotBlank
    @Size(max = 120)
    private String password;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
