package uc.seng201.gui;

import javax.swing.*;

public class StatsScreen extends JPanel implements Screen {
    private JPanel StatsPanel;
    private JTabbedPane tabbedPane1;
    private JButton button1;
    private JButton button2;
    private JList list1;
    private JList list2;
    private JList list3;

    public StatsScreen() {
        StatsPanel.setSize(1000, 600);
    }

    public JPanel getScreen() {
        return this.StatsPanel;
    }
}
