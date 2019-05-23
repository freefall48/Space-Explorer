package uc.seng201.gui;

import javax.swing.*;

/**
 * Extends the JComponent and implements extra functionality.
 */
public abstract class ScreenComponent extends JComponent {


    /**
     * Returns the root component of the screen.
     *
     * @return root component of the screen.
     */
    public abstract JComponent getRootComponent();
}
