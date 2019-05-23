package uc.seng201.misc;

import uc.seng201.SpaceExplorer;
import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.items.SpaceItem;

/**
 * Plants can contain money, parts or items. Crew members
 * are able to search them.
 */
public class Planet {

    /**
     * Name of the planet. XXX-XXX format.
     */
    private String planetName;
    /**
     * Only one part can be found per planet. Has
     * the part been found for this planet.
     */
    private boolean partFound;

    /**
     * Create a new planet with a random name.
     */
    public Planet() {
        this.planetName = generatePlanetName();
    }

    /**
     * Generates a random planet name based on the format "XXX-XXX".
     *
     * @return name of a planet.
     */
    private static String generatePlanetName() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            // Generate a random number up to 26 and add that to the value of a to find a random letter.
            builder.append((char) (SpaceExplorer.randomGenerator.nextInt(26) + 'a'));
        }
        builder.append('-');
        for (int i = 0; i < 3; i++) {
            builder.append(SpaceExplorer.randomGenerator.nextInt(9));
        }
        return builder.toString();
    }

    /**
     * Returns the planets name in all uppercase.
     *
     * @return textual description of the planet.
     */
    @Override
    public String toString() {
        return planetName.toUpperCase();
    }

    /**
     * Planets are the same if they have the same name.
     *
     * @param object planet to compare to.
     * @return true if the planets have the same name.
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof Planet) {
            return toString().equals(object.toString());
        }
        return false;
    }

    /**
     * Planets have the same hash if they have the same name.
     *
     * @return hash of the planet.
     */
    @Override
    public int hashCode() {
        return planetName.hashCode();
    }

    /**
     * When a crew member searches a planet then calculate what they
     * find.
     *
     * @param crewMember who is searching the planet.
     * @param spaceShip  reference to the spaceship.
     * @return description of the result of the search.
     */
    public String onSearch(CrewMember crewMember, SpaceShip spaceShip) {
        int outcome = SpaceExplorer.randomGenerator.nextInt(5);
        String message = String.format("Unfortunately %s did not find anything this time.", crewMember.getName());
        switch (outcome) {
            case 0:
                // Money was found.
                int bucks = SpaceExplorer.randomGenerator.nextInt(16) + 10;
                spaceShip.alterSpaceBucks(bucks);
                message = String.format("%s found $%d while searching!", crewMember.getName(), bucks);
                break;
            case 1:
                // Transporter part found.
                if (!this.partFound) {
                    this.partFound = true;
                    spaceShip.partFound();
                    message = String.format("%s FOUND A PART OF THE SHIP!", crewMember.getName().toUpperCase());
                }
                break;
            case 2:
                // Item was found when searching.
                int itemId = SpaceExplorer.randomGenerator.nextInt(SpaceItem.values().length);
                SpaceItem itemFound = SpaceItem.values()[itemId];
                spaceShip.add(itemFound);
                message = String.format("%s found the item %s while searching!", crewMember.getName(), itemFound);
                break;
        }
        return message;
    }

    /**
     * Returns a textual description of the planet based on its name
     * and if a part has been found on the planet.
     *
     * @return textual description of the planet.
     */
    public String description() {
        if (this.partFound) {
            return toString() + " - The transporter part for this planet has been found.";
        } else {
            return toString() + " - Could hold a transporter part.";
        }
    }
}
