package antifraud.app.model;

import java.io.Serializable;

/**
 * Roman Pashkov created on 22.08.2022 inside the package - antifraud.app.model
 */
public class Amount{

    private long amount;

    private String ip;

    private String number;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
