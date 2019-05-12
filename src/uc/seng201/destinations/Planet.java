package uc.seng201.destinations;

import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.utils.Helpers;
import uc.seng201.items.SpaceItem;

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
                int itemId = Helpers.randomGenerator.nextInt(SpaceItem.values().length);
                SpaceItem itemFound = SpaceItem.values()[itemId];
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
            return getPlanetName() + " - The transporter part for this planet has been found.";
        } else {
            return getPlanetName() + " - Could hold a transporter part.";
        }
    }

    public boolean getPartFound() {
        return partFound;
    }
}
