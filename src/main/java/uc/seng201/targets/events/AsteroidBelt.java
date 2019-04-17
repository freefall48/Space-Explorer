package uc.seng201.targets.events;

import uc.seng201.SpaceExplorer;

public class AsteroidBelt implements IEvent {

    @Override
    public void onTriggered() {
        SpaceExplorer.getSpaceShip().alterShield(-1);
    }
}
