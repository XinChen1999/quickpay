package QuickPay.demo.model;

public class Response {
    private String success;
    private String msg;

    public Response(String success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
