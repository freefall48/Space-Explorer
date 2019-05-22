package uc.seng201.errors;

/**
 * Game state errors.
 */
public class InvalidGameState extends IllegalStateException {

    /**
     * When modifying the game state would produce and inconsistent
     * state.
     */
    public InvalidGameState() {
        super();
    }
    /**
     * When modifying the game state would produce and inconsistent
     * state.
     *
     * @param message why the state would be inconsistent.
     */
    public InvalidGameState(final String message) {
        super(message);
    }
}
