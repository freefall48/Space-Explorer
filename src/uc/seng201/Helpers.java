package uc.seng201;


import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Helpers {

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

    public static String generatePlanetName() {
        char[] planetName = new char[7];
        for (int i = 0; i < 3; i++) {
            planetName[i] = (char) (randomGenerator.nextInt(26) + 'a');
        }
        planetName[3] = '-';
        for (int i = 4; i < 7; i++) {
            planetName[i] = (char) (randomGenerator.nextInt(9));
        }
        return planetName.toString();
    }

    public static String listToString(List list) {
        return listToString(list, false);
    }
    public static String listToString(List list, boolean newLine) {
        if (list.size() == 0) {
            return "no";
        }
        String deliminator = newLine ? ",\n" : ", ";
        return list.stream().map(Object::toString).collect(Collectors.joining(deliminator)).toString();
    }
}
