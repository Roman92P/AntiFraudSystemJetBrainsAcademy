package antifraud.app.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMINISTRATOR("ADMINISTRATOR"),
    MERCHANT("MERCHANT"),
    SUPPORT("SUPPORT");

    public final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name;
    }
}
