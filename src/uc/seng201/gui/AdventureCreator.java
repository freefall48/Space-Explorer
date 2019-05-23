package uc.seng201.gui;

import uc.seng201.*;
import uc.seng201.crew.CrewMember;
import uc.seng201.environment.Display;
import uc.seng201.misc.Planet;
import uc.seng201.environment.GameEnvironment;
import uc.seng201.environment.GameState;
import uc.seng201.utils.observerable.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

/**
 * Create a new game.
 */
class AdventureCreator extends ScreenComponent {

    /**
     * Allows the user to enter their ship name.
     */
    private JTextField textShipName;
    /**
     * Allows the user to select the duration of the game.
     */
    private JSlider sliderDuration;
    /**
     * Root panel.
     */
    private JPanel panelCreator;
    /**
     * Continue button. When the user has finished creating crew and
     * naming the ship.
     */
    private JButton btnContinue;
    /**
     * Add crew button. Allows the user to add crew members.
     */
    private JButton btnAddCrewMember;
    /**
     * Shows the current crew members the user has created.
     */
    private JList<CrewMember> listCrew;
    /**
     * Allows the user to navigate back to the main menu.
     */
    private JButton btnBack;
    /**
     * Update crew member button. Allows the user to modify an existing crew member.
     */
    private JButton btnUpdateCrewMember;
    /**
     * Remove crew member button. Allows the user to remove an existing crew member.
     */
    private JButton btnRemoveCrewMember;
    /**
     * The model that backs the crew member list. Contains all the crew member instances.
     */
    private DefaultListModel<CrewMember> crewListModel;

    /**
     * Allows the user to create a new game. Add their own crew members and name their ship.
     */
    AdventureCreator(GameState gameState) {
        crewListModel = new DefaultListModel<>();

        textShipName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                validateState();
            }
        });

        listCrew.setModel(crewListModel);

        listCrew.addListSelectionListener(e -> {
            btnRemoveCrewMember.setEnabled(true);
            btnUpdateCrewMember.setEnabled(true);
        });

        btnBack.addActionListener(e -> Display.changeScreen(Screen.MAIN_MENU));
        btnAddCrewMember.addActionListener(e -> onAddCrewMember());
        btnContinue.addActionListener(e -> onContinue());
        btnUpdateCrewMember.addActionListener(e -> onUpdateCrewMember());
        btnRemoveCrewMember.addActionListener(e -> onRemoveCrewMember());
    }

    /**
     * Generates a list of planets based on the game duration. The number
     * of planets is equal to the number of days in the game.
     *
     * @param duration number of days the game will run.
     * @return list of generated planets.
     */
    private Set<Planet> generatePlanets(int duration) {
        Set<Planet> planets = new HashSet<>();
        for (int i = 0; i < duration; i++) {
            planets.add(new Planet());
        }
        return planets;
    }

    @Override
    public JComponent getRootComponent() {
        return panelCreator;
    }

    /**
     * Called when the continue button is clicked. The users crew and spaceship name are
     * used to create a new spaceship and added to the new game state. The new game
     * state is then set to the event manager.
     */
    private void onContinue() {
        int gameDuration = sliderDuration.getValue();
        SpaceShip spaceShip = new SpaceShip(textShipName.getText(), SpaceShip.calcPartsToFind(gameDuration));

        /*
        Creates the hash set based on the crew model. We use a hash set as it only
        allows one copy of each entry and we dont care about the crew order.
         */
        spaceShip.add(new HashSet<>(Collections.list(crewListModel.elements())));

        // Generate the planets for this run through.
        Set<Planet> planets = generatePlanets(gameDuration);
        GameState gameState = new GameState(spaceShip, gameDuration, planets);

        // There is a new game state so notify the event manager.
        GameEnvironment.EVENT_MANAGER.notifyObservers(Event.NEW_GAME_STATE, gameState);
        Display.changeScreen(Screen.MAIN_SCREEN);
    }

    /**
     * Called when the add crew member button is called. Creates a dialog for the
     * user to create a crew member. If the user creates a crew member the new
     * crew member is added to the backing set.
     */
    private void onAddCrewMember() {
        CreateCrewMember createDialog = new CreateCrewMember();
        createDialog.setLocationRelativeTo(panelCreator);
        createDialog.setResizable(false);
        CrewMember newCrewMember = createDialog.showDialog();
        if (newCrewMember == null) {
            return;
        }
        // Make sure we are not adding the same crew member.
        if (crewListModel.contains(newCrewMember)) {
            Display.popup(String.format("Cannot add %s a %s as they are already part of the crew!",
                    newCrewMember.getName(), newCrewMember.getCrewType()));
        } else {
            crewListModel.addElement(newCrewMember);
            resetCrewButtons();
            validateState();
        }

    }

    /**
     * Called when the remove crew member button is clicked. Prompts the user
     * if they really want to remove the selected crew member. If yes the
     * selected crew member is removed from the backing set.
     */
    private void onRemoveCrewMember() {
        int confirmed = JOptionPane.showConfirmDialog(panelCreator,
                String.format("Do you really want to remove %s?", listCrew.getSelectedValue()), "Remove Crew Member",
                JOptionPane.YES_NO_OPTION);
        if (confirmed == 0) {
            this.crewListModel.removeElementAt(listCrew.getSelectedIndex());
            resetCrewButtons();
            validateState();
        }
    }

    /**
     * Called when the update crew member button is clicked. Creates a dialog to
     * update the details of the selected crew member, and updates the backing
     * set if the crew member changes.
     */
    private void onUpdateCrewMember() {
        CrewMember currentCrewMember = listCrew.getSelectedValue();
        CreateCrewMember createCrewMemberDialog = new CreateCrewMember(currentCrewMember);
        createCrewMemberDialog.setResizable(false);
        createCrewMemberDialog.setLocationRelativeTo(panelCreator);
        CrewMember alteredCrewMember = createCrewMemberDialog.showDialog();
        if (alteredCrewMember != null) {
            if (!crewListModel.contains(alteredCrewMember)) {
                int insertIndex = crewListModel.indexOf(currentCrewMember);
                crewListModel.removeElement(currentCrewMember);
                crewListModel.add(insertIndex, alteredCrewMember);
            } else {
                Display.popup(String.format("Cannot add %s a %s as they are already part of the crew!",
                        alteredCrewMember.getName(), alteredCrewMember.getCrewType()));
            }
            resetCrewButtons();
            validateState();
        }

    }

    /**
     * Disables the update and remove crew buttons. Checks if the add crew member
     * button should be enabled.
     */
    private void resetCrewButtons() {
        this.btnUpdateCrewMember.setEnabled(false);
        this.btnRemoveCrewMember.setEnabled(false);
        btnAddCrewMember.setEnabled(crewListModel.size() <= SpaceShip.MAXIMUM_CREW_COUNT);
        revalidate();
        repaint();
    }

    /**
     * Makes sure there is a valid number of crew and that the spaceships name is
     * of an acceptable format.
     */
    private void validateState() {
        btnAddCrewMember.setEnabled(SpaceShip.MAXIMUM_CREW_COUNT > crewListModel.size());
        btnContinue.setEnabled(this.crewListModel.size() >= SpaceShip.MINIMUM_CREW_COUNT
                && this.crewListModel.size() <= SpaceShip.MAXIMUM_CREW_COUNT && !textShipName.getText().equals("")
                && textShipName.getText().matches("^([a-zA-Z0-9_]( [a-zA-Z0-9_]+)*){3,24}$"));
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelCreator = new JPanel();
        panelCreator.setLayout(new GridBagLayout());
        panelCreator.setPreferredSize(new Dimension(650, 500));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setMinimumSize(new Dimension(-1, -1));
        panel1.setPreferredSize(new Dimension(-1, -1));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 4.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelCreator.add(panel1, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Droid Sans Mono", -1, 36, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Create New Adventure");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel1.add(label1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel2.setAutoscrolls(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 7.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 0, 20);
        panelCreator.add(panel2, gbc);
        final JSeparator separator1 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel2.add(separator1, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Droid Sans Mono", -1, 20, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Current Crew");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel2.add(label2, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(30);
        scrollPane1.setPreferredSize(new Dimension(0, 150));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 10.0;
        gbc.weighty = 7.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(scrollPane1, gbc);
        listCrew = new JList();
        listCrew.setAutoscrolls(true);
        listCrew.setEnabled(true);
        listCrew.setFixedCellHeight(40);
        listCrew.setFixedCellWidth(-1);
        Font listCrewFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, listCrew.getFont());
        if (listCrewFont != null) listCrew.setFont(listCrewFont);
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        listCrew.setModel(defaultListModel1);
        listCrew.setSelectionMode(0);
        listCrew.setVisibleRowCount(4);
        scrollPane1.setViewportView(listCrew);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel3, gbc);
        btnRemoveCrewMember = new JButton();
        btnRemoveCrewMember.setEnabled(false);
        btnRemoveCrewMember.setText("Remove");
        btnRemoveCrewMember.setToolTipText("Remove selected crew member from our force.");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 10, 2, 0);
        panel3.add(btnRemoveCrewMember, gbc);
        btnUpdateCrewMember = new JButton();
        btnUpdateCrewMember.setEnabled(false);
        btnUpdateCrewMember.setText("Change");
        btnUpdateCrewMember.setToolTipText("Change the selected crew memeber.");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 10, 2, 0);
        panel3.add(btnUpdateCrewMember, gbc);
        btnAddCrewMember = new JButton();
        btnAddCrewMember.setEnabled(true);
        btnAddCrewMember.setText("Add");
        btnAddCrewMember.setToolTipText("Add a crew member to our force");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 10, 2, 0);
        panel3.add(btnAddCrewMember, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer1, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel4, gbc);
        textShipName = new JTextField();
        textShipName.setToolTipText("Ship name must be between 3-24 characters long");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 200;
        panel4.add(textShipName, gbc);
        sliderDuration = new JSlider();
        sliderDuration.setMajorTickSpacing(1);
        sliderDuration.setMaximum(10);
        sliderDuration.setMinimum(3);
        sliderDuration.setPaintLabels(true);
        sliderDuration.setPaintTicks(false);
        sliderDuration.setSnapToTicks(true);
        sliderDuration.setToolTipText("Duration of the game");
        sliderDuration.setValue(5);
        sliderDuration.setValueIsAdjusting(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 150;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel4.add(sliderDuration, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Ship Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel4.add(label3, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Duration:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 15, 0, 5);
        panel4.add(label4, gbc);
        final JSeparator separator2 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel4.add(separator2, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 20, 20);
        panelCreator.add(panel5, gbc);
        btnBack = new JButton();
        btnBack.setText("Main Menu");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 10);
        panel5.add(btnBack, gbc);
        btnContinue = new JButton();
        btnContinue.setEnabled(false);
        btnContinue.setText("Start Adventure");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel5.add(btnContinue, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(spacer2, gbc);
        final JSeparator separator3 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel5.add(separator3, gbc);
        label3.setLabelFor(textShipName);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelCreator;
    }


}
