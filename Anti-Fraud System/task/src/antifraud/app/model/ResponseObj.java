package antifraud.app.model;

/**
 * Roman Pashkov created on 22.08.2022 inside the package - antifraud.app.model
 */
public class ResponseObj {

    public ResponseObj(String result, String info) {

        this.result = result;
        this.info = info;
    }

    private String result;

    private String info;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}