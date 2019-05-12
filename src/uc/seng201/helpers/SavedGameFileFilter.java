package uc.seng201.helpers;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Implementation of a FileFilter that only allows JSON files to be
 * loaded.
 * 
 * @author Matthew Johnson
 *
 */
public class SavedGameFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        return f.getName().toUpperCase().endsWith(".JSON");
    }

    @Override
    public String getDescription() {
        return "Game Saves (*.json)";
    }
}
