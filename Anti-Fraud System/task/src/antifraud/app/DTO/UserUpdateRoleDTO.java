package antifraud.app.DTO;

import antifraud.app.model.Role;

import java.io.Serializable;

public class UserUpdateRoleDTO implements Serializable {

    private String username;
    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
