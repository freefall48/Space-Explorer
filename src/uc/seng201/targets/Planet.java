package uc.seng201.targets;

import uc.seng201.Helpers;
import uc.seng201.SpaceExplorer;
import uc.seng201.items.ItemType;

public class Planet {

    private String planetName;
    private boolean partFound = false;

    public Planet() {
        planetName = Helpers.generatePlanetName();
    }

    public void onSearch() {
        int action = Helpers.randomGenerator.nextInt(4);
        switch (action) {
            case 0:
                SpaceExplorer.getSpaceShip().alterSpaceBucks(
                        Helpers.randomGenerator.nextInt(15) + 10);
                break;
            case 1:
                this.partFound = true;
                SpaceExplorer.getSpaceShip().partFound();
                break;
            case 2: case 3:
                int itemId = Helpers.randomGenerator.nextInt(ItemType.values().length - 1);
                SpaceExplorer.getSpaceShip().add(ItemType.values()[itemId]);
                break;
            default:
                break;

        }
    }

    public String getPlanetName() {
        return planetName;
    }

    public boolean getPartFound() {
        return partFound;
    }
}
