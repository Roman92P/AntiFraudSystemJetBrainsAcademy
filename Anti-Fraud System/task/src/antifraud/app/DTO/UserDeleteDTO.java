package antifraud.app.DTO;

import java.io.Serializable;

/**
 * Roman Pashkov created on 26.08.2022 inside the package - antifraud.app.DAO
 */
public class UserDeleteDTO implements Serializable {

    private String username;
    private final String status = "Deleted successfully!";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }
}