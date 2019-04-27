package uc.seng201.gui;

import com.sun.tools.javac.Main;
import uc.seng201.SpaceExplorer;
import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdventureCreator extends JPanel implements Screen{
    private JTextField textShipName;
    private JSlider sliderDuration;
    private JPanel panelCreator;
    private JButton btnContinue;
    private JButton btnAddCrew;
    private JList listCrew;
    private JCheckBox checkboxCustomShipFile;

    static DefaultListModel<CrewMember> listCrewModal = new DefaultListModel<>();

    @Override
    public JPanel getRootPanel() {
        return panelCreator;
    }

    AdventureCreator() {

        textShipName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!textShipName.getText().equals("") && textShipName.getText().matches("^([a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*){1,12}$")) {
                    btnAddCrew.setEnabled(true);
                } else {
                    btnAddCrew.setEnabled(false);
                }
            }
        });

        listCrew.setModel(listCrewModal);
        listCrew.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        btnAddCrew.addActionListener(e -> {
            JDialog test = new CreateCrew();
            test.setSize(450,230);
            test.setVisible(true);
        });
        btnContinue.addActionListener(e -> {
            SpaceExplorerGui.spaceShip = new SpaceShip(textShipName.getText(),
                    SpaceExplorer.calcPartsToFind(sliderDuration.getValue()));
            SpaceExplorerGui.spaceShip.add(listCrewModal.toArray());
            SpaceExplorerGui.planets = SpaceExplorer.generatePlanets(sliderDuration.getValue());
            SpaceExplorerGui.currentPlanet = SpaceExplorerGui.planets.get(0);
            SpaceExplorerGui.redraw(new MainScreen().getRootPanel());
        });

        checkboxCustomShipFile.addActionListener(e -> {

            if (checkboxCustomShipFile.isSelected()) {
                FileDialog fd = new FileDialog(SpaceExplorerGui.getControlFrame(), "Choose a file", FileDialog.LOAD);
                fd.setFile("*.png");
                fd.setMultipleMode(false);
                fd.setVisible(true);
                if (fd.getFile() != null) {
                    try {
                        SpaceExplorerGui.shipImage = ImageIO.read(fd.getFiles()[0]);
                        SpaceExplorerGui.shipImageLocation = fd.getDirectory()+fd.getFile();
                    } catch (IOException error) {
                        JOptionPane.showMessageDialog(SpaceExplorerGui.getControlFrame(),
                                "Failed to load the selected image!","Error", JOptionPane.ERROR_MESSAGE);
                        checkboxCustomShipFile.setSelected(false);
                    }
                } else {
                    checkboxCustomShipFile.setSelected(false);
                }
                fd.dispose();
            }
        });

        btnContinue.addActionListener(e -> {
//            if ()
        });

        listCrewModal.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                update();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                update();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                update();
            }

            private void update() {
                if (listCrewModal.getSize() >= 2 && !textShipName.getText().equals("")) {
                    btnContinue.setEnabled(true);
                }
                if (listCrewModal.getSize() == 4) {
                    btnAddCrew.setEnabled(false);
                }
            }
        });
    }

}
