package uc.seng201.errors;

public class SpaceShipException extends IllegalStateException {

    public SpaceShipException() {
        super();
    }

    public SpaceShipException(String message) {
        super(message);
    }
}
