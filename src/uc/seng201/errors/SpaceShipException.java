package uc.seng201.errors;

/**
 * Spaceship errors.
 */
public class SpaceShipException extends IllegalStateException {

    /**
     * When the spaceship cannot be modified in way attempted.
     */
    public SpaceShipException() {
        super();
    }

    /**
     * When the spaceship cannot be modified in the way attempted and
     * a message to describe why it will not work.
     *
     * @param message why the spaceship cannot be modifed in that way.
     */
    public SpaceShipException(final String message) {
        super(message);
    }
}
