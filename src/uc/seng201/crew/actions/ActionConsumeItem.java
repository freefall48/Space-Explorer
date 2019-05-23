package uc.seng201.crew.actions;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.GameState;
import uc.seng201.errors.ActionException;
import uc.seng201.items.SpaceItem;

/**
 * Action for a crew member consuming an item from the space ship.
 */
public final class ActionConsumeItem extends ConsumeActionPoint {
    @Override
    public String perform(final GameState gameState, final Object[] args, final CrewMember... crewMembers) {
        super.perform(gameState, args, crewMembers);
        if (args.length != 1 && crewMembers.length != 1) {
            throw new ActionException("Invalid args provided");
        }
        if (args[0] instanceof SpaceItem) {
            SpaceItem item = (SpaceItem) args[0];
            gameState.getSpaceShip().remove(item);
            (item).onConsume(crewMembers[0]);
        }
        return null;
    }
}
