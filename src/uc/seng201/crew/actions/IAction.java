package uc.seng201.crew.actions;

import uc.seng201.GameState;
import uc.seng201.crew.CrewMember;

public interface IAction {

    String perform(GameState gameState, Object[] args, CrewMember... crewMembers);
}
