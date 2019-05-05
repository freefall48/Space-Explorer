package uc.seng201.helpers;

import javax.swing.filechooser.FileFilter;
import java.io.File;

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
