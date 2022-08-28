package antifraud.app.DAO;

import lombok.Data;

import java.io.Serializable;

/**
 * Roman Pashkov created on 26.08.2022 inside the package - antifraud.app.DAO
 */
public class UserDTO implements Serializable {
    private Long id;
    private String name;
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
