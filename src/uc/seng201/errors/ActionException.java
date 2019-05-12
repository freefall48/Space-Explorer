package uc.seng201.errors;


public class ActionException extends IllegalStateException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ActionException(String message) {
        super(message);
    }

    public ActionException() {
        super();
    }
}
