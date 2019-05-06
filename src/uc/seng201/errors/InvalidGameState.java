package uc.seng201.errors;

public class InvalidGameState extends IllegalStateException {

    public InvalidGameState() {
        super();
    }

    public InvalidGameState(String message) {
        super(message);
    }
}
