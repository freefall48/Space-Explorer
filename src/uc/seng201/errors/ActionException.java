package uc.seng201.errors;

/**
 * Action that cannot be performed.
 */
public class ActionException extends IllegalStateException {

    /**
     * When an action attempted cannot be performed because it would
     * produce an inconsistent game state.
     *
     * @param message describes how the state would be inconsistent.
     */
    public ActionException(final String message) {
        super(message);
    }

    /**
     * When an action attempted cannot be performed because it would
     * produce an inconsistent game state.
     */
    public ActionException() {
        super();
    }
}
