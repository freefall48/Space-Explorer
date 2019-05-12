package uc.seng201.errors;

public class InvalidGameState extends IllegalStateException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidGameState() {
        super();
    }

    public InvalidGameState(String message) {
        super(message);
    }
}
