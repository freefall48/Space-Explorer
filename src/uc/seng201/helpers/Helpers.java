package uc.seng201.helpers;


import uc.seng201.destinations.Planet;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides static helper methods.
 * 
 * @author Matthew Johnson
 *
 */
public class Helpers {
	
	/**
	 * Provides a single random generator to all classes.
	 */
    public static SecureRandom randomGenerator = new SecureRandom();
    
    /**
     * Provides the ability to check if a String is safe to parse to an integer.
     *
     * @param value String to try parse.
     * @return If it is safe to parse the string to an integer.
     */
    public static boolean intTryParse(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Generates a random planet name based on the format "XXX-XXX".
     * 
     * @return name of a planet.
     */
    public static String generatePlanetName() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            builder.append((char) (randomGenerator.nextInt(26) + 'a'));
        }
        builder.append('-');
        for (int i = 0; i < 3; i++) {
            builder.append(randomGenerator.nextInt(9));
        }
        return builder.toString();
    }

    /**
     * Calculates the number of spaceship parts that should be found based
     * on the number of days the game should run over.
     * 
     * @param duration number of days the game will run.
     * @return number of parts that should be found.
     */
    public static int calcPartsToFind(int duration) {
        return (int) (duration * 0.6666);
    }
    
    /**
     * Generates a list of planets based on the game duration. The number
     * of planets is equal to the number of days in the game.
     * 
     * @param duration number of days the game will run.
     * @return list of generated planets.
     */
    public static List<Planet> generatePlanets(int duration) {
        List<Planet> planets = new ArrayList<>();
        for (int i = 0; i < duration; i++) {
            planets.add(new Planet());
        }
        return planets;
    }
}
