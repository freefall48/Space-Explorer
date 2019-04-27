package uc.seng201.targets;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.helpers.Helpers;
import uc.seng201.items.ItemType;

public class Planet {

    private String planetName;
    private boolean partFound = false;

    public Planet() {
        this.planetName = Helpers.generatePlanetName();
    }

    @Override
    public String toString() {
        return getPlanetName();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Planet) {
            return getPlanetName().equals(((Planet) object).getPlanetName());
        }
        return false;
    }

    public String onSearch(CrewMember crewMember) {
        int action = Helpers.randomGenerator.nextInt(8);
        String message;
        switch (action) {
            case 0:
                int bucks = Helpers.randomGenerator.nextInt(16) + 10;
                SpaceExplorer.getSpaceShip().alterSpaceBucks(bucks);
                message = String.format("%s found $%d while searching!", crewMember.getName(), bucks);
                break;
            case 1:
                this.partFound = true;
                SpaceExplorer.getSpaceShip().partFound();
                message = String.format("%s FOUND A PART OF THE SHIP!", crewMember.getName().toUpperCase());
                break;
            case 2:
            case 3:
                int itemId = Helpers.randomGenerator.nextInt(ItemType.values().length);
                ItemType itemFound = ItemType.values()[itemId];
                SpaceExplorer.getSpaceShip().add(itemFound);
                message = String.format("%s found %s while exploring!", crewMember.getName(), itemFound);
                break;
            default:
                message = String.format("Unfortunately %s did not find anything this time.", crewMember.getName());
                break;
        }
        return message;
    }

    public String getPlanetName() {
        return planetName.toUpperCase();
    }

    public boolean getPartFound() {
        return partFound;
    }
}
