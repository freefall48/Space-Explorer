package uc.seng201.errors;

public class SpaceShipException extends IllegalStateException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpaceShipException() {
        super();
    }

    public SpaceShipException(String message) {
        super(message);
    }
}
