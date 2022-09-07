package antifraud.app.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Roman Pashkov created on 06.09.2022 inside the package - antifraud.app.model
 */
@Entity
public class Stolencard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String number;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
