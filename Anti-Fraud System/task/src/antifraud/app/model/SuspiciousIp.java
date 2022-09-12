package antifraud.app.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Roman Pashkov created on 06.09.2022 inside the package - antifraud.app.model
 */
@Entity
public class SuspiciousIp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String ip;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}