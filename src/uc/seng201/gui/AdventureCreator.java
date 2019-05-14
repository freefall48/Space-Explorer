package uc.seng201.gui;

import uc.seng201.Display;
import uc.seng201.GameState;
import uc.seng201.SpaceExplorer;
import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.destinations.Planet;
import uc.seng201.utils.observerable.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

class AdventureCreator extends ScreenComponent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JTextField textShipName;
    private JSlider sliderDuration;
    private JPanel panelCreator;
    private JButton btnContinue;
    private JButton btnAddCrewMember;
    private JList<CrewMember> listCrew;
    private JButton btnBack;
    private JButton btnUpdateCrewMember;
    private JButton btnRemoveCrewMember;

    private DefaultListModel<CrewMember> listCrewModal;

    AdventureCreator(GameState gameState) {
        listCrewModal = new DefaultListModel<>();

        textShipName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                validateState();
            }
        });

        listCrew.setModel(listCrewModal);

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
    private List<Planet> generatePlanets(int duration) {
        List<Planet> planets = new ArrayList<>();
        for (int i = 0; i < duration; i++) {
            planets.add(new Planet());
        }
        return planets;
    }

    @Override
    public JComponent getRootComponent() {
        return panelCreator;
    }

    private void onContinue() {
        int gameDuration = sliderDuration.getValue();
        SpaceShip spaceShip = new SpaceShip(textShipName.getText(), SpaceShip.calcPartsToFind(gameDuration));
        spaceShip.add(new HashSet<>(Collections.list(listCrewModal.elements())));

        List<Planet> planets = generatePlanets(gameDuration);
        GameState gameState = new GameState(spaceShip, gameDuration, planets);

        SpaceExplorer.eventManager.notifyObservers(Event.NEW_GAME_STATE, gameState);
        Display.changeScreen(Screen.MAIN_SCREEN);
    }

    private void onAddCrewMember() {
        CreateCrewMember createDialog = new CreateCrewMember();
        createDialog.setLocationRelativeTo(panelCreator);
        createDialog.setResizable(false);
        CrewMember newCrewMember = createDialog.showDialog();
        if (newCrewMember == null) {
            return;
        }
        if (listCrewModal.contains(newCrewMember)) {
            Display.popup(String.format("Cannot add %s a %s as they are already part of the crew!",
                    newCrewMember.getName(), newCrewMember.getCrewType()));
        } else {
            listCrewModal.addElement(newCrewMember);
            resetCrewButtons();
            validateState();
        }

    }

    private void onRemoveCrewMember() {
        int confirmed = JOptionPane.showConfirmDialog(panelCreator,
                String.format("Do you really want to remove %s?", listCrew.getSelectedValue()), "Remove Crew Member",
                JOptionPane.YES_NO_OPTION);
        if (confirmed == 0) {
            this.listCrewModal.removeElementAt(listCrew.getSelectedIndex());
            resetCrewButtons();
            validateState();
        }
    }

    private void onUpdateCrewMember() {
        CrewMember currentCrewMember = listCrew.getSelectedValue();
        CreateCrewMember createCrewMemberDialog = new CreateCrewMember(currentCrewMember);
        createCrewMemberDialog.setResizable(false);
        createCrewMemberDialog.setLocationRelativeTo(panelCreator);
        CrewMember alteredCrewMember = createCrewMemberDialog.showDialog();
        if (alteredCrewMember != null) {
            if (!listCrewModal.contains(alteredCrewMember)) {
                int insertIndex = listCrewModal.indexOf(currentCrewMember);
                listCrewModal.removeElement(currentCrewMember);
                listCrewModal.add(insertIndex, alteredCrewMember);
            } else {
                Display.popup(String.format("Cannot add %s a %s as they are already part of the crew!",
                        alteredCrewMember.getName(), alteredCrewMember.getCrewType()));
            }
            resetCrewButtons();
            validateState();
        }

    }

    private void resetCrewButtons() {
        this.btnUpdateCrewMember.setEnabled(false);
        this.btnRemoveCrewMember.setEnabled(false);
        revalidate();
        repaint();
    }

    private void validateState() {
        btnAddCrewMember.setEnabled(SpaceShip.MAXIMUM_CREW_COUNT > listCrewModal.size());
        btnContinue.setEnabled(this.listCrewModal.size() >= SpaceShip.MINIMUM_CREW_COUNT
                && this.listCrewModal.size() <= SpaceShip.MAXIMUM_CREW_COUNT && !textShipName.getText().equals("")
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
