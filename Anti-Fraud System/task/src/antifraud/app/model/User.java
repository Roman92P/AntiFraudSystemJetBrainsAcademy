package antifraud.app.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Roman Pashkov created on 26.08.2022 inside the package - antifraud.app.model
 */
@Entity
public class User implements Serializable, Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String role;
    private User user;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int compareTo(User user) {
        this.user = user;
        if (this.id > user.getId()) {
            return 1;
        } else if (this.id < user.getId()) {
            return -1;
        } else {
            return 0;
        }
    }
}
