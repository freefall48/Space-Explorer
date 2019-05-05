package uc.seng201.errors;


public class ActionException extends IllegalStateException {

    public ActionException(String message) {
        super(message);
    }

    public ActionException() {
        super();
    }
}
