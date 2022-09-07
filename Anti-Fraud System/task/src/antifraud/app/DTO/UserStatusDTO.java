package antifraud.app.DTO;

import java.io.Serializable;

public class UserStatusDTO implements Serializable {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
