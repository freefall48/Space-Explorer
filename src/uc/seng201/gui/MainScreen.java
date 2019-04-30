package uc.seng201.gui;

import uc.seng201.crew.CrewMember;
import uc.seng201.helpers.Helpers;
import uc.seng201.helpers.StateActions;
import uc.seng201.events.EventTrigger;
import uc.seng201.events.RandomEvents;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.io.IOException;

public class MainScreen implements Screen {

    private JPanel panelRoot;
    private JLabel lblSpaceShipName;
    private JTabbedPane tabbedContentMenu;
    private JList<CrewMember> listCrew;
    private JLabel lblDay;
    private JLabel lblOrbiting;
    private JLabel lblMissingParts;
    private JLabel lblBalance;
    private JButton btnPerformAction;
    private JButton btnSave;
    private JButton btnNextDay;
    private JLabel lblShipHealth;
    private JList<String> listFoodItems;
    private JList<String> listMedicalSupplies;
    private JList<String> listPlanets;

    private DefaultListModel<CrewMember> listCrewModal = new DefaultListModel<>();
    private DefaultListModel<String> listMedicalSuppliesModel = new DefaultListModel<>();
    private DefaultListModel<String> listFoodItemsModel = new DefaultListModel<>();
    private DefaultListModel<String> listPlanetsModel = new DefaultListModel<>();

    public JPanel getRootPanel() {
        return panelRoot;
    }

    MainScreen() {
        initialiseTables();
        updateHUD();

        btnNextDay.addActionListener(e -> onNextDay());

        listCrew.addListSelectionListener(this::onCrewMemberSelection);

        btnPerformAction.addActionListener(e -> onPerformAction());

        btnSave.addActionListener(e -> onSave());
    }

    private void onSave() {
        FileDialog fd = new FileDialog(SpaceExplorerGui.getControlFrame(), "Save", FileDialog.SAVE);
        fd.setFilenameFilter((dir, name) -> name.toUpperCase().endsWith(".JSON"));
        fd.setVisible(true);
        try {
            if (fd.getFile() != null) {
                StateActions.saveState(fd.getDirectory() + fd.getFile(), SpaceExplorerGui.spaceShip,
                        SpaceExplorerGui.planets, SpaceExplorerGui.currentPlanet, SpaceExplorerGui.currentDay,
                        SpaceExplorerGui.gameDuration, SpaceExplorerGui.shipImageLocation);
            } else {
                throw new IOException();
            }
        } catch (IOException error) {
            JOptionPane.showMessageDialog(SpaceExplorerGui.getControlFrame(),
                    "Failed to save or was cancelled!", "Could not save", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onNextDay() {
        boolean showActionsLeftWarning = false;
        for (int i = 0; i < listCrewModal.getSize(); i++) {
            if (listCrewModal.get(i).canPerformActions()) {
                showActionsLeftWarning = true;
            }
        }
        if (showActionsLeftWarning) {
            int confirmed = JOptionPane.showConfirmDialog(SpaceExplorerGui.getControlFrame(),
                    "Do you really want to move to the next day? Some crew members can still perform actions!",
                    "Actions Left Today", JOptionPane.YES_NO_OPTION);
            if (confirmed != 0) {
                return;
            }
        }
        int nextDay = SpaceExplorerGui.currentDay + 1;
        if (nextDay <= SpaceExplorerGui.gameDuration) {
            SpaceExplorerGui.currentDay = nextDay;
            updateHUD();
        } else {
            SpaceExplorerGui.failedGame("On no! It seems you have failed to rebuild your ship in time! Err....");
        }
        SpaceExplorerGui.spaceShip.startOfDay();
        if (Helpers.randomGenerator.nextBoolean()) {
            RandomEvents event = RandomEvents.values()[Helpers.randomGenerator.nextInt(RandomEvents.values().length)];
            if (event.getTrigger().equals(EventTrigger.START_DAY)) {
                JOptionPane.showMessageDialog(SpaceExplorerGui.getControlFrame(),
                        String.format(event.getEventDescription(), SpaceExplorerGui.spaceShip.getShipName()));
                event.onTrigger(SpaceExplorerGui.spaceShip);
            }
        }
        updateTablesModels();
        reloadState();
    }

    private void onPerformAction() {
        JDialog performAction = new PerformAction(listCrewModal.get(listCrew.getSelectedIndex()));
        performAction.setSize(450, 350);
        performAction.setLocationRelativeTo(null);
        performAction.setVisible(true);
        checkAllPartsFound();
        updateHUD();
        reloadState();
        listCrew.clearSelection();
        btnPerformAction.setEnabled(false);
    }

    private void checkAllPartsFound() {
        if (SpaceExplorerGui.spaceShip.getMissingParts() == 0) {
            SpaceExplorerGui.completedGame();
        }
    }

    private void onCrewMemberSelection(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting() && listCrew.getSelectedIndex() != -1) {
            if (listCrewModal.get(listCrew.getSelectedIndex()).canPerformActions()) {
                btnPerformAction.setEnabled(true);
            } else {
                btnPerformAction.setEnabled(false);
            }
        }
    }

    private void initialiseTables() {
        listCrewModal.addAll(SpaceExplorerGui.spaceShip.getShipCrew());
        listCrew.setModel(listCrewModal);
        listFoodItems.setModel(listFoodItemsModel);
        listMedicalSupplies.setModel(listMedicalSuppliesModel);
        listPlanets.setModel(listPlanetsModel);
    }

    private void updateTablesModels() {
        listFoodItemsModel.removeAllElements();
        listMedicalSuppliesModel.removeAllElements();
        listPlanetsModel.removeAllElements();
        SpaceExplorerGui.planets.forEach(planet -> listPlanetsModel.addElement(planet.description()));
        SpaceExplorerGui.spaceShip.getShipItems().forEach(item -> {
            switch (item.getItemType()) {
                case FOOD:
                    listFoodItemsModel.addElement(item.toString() + " - " + item.getItemDescription());
                    break;
                case MEDICAL:
                    listMedicalSuppliesModel.addElement(item.toString() + " - " + item.getItemDescription());
                    break;
            }
        });
    }

    private void updateHUD() {
        updateTablesModels();
        lblSpaceShipName.setText(SpaceExplorerGui.spaceShip.getShipName());
        lblDay.setText(String.format("%d of %d", SpaceExplorerGui.currentDay, SpaceExplorerGui.gameDuration));
        lblOrbiting.setText(SpaceExplorerGui.currentPlanet.toString());
        lblMissingParts.setText(String.valueOf(SpaceExplorerGui.spaceShip.getMissingParts()));
        lblBalance.setText("$" + SpaceExplorerGui.spaceShip.getSpaceBucks());
        lblShipHealth.setText(String.format("%d of %d", SpaceExplorerGui.spaceShip.getShieldCount(),
                SpaceExplorerGui.spaceShip.getMaxShieldCount()));
    }

    private void reloadState() {
        panelRoot.revalidate();
        panelRoot.repaint();
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
        panelRoot = new JPanel();
        panelRoot.setLayout(new GridBagLayout());
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setMaximumSize(new Dimension(500, 2147483647));
        panel1.setPreferredSize(new Dimension(500, 189));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 15.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 20, 20, 5);
        panelRoot.add(panel1, gbc);
        tabbedContentMenu = new JTabbedPane();
        Font tabbedContentMenuFont = this.$$$getFont$$$("Droid Sans Mono", -1, 14, tabbedContentMenu.getFont());
        if (tabbedContentMenuFont != null) tabbedContentMenu.setFont(tabbedContentMenuFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(tabbedContentMenu, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        tabbedContentMenu.addTab("Crew Members", panel2);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(scrollPane1, gbc);
        listCrew = new JList();
        listCrew.setFixedCellHeight(30);
        Font listCrewFont = this.$$$getFont$$$("Droid Sans Mono", -1, 14, listCrew.getFont());
        if (listCrewFont != null) listCrew.setFont(listCrewFont);
        listCrew.setMaximumSize(new Dimension(0, 0));
        listCrew.setVisibleRowCount(4);
        scrollPane1.setViewportView(listCrew);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        tabbedContentMenu.addTab("Food Items", panel3);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(scrollPane2, gbc);
        listFoodItems = new JList();
        listFoodItems.setFixedCellHeight(30);
        Font listFoodItemsFont = this.$$$getFont$$$("Droid Sans Mono", -1, 14, listFoodItems.getFont());
        if (listFoodItemsFont != null) listFoodItems.setFont(listFoodItemsFont);
        scrollPane2.setViewportView(listFoodItems);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        tabbedContentMenu.addTab("Medical Supplies", panel4);
        final JScrollPane scrollPane3 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel4.add(scrollPane3, gbc);
        listMedicalSupplies = new JList();
        listMedicalSupplies.setFixedCellHeight(30);
        Font listMedicalSuppliesFont = this.$$$getFont$$$("Droid Sans Mono", -1, 14, listMedicalSupplies.getFont());
        if (listMedicalSuppliesFont != null) listMedicalSupplies.setFont(listMedicalSuppliesFont);
        scrollPane3.setViewportView(listMedicalSupplies);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        tabbedContentMenu.addTab("Planets", panel5);
        final JScrollPane scrollPane4 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add(scrollPane4, gbc);
        listPlanets = new JList();
        listPlanets.setFixedCellHeight(30);
        Font listPlanetsFont = this.$$$getFont$$$("Droid Sans Mono", -1, 14, listPlanets.getFont());
        if (listPlanetsFont != null) listPlanets.setFont(listPlanetsFont);
        scrollPane4.setViewportView(listPlanets);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 20, 20, 20);
        panelRoot.add(panel6, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Droid Sans Mono", -1, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Orbiting Planet:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 10, 5);
        panel6.add(label1, gbc);
        lblOrbiting = new JLabel();
        Font lblOrbitingFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, lblOrbiting.getFont());
        if (lblOrbitingFont != null) lblOrbiting.setFont(lblOrbitingFont);
        lblOrbiting.setText("XXX-XXX");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(lblOrbiting, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Droid Sans Mono", -1, 18, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Missing Parts:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 10, 5);
        panel6.add(label2, gbc);
        lblMissingParts = new JLabel();
        Font lblMissingPartsFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, lblMissingParts.getFont());
        if (lblMissingPartsFont != null) lblMissingParts.setFont(lblMissingPartsFont);
        lblMissingParts.setText("X");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(lblMissingParts, gbc);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("Droid Sans Mono", -1, 18, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Day:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 10, 5);
        panel6.add(label3, gbc);
        lblDay = new JLabel();
        Font lblDayFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, lblDay.getFont());
        if (lblDayFont != null) lblDay.setFont(lblDayFont);
        lblDay.setText("X");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(lblDay, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel6.add(spacer1, gbc);
        final JSeparator separator1 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        panel6.add(separator1, gbc);
        btnNextDay = new JButton();
        Font btnNextDayFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, btnNextDay.getFont());
        if (btnNextDayFont != null) btnNextDay.setFont(btnNextDayFont);
        btnNextDay.setText("Next Day");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        panel6.add(btnNextDay, gbc);
        btnPerformAction = new JButton();
        btnPerformAction.setEnabled(false);
        Font btnPerformActionFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, btnPerformAction.getFont());
        if (btnPerformActionFont != null) btnPerformAction.setFont(btnPerformActionFont);
        btnPerformAction.setText("Perform  Crew Action");
        btnPerformAction.setToolTipText("Select a crew member to perform an action!");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        panel6.add(btnPerformAction, gbc);
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("Droid Sans Mono", -1, 18, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Balance:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 10, 5);
        panel6.add(label4, gbc);
        lblBalance = new JLabel();
        Font lblBalanceFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, lblBalance.getFont());
        if (lblBalanceFont != null) lblBalance.setFont(lblBalanceFont);
        lblBalance.setText("$XX");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(lblBalance, gbc);
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("Droid Sans Mono", -1, 18, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Ship Health:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 10, 5);
        panel6.add(label5, gbc);
        lblShipHealth = new JLabel();
        Font lblShipHealthFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, lblShipHealth.getFont());
        if (lblShipHealthFont != null) lblShipHealth.setFont(lblShipHealthFont);
        lblShipHealth.setText("X");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(lblShipHealth, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelRoot.add(panel7, gbc);
        lblSpaceShipName = new JLabel();
        Font lblSpaceShipNameFont = this.$$$getFont$$$("Droid Sans Mono", -1, 36, lblSpaceShipName.getFont());
        if (lblSpaceShipNameFont != null) lblSpaceShipName.setFont(lblSpaceShipNameFont);
        lblSpaceShipName.setText("#SpaceShip");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel7.add(lblSpaceShipName, gbc);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelRoot.add(panel8, gbc);
        btnSave = new JButton();
        btnSave.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 20);
        panel8.add(btnSave, gbc);
        final JSeparator separator2 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 20, 0, 20);
        panelRoot.add(separator2, gbc);
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
        return panelRoot;
    }

}
