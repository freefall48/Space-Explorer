package uc.seng201.items;

import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;

public interface IItem {

    int getPrice();

    void onUse(CrewMember crewMember, SpaceShip spaceShip);
}
