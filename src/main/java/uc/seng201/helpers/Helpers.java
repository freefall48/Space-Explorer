package uc.seng201.helpers;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Helpers {

    public static SecureRandom randomGenerator = new SecureRandom();
    /**
     * Used to output to the console. IDEs dont use a standard console
     * so this allows user input to be gathered reliably.
     */
    public static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

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

    public static void waitForEnter() throws IOException {
        System.out.print("\n\nPress enter to continue...");
        bufferedReader.readLine();
        System.out.println();
    }

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

    public static String listToString(Object[] objects) {
        return listToString(Arrays.asList(objects));
    }

    public static String listToString(List list) {
        return listToString(list, false);
    }

    public static String listToString(List list, boolean newLine) {
        if (list.size() == 0) {
            return "None";
        }
        String deliminator = newLine ? ",\n" : ", ";
        return list.stream().map(Object::toString).collect(Collectors.joining(deliminator)).toString();
    }

    public static String removeExtention(String filePath) {
        // These first few lines the same as Justin's
        File f = new File(filePath);

        // if it's a directory, don't remove the extention
        if (f.isDirectory()) return filePath;

        String name = f.getName();

        // Now we know it's a file - don't need to do any special hidden
        // checking or contains() checking because of:
        final int lastPeriodPos = name.lastIndexOf('.');
        if (lastPeriodPos <= 0) {
            // No period after first character - return name as it was passed in
            return filePath;
        } else {
            // Remove the last period and everything after it
            File renamed = new File(f.getParent(), name.substring(0, lastPeriodPos));
            return renamed.getPath();
        }
    }
}
