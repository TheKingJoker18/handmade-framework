package exception;

public class MyException extends Exception {
    int error_code;
    String message;

    public int getErrorCode() {
        return error_code;
    }
    public void setErrorCode(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public MyException() {}
    public MyException(int error_code, String message) {
        this.setErrorCode(error_code);
        this.setMessage(message);
    }
}
