package antifraud.app.model;

/**
 * Roman Pashkov created on 22.08.2022 inside the package - antifraud.app.model
 */
public class ResponseObj {

    public ResponseObj(String result) {
        this.result = result;
    }

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
