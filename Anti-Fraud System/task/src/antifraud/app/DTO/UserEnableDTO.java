package antifraud.app.DTO;

import antifraud.app.model.ENABLE_OPERATION;

import java.io.Serializable;

public class UserEnableDTO implements Serializable {

    private String username;
    private ENABLE_OPERATION operation;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ENABLE_OPERATION getOperation() {
        return operation;
    }

    public void setOperation(ENABLE_OPERATION operation) {
        this.operation = operation;
    }
}