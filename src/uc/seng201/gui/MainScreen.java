package uc.seng201.gui;

import uc.seng201.GameState;
import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.actions.ActionSleep;
import uc.seng201.utils.SavedGameFileFilter;
import uc.seng201.utils.observerable.Event;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.IOException;
import java.util.Iterator;

class MainScreen extends ScreenComponent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel panelRoot;
    private JLabel lblSpaceShipName;
    private JTabbedPane tabbedContentMenu;
    private JList<CrewMemberModelEntry> listCrew;
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
    private JLabel currentScoreLabel;

    private DefaultListModel<CrewMemberModelEntry> crewMemberDefaultListModel = new DefaultListModel<>();
    private DefaultListModel<String> listMedicalSuppliesModel = new DefaultListModel<>();
    private DefaultListModel<String> listFoodItemsModel = new DefaultListModel<>();
    private DefaultListModel<String> listPlanetsModel = new DefaultListModel<>();

    private GameState gameState;

    MainScreen(GameState gameState) {
        this.gameState = gameState;

        btnNextDay.addActionListener(e -> onNextDay());
        listCrew.addListSelectionListener(this::onCrewMemberSelection);
        btnPerformAction.addActionListener(e -> onPerformAction());
        btnSpaceTraders.addActionListener(e -> onTrade());
        btnSave.addActionListener(e -> onSave());
        btnInspect.addActionListener(e -> onInspect());

        listCrew.setModel(crewMemberDefaultListModel);

        listFoodItems.setModel(listFoodItemsModel);
        listMedicalSupplies.setModel(listMedicalSuppliesModel);
        listPlanets.setModel(listPlanetsModel);

        updateInfoPane();

    }

    private void onInspect() {
        JDialog inspectCrewMember = new InspectCrewMember(listCrew.getSelectedValue().crewMember);
        inspectCrewMember.setSize(700, 500);
        inspectCrewMember.setResizable(false);
        inspectCrewMember.setLocationRelativeTo(this);
        inspectCrewMember.setVisible(true);
    }

    private void onTrade() {
        JDialog traders = new Traders(gameState.getTrader().getAvailableItems(), gameState.getSpaceShip().getBalance());
        traders.setSize(600, 400);
        traders.setLocationRelativeTo(this);
        traders.setVisible(true);
        updateTablesModels();
        updateInfoPane();
        panelRoot.repaint();
    }

    @Override
    public JComponent getRootComponent() {
        return panelRoot;
    }

    /**
     * Handler for the save button. When the user tries to save the game a popup
     * is displayed asking for a location to save the game file. If the user provides
     * a file location then the current game state is saved to that file location. The
     * user may exit or cancel the popup and no file will be saved. If the game-state
     * cannot be saved the user is notified.
     */
    private void onSave() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setFileFilter(new SavedGameFileFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        int success = fileChooser.showSaveDialog(this);
        if (success == JFileChooser.APPROVE_OPTION) {
            String fileLocation = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                GameState.saveState(gameState, fileLocation);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save!",
                        "Failed", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Handler for the onClick event of the next day button. Checks if the
     * user does want to move to the next day. If moving to the next day
     * then all the observers of the event "START_DAY" are notified. The UI
     * is then repainted to reflect any changes that have been made.
     */
    private void onNextDay() {
        if (!isNextDayNeeded()) {
            return;
        }
        // Notify observers that we are moving to the next day.
        SpaceExplorer.eventManager.notifyObservers(Event.START_DAY, gameState);

        forceRequiredActions();
        // Update the UI to reflect any changes.
        updateTablesModels();
        updateInfoPane();
        repaint();
    }

    /**
     * Checks if there are still actions remaining today for the crew, If there are
     * remaining actions, provides a popup confirming if the user does want to
     * move to the next day. If there are no available actions then no popup is shown.
     *
     * @return true if the user does want to move to the next day.
     */
    private boolean isNextDayNeeded() {
        if (gameState.getSpaceShip().hasCrewActionsRemaining()) {
            int confirmed = JOptionPane.showConfirmDialog(this,
                    "Do you really want to move to the next day? Some crew members can still perform actions!",
                    "Actions Left Today", JOptionPane.YES_NO_OPTION);
            return confirmed == 0;
        }
        return true;
    }

    private void forceRequiredActions() {
        gameState.getSpaceShip().getShipCrew().forEach(crewMember -> {
            if (crewMember.getTiredness() == crewMember.getMaxTiredness()) {
                new ActionSleep().perform(gameState, null, crewMember);
                JOptionPane.showMessageDialog(this, crewMember.getName() +
                        " was overcome with tiredness and forced to spend the day sleeping.");
            }
        });
    }

    private void onPerformAction() {
        JDialog performAction = new PerformAction(gameState, listCrew.getSelectedValue().crewMember);
        performAction.setSize(450, 350);
        performAction.setLocationRelativeTo(this);
        performAction.setVisible(true);

        SpaceExplorer.eventManager.notifyObservers(Event.CREW_MEMBER_ACTION);

        updateInfoPane();
        panelRoot.repaint();
    }

    /**
     * Sets the selected crew member to the first crew member who has an available action for the
     * current day. The action button is then activated. If no crew members have actions the first
     * crew member in the list is selected but the action button is not activated.
     */
    private void defaultSelectedCrewMember() {
        for (Iterator<CrewMemberModelEntry> iterator = crewMemberDefaultListModel.elements().asIterator(); iterator.hasNext(); ) {
            CrewMember crewMember = iterator.next().crewMember;
            if (crewMember.getActionsLeftToday() > 0) {
                listCrew.setSelectedValue(crewMember.description(), true);
                btnPerformAction.setEnabled(true);
                return;
            }
        }
        // No crew actions left but select one for the inspect button
        listCrew.setSelectedIndex(0);
        btnPerformAction.setEnabled(false);
    }

    private void onCrewMemberSelection(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting() && listCrew.getSelectedIndex() != -1) {
            btnPerformAction.setEnabled(listCrew.getSelectedValue().crewMember.canPerformActions());
        }
    }

    private void updateTablesModels() {
        listFoodItemsModel.clear();
        listMedicalSuppliesModel.clear();
        listPlanetsModel.clear();
        crewMemberDefaultListModel.clear();

        gameState.getSpaceShip().getShipCrew().forEach(crewMember -> crewMemberDefaultListModel.addElement(
                new CrewMemberModelEntry(crewMember)));

        gameState.getPlanets().forEach(planet -> listPlanetsModel.addElement(
                planet.description()));

        gameState.getSpaceShip().getShipItems().forEach((item, qty) -> {
            switch (item.getItemType()) {
                case FOOD:
                    listFoodItemsModel.addElement(String.format("%d x %s - %s", qty, item.toString(),
                            item.getItemDescription()));
                    break;
                case MEDICAL:
                    listMedicalSuppliesModel.addElement(String.format("%d x %s - %s", qty, item.toString(),
                            item.getItemDescription()));
                    break;
            }
        });

        defaultSelectedCrewMember();
    }

    private void updateInfoPane() {
        updateTablesModels();
        lblSpaceShipName.setText(gameState.getSpaceShip().getShipName());
        lblDay.setText(String.format("%d of %d", gameState.getCurrentDay(),
                gameState.getDuration()));
        lblOrbiting.setText(gameState.getCurrentPlanet().toString());
        lblMissingParts.setText(String.valueOf(gameState.getSpaceShip().getMissingParts()));
        lblBalance.setText("$" + gameState.getSpaceShip().getBalance());
        lblShipHealth.setText(String.format("%d", gameState.getSpaceShip().getShieldCount()));

        gameState.computeScore();
        currentScoreLabel.setText(String.valueOf(gameState.getScore()));
    }

    final class CrewMemberModelEntry {
        final CrewMember crewMember;

        CrewMemberModelEntry(CrewMember crewMember) {
            this.crewMember = crewMember;
        }

        @Override
        public String toString() {
            return crewMember.description();
        }
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
        gbc.gridy = 6;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel6.add(spacer1, gbc);
        final JSeparator separator1 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
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
        gbc.gridy = 12;
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
        gbc.gridy = 10;
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
        gbc.gridy = 11;
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
        gbc.gridy = 7;
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
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 0, 0, 0);
        panel6.add(btnInspect, gbc);
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("Droid Sans Mono", -1, 18, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Current Score:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 10, 5);
        panel6.add(label6, gbc);
        currentScoreLabel = new JLabel();
        Font currentScoreLabelFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, currentScoreLabel.getFont());
        if (currentScoreLabelFont != null) currentScoreLabel.setFont(currentScoreLabelFont);
        currentScoreLabel.setText("X");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(currentScoreLabel, gbc);
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
