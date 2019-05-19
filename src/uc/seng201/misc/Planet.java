package uc.seng201.misc;

import uc.seng201.SpaceExplorer;
import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.items.SpaceItem;

public class Planet {

    private String planetName;
    private boolean partFound = false;

    public Planet() {
        this.planetName = generatePlanetName();
    }

    /**
     * Generates a random planet name based on the format "XXX-XXX".
     *
     * @return name of a planet.
     */
    public static String generatePlanetName() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            builder.append((char) (SpaceExplorer.randomGenerator.nextInt(26) + 'a'));
        }
        builder.append('-');
        for (int i = 0; i < 3; i++) {
            builder.append(SpaceExplorer.randomGenerator.nextInt(9));
        }
        return builder.toString();
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
        int action = SpaceExplorer.randomGenerator.nextInt(5);
        String message = String.format("Unfortunately %s did not find anything this time.", crewMember.getName());
        switch (action) {
            case 0:
                int bucks = SpaceExplorer.randomGenerator.nextInt(16) + 10;
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
                int itemId = SpaceExplorer.randomGenerator.nextInt(SpaceItem.values().length);
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
