package uc.seng201.gui;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.events.IRandomEvent;
import uc.seng201.events.RandomEvent;
import uc.seng201.helpers.Helpers;
import uc.seng201.helpers.SavedGameFileFilter;
import uc.seng201.helpers.StateActions;
import uc.seng201.events.EventTrigger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.IOException;
import java.util.Iterator;

class MainScreen extends ScreenComponent {

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
    private JButton btnHelp;
    private JButton btnSpaceTraders;
    private JButton btnInspect;

    private DefaultListModel<CrewMember> listCrewModal = new DefaultListModel<>();
    private DefaultListModel<String> listMedicalSuppliesModel = new DefaultListModel<>();
    private DefaultListModel<String> listFoodItemsModel = new DefaultListModel<>();
    private DefaultListModel<String> listPlanetsModel = new DefaultListModel<>();

    private SpaceExplorer spaceExplorer;

    MainScreen(SpaceExplorer spaceExplorer) {
        this.spaceExplorer = spaceExplorer;

        initialiseTables();
        updateInfoPane();

        btnNextDay.addActionListener(e -> onNextDay());

        listCrew.addListSelectionListener(this::onCrewMemberSelection);

        btnPerformAction.addActionListener(e -> onPerformAction());

        btnSpaceTraders.addActionListener(e -> onTrade());

        btnSave.addActionListener(e -> onSave());

        btnInspect.addActionListener(e -> onInspect());

//        btnHelp.addActionListener(e -> {
//            // TODO: Generate the help message
//            JOptionPane.showMessageDialog(this, Helpers.listToString(Modifications.values()));
//        });

        defaultSelectedCrewMember();

    }

    private void onInspect() {
        JDialog inspectCrewMember = new InspectCrewMember(listCrew.getSelectedValue());
        inspectCrewMember.setSize(700, 500);
        inspectCrewMember.setResizable(false);
        inspectCrewMember.setLocationRelativeTo(spaceExplorer.getRootFrame());
        inspectCrewMember.setVisible(true);
    }

    private void onTrade() {
        JDialog traders = new Traders(this.spaceExplorer.getGameState());
        traders.setSize(600, 400);
        traders.setLocationRelativeTo(this.spaceExplorer.getRootFrame());
        traders.setVisible(true);
        updateTablesModels();
        updateInfoPane();
        panelRoot.repaint();
    }

    @Override
    public JComponent getRootComponent() {
        return panelRoot;
    }

    private void onSave() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileFilter(new SavedGameFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        int success = fileChooser.showSaveDialog(this);
        if (success == JFileChooser.APPROVE_OPTION) {
            String fileLocation = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                StateActions.saveState(this.spaceExplorer.getGameState(), fileLocation);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save!",
                        "Failed", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void onNextDay() {
        if (!isNextDayNeeded()) {
            return;
        }
        if (this.spaceExplorer.getGameState().hasNextDay()) {
            this.spaceExplorer.getGameState().nextDay();
            updateInfoPane();
        } else {
            SpaceExplorer.failedGame("On no! It seems you have failed to rebuild your ship in time! Err....");
            return;
        }
        if (Helpers.randomGenerator.nextBoolean()) {
            RandomEvent event = IRandomEvent.eventToTrigger(EventTrigger.START_DAY);
            event.getInstance().onTrigger(this.spaceExplorer.getGameState().getSpaceShip());
            JOptionPane.showMessageDialog(this, event.getEventDescription());
        }
        forceRequiredActions();
        updateTablesModels();
        defaultSelectedCrewMember();
        panelRoot.repaint();
    }

    private boolean isNextDayNeeded() {
        if (this.spaceExplorer.getGameState().getSpaceShip().hasCrewActionsRemaining()) {
            int confirmed = JOptionPane.showConfirmDialog(this,
                    "Do you really want to move to the next day? Some crew members can still perform actions!",
                    "Actions Left Today", JOptionPane.YES_NO_OPTION);
            return confirmed == 0;
        }
        return true;
    }

    private void forceRequiredActions() {
        spaceExplorer.getGameState().getSpaceShip().getShipCrew().forEach(crewMember -> {
            if (crewMember.getTiredness() == crewMember.getMaxTiredness()) {
                crewMember.alterTiredness(0 - crewMember.getMaxTiredness());
                crewMember.performAction();
                JOptionPane.showMessageDialog(this, crewMember.getName() +
                        " was overcome with tiredness and forced to spend the day sleeping.");
            }
        });

    }

    private void onPerformAction() {
        JDialog performAction = new PerformAction(this.spaceExplorer.getGameState(),
                listCrewModal.get(listCrew.getSelectedIndex()));
        performAction.setSize(450, 350);
        performAction.setLocationRelativeTo(spaceExplorer.getRootFrame());
        performAction.setVisible(true);

        if (!this.spaceExplorer.getGameState().isMissingShipParts()) {
            SpaceExplorer.completedGame();
        }

        updateInfoPane();
        defaultSelectedCrewMember();
        panelRoot.repaint();
    }

    private void defaultSelectedCrewMember() {
        for (Iterator<CrewMember> iterator = listCrewModal.elements().asIterator(); iterator.hasNext(); ) {
            CrewMember crewMember = iterator.next();
            if (crewMember.getActionsLeftToday() > 0) {
                listCrew.setSelectedValue(crewMember, true);
                btnPerformAction.setEnabled(true);
                return;
            }
        }
        listCrew.setSelectedIndex(0);
        btnPerformAction.setEnabled(false);
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
        listCrewModal.addAll(this.spaceExplorer.getGameState().getSpaceShip().getShipCrew());
        listCrew.setModel(listCrewModal);
        defaultSelectedCrewMember();

        listFoodItems.setModel(listFoodItemsModel);
        listMedicalSupplies.setModel(listMedicalSuppliesModel);
        listPlanets.setModel(listPlanetsModel);
    }

    private void updateTablesModels() {
        listFoodItemsModel.clear();
        listMedicalSuppliesModel.clear();
        listPlanetsModel.clear();

        this.spaceExplorer.getGameState().getPlanets().forEach(planet -> listPlanetsModel.addElement(
                planet.description()));
        this.spaceExplorer.getGameState().getSpaceShip().getShipItems().forEach(item -> {
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

    private void updateInfoPane() {
        updateTablesModels();
        lblSpaceShipName.setText(this.spaceExplorer.getGameState().getSpaceShip().getShipName());
        lblDay.setText(String.format("%d of %d", this.spaceExplorer.getGameState().getCurrentDay(),
                this.spaceExplorer.getGameState().getDuration()));
        lblOrbiting.setText(this.spaceExplorer.getGameState().getCurrentPlanet().toString());
        lblMissingParts.setText(String.valueOf(this.spaceExplorer.getGameState().getSpaceShip().getMissingParts()));
        lblBalance.setText("$" + this.spaceExplorer.getGameState().getSpaceShip().getSpaceBucks());
        lblShipHealth.setText(String.format("%d", this.spaceExplorer.getGameState().getSpaceShip().getShieldCount()));
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
        tabbedContentMenu.setEnabled(true);
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
        listFoodItems.setEnabled(true);
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
        listMedicalSupplies.setEnabled(true);
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
        listPlanets.setEnabled(true);
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
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel6.add(separator1, gbc);
        btnNextDay = new JButton();
        Font btnNextDayFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, btnNextDay.getFont());
        if (btnNextDayFont != null) btnNextDay.setFont(btnNextDayFont);
        btnNextDay.setText("Next Day");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel6.add(btnNextDay, gbc);
        btnPerformAction = new JButton();
        btnPerformAction.setEnabled(true);
        Font btnPerformActionFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, btnPerformAction.getFont());
        if (btnPerformActionFont != null) btnPerformAction.setFont(btnPerformActionFont);
        btnPerformAction.setText("Action");
        btnPerformAction.setToolTipText("Perform actions that require crew members.");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 0, 0, 0);
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
        final JSeparator separator2 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel6.add(separator2, gbc);
        btnSpaceTraders = new JButton();
        btnSpaceTraders.setEnabled(true);
        Font btnSpaceTradersFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, btnSpaceTraders.getFont());
        if (btnSpaceTradersFont != null) btnSpaceTraders.setFont(btnSpaceTradersFont);
        btnSpaceTraders.setText("Space Traders");
        btnSpaceTraders.setToolTipText("Visit your local space traders.");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 2, 0);
        panel6.add(btnSpaceTraders, gbc);
        btnInspect = new JButton();
        btnInspect.setEnabled(true);
        Font btnInspectFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, btnInspect.getFont());
        if (btnInspectFont != null) btnInspect.setFont(btnInspectFont);
        btnInspect.setText("Inspect");
        btnInspect.setToolTipText("Perform actions that require crew members.");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 0, 0, 0);
        panel6.add(btnInspect, gbc);
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
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 2, 0, 20);
        panel8.add(btnSave, gbc);
        btnHelp = new JButton();
        btnHelp.setText("Help");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 2);
        panel8.add(btnHelp, gbc);
        final JSeparator separator3 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 20, 0, 20);
        panelRoot.add(separator3, gbc);
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
