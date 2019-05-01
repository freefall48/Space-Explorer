package uc.seng201.targets;

import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.helpers.Helpers;
import uc.seng201.items.Items;

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

    public String onSearch(CrewMember crewMember, SpaceShip spaceShip) {
        int action = Helpers.randomGenerator.nextInt(5);
        String message = String.format("Unfortunately %s did not find anything this time.", crewMember.getName());
        switch (action) {
            case 0:
                int bucks = Helpers.randomGenerator.nextInt(16) + 10;
                spaceShip.alterSpaceBucks(bucks);
                message = String.format("%s found $%d while searching!", crewMember.getName(), bucks);
                break;
            case 1:
                if (!this.partFound) {
                    this.partFound = true;
                    spaceShip.partFound();
                    message = String.format("%s FOUND A PART OF THE SHIP!", crewMember.getName().toUpperCase());
                }
                break;
            case 2:
                int itemId = Helpers.randomGenerator.nextInt(Items.values().length);
                Items itemFound = Items.values()[itemId];
                spaceShip.add(itemFound);
                message = String.format("%s found %s while exploring!", crewMember.getName(), itemFound);
                break;
        }
        return message;
    }

    public String getPlanetName() {
        return planetName.toUpperCase();
    }

    public String description() {
        if (this.partFound) {
            return getPlanetName() + " does not have a part.";
        } else {
            return getPlanetName() + " still has a part hidden!";
        }
    }

    public boolean getPartFound() {
        return partFound;
    }
}
