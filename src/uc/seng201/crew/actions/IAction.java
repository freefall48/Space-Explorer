package uc.seng201.crew.actions;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.GameState;

public interface IAction {

    String perform(GameState gameState, Object[] args, CrewMember... crewMembers);
}
