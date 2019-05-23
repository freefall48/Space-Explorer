package uc.seng201.gui;

import uc.seng201.crew.CrewMember;
import uc.seng201.environment.GameEnvironment;
import uc.seng201.environment.GameState;
import uc.seng201.utils.SavedGameFileFilter;
import uc.seng201.utils.observerable.Event;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Iterator;

class MainScreen extends ScreenComponent {

    /**
     * Root panel
     */
    private JPanel panelRoot;
    /**
     * Displays the ships name
     */
    private JLabel spaceShipLabel;
    /**
     * Provides tabs to display the crew, items and planets.
     */
    private JTabbedPane tabbedContentMenu;
    /**
     * Display the current crew members.
     */
    private JList<CrewMemberModelEntry> listCrew;
    /**
     * Displays information about the current and total days.
     */
    private JLabel dayLabel;
    /**
     * Displays the current planet being orbited.
     */
    private JLabel orbitingLabel;
    /**
     * Displays how many parts are still missing.
     */
    private JLabel missingPartsLabel;
    /**
     * Displays the space ships balance.
     */
    private JLabel balanceLabel;
    /**
     * Button to perform an action as a crew member.
     */
    private JButton actionButton;
    /**
     * Button that saves the game.
     */
    private JButton saveButton;
    /**
     * Button that moves to the next day.
     */
    private JButton nextDayButton;
    /**
     * Displays information about the ships shields.
     */
    private JLabel shipShieldsLabel;
    /**
     * Displays the ships current food items.
     */
    private JList<String> listFoodItems;
    /**
     * Displays the ships current medical items.
     */
    private JList<String> listMedicalSupplies;
    /**
     * Displays the planets available in this game.
     */
    private JList<String> listPlanets;
    /**
     * Button opens the space traders.
     */
    private JButton spaceTradersButton;
    /**
     * Button allows the user to inspect details about one crew member.
     */
    private JButton inspectButton;
    /**
     * Displays the users current score.
     */
    private JLabel currentScoreLabel;
    /**
     * Displays the ships health.
     */
    private JLabel shipHealthLabel;

    /**
     * Model backing the user list.
     */
    private DefaultListModel<CrewMemberModelEntry> crewMemberDefaultListModel = new DefaultListModel<>();
    /**
     * Model backing the medical supplies list.
     */
    private DefaultListModel<String> listMedicalSuppliesModel = new DefaultListModel<>();
    /**
     * Model backing the food items list.
     */
    private DefaultListModel<String> listFoodItemsModel = new DefaultListModel<>();
    /**
     * Model backing the planets list.
     */
    private DefaultListModel<String> listPlanetsModel = new DefaultListModel<>();

    /**
     * The current game state.
     */
    private GameState gameState;

    MainScreen(GameState gameState) {
        this.gameState = gameState;

        nextDayButton.addActionListener(e -> onNextDay());
        listCrew.addListSelectionListener(this::onCrewMemberSelection);

        // Add easy of life features. Double-click for action dialog and Right-click for inspect dialog.
        listCrew.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (SwingUtilities.isRightMouseButton(e)) {
                    listCrew.setSelectedIndex(listCrew.locationToIndex(e.getPoint()));
                    onInspect();
                } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    onPerformAction();
                }
            }

            /* Prevent the need to double right-click by setting the focused component as the list when
               mouse is over the list.*/
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                listCrew.requestFocus();
            }
        });
        // Add listeners for button clicks.
        actionButton.addActionListener(e -> onPerformAction());
        spaceTradersButton.addActionListener(e -> onTrade());
        saveButton.addActionListener(e -> onSave());
        inspectButton.addActionListener(e -> onInspect());

        // Set the models for the lists to follow.
        listCrew.setModel(crewMemberDefaultListModel);
        listFoodItems.setModel(listFoodItemsModel);
        listMedicalSupplies.setModel(listMedicalSuppliesModel);
        listPlanets.setModel(listPlanetsModel);

        paintInfoPanel();
        computeTablesModels();

    }

    /**
     * Handler for when the inspect event occurs. Creates a new popup that
     * displays information about the selected crew member.
     */
    private void onInspect() {
        JDialog inspectCrewMember = new InspectCrewMember(listCrew.getSelectedValue().crewMember);
        inspectCrewMember.setSize(700, 500);
        inspectCrewMember.setResizable(false);
        inspectCrewMember.setLocationRelativeTo(this);
        inspectCrewMember.setVisible(true);
    }

    /**
     * Handler for when the trade event occurs. Creates a pop up of the current
     * space traders.
     */
    private void onTrade() {
        JDialog traders = new Traders(gameState);
        traders.setSize(600, 400);
        traders.setLocationRelativeTo(this);
        traders.setVisible(true);
        computeTablesModels();
        paintInfoPanel();
        panelRoot.repaint();
    }

    /**
     * Returns the root component of this screen. All other elements are painted onto this
     * panel.
     *
     * @return the root panel of this screen.
     */
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
        /*
        The user must be able to move onto the next day at any point but just warn them if they
        still have actions available.
         */
        if (!isNextDayNeeded()) {
            return;
        }
        // Notify observers that we are moving to the next day.
        GameEnvironment.EVENT_MANAGER.notifyObservers(Event.START_DAY, gameState);

        // Update the UI to reflect any changes.
        computeTablesModels();
        paintInfoPanel();
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

    /**
     * Creates a perform action window for the selected crew member. This screen
     * is then
     */
    private void onPerformAction() {

        //Create the window and configure it as required.
        JDialog performAction = new PerformAction(gameState, listCrew.getSelectedValue().crewMember);
        performAction.setSize(450, 350);
        performAction.setLocationRelativeTo(this);
        performAction.setVisible(true);

        // Update the display to reflect the new possible state after an action.
        paintInfoPanel();
        computeTablesModels();
    }

    /**
     * Sets the selected crew member to the first crew member who has an available action for the
     * current day. The action button is then activated. If no crew members have actions the first
     * crew member in the list is selected but the action button is not activated.
     */
    private void defaultSelectedCrewMember() {
        for (Iterator<CrewMemberModelEntry> iterator = crewMemberDefaultListModel.elements().asIterator(); iterator.hasNext(); ) {
            CrewMemberModelEntry crewMemberModelEntry = iterator.next();
            if (crewMemberModelEntry.crewMember.getActionsLeftToday() > 0) {
                listCrew.setSelectedValue(crewMemberModelEntry, true);
                actionButton.setEnabled(true);
                return;
            }
        }
        // No crew actions left but select one for the inspect button
        listCrew.setSelectedIndex(0);
        actionButton.setEnabled(false);
    }

    private void onCrewMemberSelection(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting() && listCrew.getSelectedIndex() != -1) {
            actionButton.setEnabled(listCrew.getSelectedValue().crewMember.canPerformActions());
        }
    }

    /**
     * Generates the models used to back the lists on the panel.
     */
    private void computeTablesModels() {

        /*
         Clean out the models as the information contains is likely stale.
         */
        listFoodItemsModel.clear();
        listMedicalSuppliesModel.clear();
        listPlanetsModel.clear();
        crewMemberDefaultListModel.clear();

        // Add all the crew members.
        gameState.getSpaceShip().getShipCrew().forEach(crewMember -> crewMemberDefaultListModel.addElement(
                new CrewMemberModelEntry(crewMember)));

        // Add all the planets.
        gameState.getPlanets().forEach(planet -> listPlanetsModel.addElement(
                planet.description()));

        // Although they are all items, split them apart to make it easy for the user to view.
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

        // Set the default crew member to displayed and make sure the list is the correct width.
        revalidate();
        repaint();
        defaultSelectedCrewMember();
    }

    /**
     * Repaints the information about the game-state that is being displayed.
     */
    private void paintInfoPanel() {
        spaceShipLabel.setText(gameState.getSpaceShip().getShipName());
        dayLabel.setText(String.format("%d of %d", gameState.getCurrentDay(),
                gameState.getDuration()));
        orbitingLabel.setText(gameState.getCurrentPlanet().toString());
        missingPartsLabel.setText(String.valueOf(gameState.getSpaceShip().getMissingParts()));
        balanceLabel.setText("$" + gameState.getSpaceShip().getBalance());
        shipShieldsLabel.setText(String.format("%d", gameState.getSpaceShip().getShieldCount()));
        shipHealthLabel.setText(String.format("%d/%d", gameState.getSpaceShip().getShipHealth(),
                gameState.getSpaceShip().getShipHealthMax()));

        gameState.computeScore();
        currentScoreLabel.setText(String.valueOf(gameState.getScore()));
        repaint();
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
        listCrew.setSelectionMode(0);
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
        orbitingLabel = new JLabel();
        Font orbitingLabelFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, orbitingLabel.getFont());
        if (orbitingLabelFont != null) orbitingLabel.setFont(orbitingLabelFont);
        orbitingLabel.setText("XXX-XXX");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(orbitingLabel, gbc);
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
        missingPartsLabel = new JLabel();
        Font missingPartsLabelFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, missingPartsLabel.getFont());
        if (missingPartsLabelFont != null) missingPartsLabel.setFont(missingPartsLabelFont);
        missingPartsLabel.setText("X");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(missingPartsLabel, gbc);
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
        dayLabel = new JLabel();
        Font dayLabelFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, dayLabel.getFont());
        if (dayLabelFont != null) dayLabel.setFont(dayLabelFont);
        dayLabel.setText("X");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(dayLabel, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel6.add(spacer1, gbc);
        final JSeparator separator1 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel6.add(separator1, gbc);
        nextDayButton = new JButton();
        Font nextDayButtonFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, nextDayButton.getFont());
        if (nextDayButtonFont != null) nextDayButton.setFont(nextDayButtonFont);
        nextDayButton.setText("Next Day");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel6.add(nextDayButton, gbc);
        actionButton = new JButton();
        actionButton.setEnabled(true);
        Font actionButtonFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, actionButton.getFont());
        if (actionButtonFont != null) actionButton.setFont(actionButtonFont);
        actionButton.setText("Action");
        actionButton.setToolTipText("Perform actions that require crew members.");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 0, 0, 0);
        panel6.add(actionButton, gbc);
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
        balanceLabel = new JLabel();
        Font balanceLabelFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, balanceLabel.getFont());
        if (balanceLabelFont != null) balanceLabel.setFont(balanceLabelFont);
        balanceLabel.setText("$XX");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(balanceLabel, gbc);
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("Droid Sans Mono", -1, 18, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Shields:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 10, 5);
        panel6.add(label5, gbc);
        shipShieldsLabel = new JLabel();
        Font shipShieldsLabelFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, shipShieldsLabel.getFont());
        if (shipShieldsLabelFont != null) shipShieldsLabel.setFont(shipShieldsLabelFont);
        shipShieldsLabel.setText("X");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(shipShieldsLabel, gbc);
        final JSeparator separator2 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel6.add(separator2, gbc);
        spaceTradersButton = new JButton();
        spaceTradersButton.setEnabled(true);
        Font spaceTradersButtonFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, spaceTradersButton.getFont());
        if (spaceTradersButtonFont != null) spaceTradersButton.setFont(spaceTradersButtonFont);
        spaceTradersButton.setText("Space Traders");
        spaceTradersButton.setToolTipText("Visit your local space traders.");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 2, 0);
        panel6.add(spaceTradersButton, gbc);
        inspectButton = new JButton();
        inspectButton.setEnabled(true);
        Font inspectButtonFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, inspectButton.getFont());
        if (inspectButtonFont != null) inspectButton.setFont(inspectButtonFont);
        inspectButton.setText("Inspect");
        inspectButton.setToolTipText("Perform actions that require crew members.");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 0, 0, 0);
        panel6.add(inspectButton, gbc);
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$("Droid Sans Mono", -1, 18, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Current Score:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 10, 5);
        panel6.add(label6, gbc);
        currentScoreLabel = new JLabel();
        Font currentScoreLabelFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, currentScoreLabel.getFont());
        if (currentScoreLabelFont != null) currentScoreLabel.setFont(currentScoreLabelFont);
        currentScoreLabel.setText("X");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(currentScoreLabel, gbc);
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$("Droid Sans Mono", -1, 18, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("Health:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 10, 5);
        panel6.add(label7, gbc);
        shipHealthLabel = new JLabel();
        Font shipHealthLabelFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 18, shipHealthLabel.getFont());
        if (shipHealthLabelFont != null) shipHealthLabel.setFont(shipHealthLabelFont);
        shipHealthLabel.setText("X");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel6.add(shipHealthLabel, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelRoot.add(panel7, gbc);
        spaceShipLabel = new JLabel();
        Font spaceShipLabelFont = this.$$$getFont$$$("Droid Sans Mono", -1, 36, spaceShipLabel.getFont());
        if (spaceShipLabelFont != null) spaceShipLabel.setFont(spaceShipLabelFont);
        spaceShipLabel.setText("#SpaceShip");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel7.add(spaceShipLabel, gbc);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelRoot.add(panel8, gbc);
        saveButton = new JButton();
        saveButton.setHorizontalAlignment(0);
        saveButton.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 2, 0, 20);
        panel8.add(saveButton, gbc);
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

    /**
     * Easy way to hold the crew members within the list model. Final as there is
     * no need to extend this class.
     */
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

}
