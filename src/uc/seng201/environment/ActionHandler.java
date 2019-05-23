package uc.seng201.environment;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.actions.CrewAction;
import uc.seng201.utils.observerable.Observer;

/**
 * Handler responsible for controlling crew members actions.
 */
public final class ActionHandler implements Observer {

    @Override
    public void onEvent(final Object... args) {
        if (args.length == 4) {
            // Check to make sure we have the right arguments.
            if (args[0] instanceof CrewAction && args[1] instanceof CrewMember[] && args[2] instanceof Object[]
                    && args[3] instanceof GameState) {

                CrewAction action = (CrewAction) args[0];
                CrewMember[] actingCrew = (CrewMember[]) args[1];

                /*
                Call the correct action and provide it with the game-state instance, action specific arguments
                and the crew who are performing the action. Get the response message.
                 */
                String message = action.getInstance().perform((GameState) args[3], (Object[]) args[2], actingCrew);

                // Let the display handler deal with the message
                if (message != null) {
                    Display.popup(message);
                }
            }
        }
    }
}
