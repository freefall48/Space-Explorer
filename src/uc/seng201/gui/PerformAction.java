package uc.seng201.gui;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.actions.CrewAction;
import uc.seng201.environment.GameEnvironment;
import uc.seng201.environment.GameState;
import uc.seng201.items.SpaceItem;
import uc.seng201.utils.observerable.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * Provides the ability for the user to perform
 * actions as a crew member.
 */
public class PerformAction extends JDialog {
    /**
     * Root panel.
     */
    private JPanel contentPane;
    /**
     * Button to perform the action.
     */
    private JButton okButton;
    /**
     * Exit out the current dialog.
     */
    private JButton cancelButton;
    /**
     * Display the possible actions.
     */
    private JComboBox<String> actionsComboBox;
    /**
     * Display the name of the acting crew member.
     */
    private JLabel crewMemberNameLabel;
    /**
     * Displays what the current action will do.
     */
    private JLabel actionTextLabel;
    /**
     * Allows the user to enter additional information if needed.
     */
    private JComboBox<String> additionalInput1ComboBox;
    /**
     * Label for the first additional input.
     */
    private JLabel additionalInput1Label;
    /**
     * Label for the second additional input.
     */
    private JLabel additionalInput2Label;
    /**
     * Allows the user to enter additional information if needed.
     */
    private JComboBox<String> additionalInput2ComboBox;

    /**
     * The current game state.
     */
    private GameState gameState;
    /**
     * The main crew member who is performing the action.
     */
    private CrewMember primaryCrewMember;
    /**
     * Some actions may need 2 crew members to act. This is
     * the second acting crew member.
     */
    private CrewMember extraCrewMember;

    /**
     * Model that contains all the additional crew that can be used by actions
     * requiring more crew.
     */
    private DefaultComboBoxModel<String> additionalCrewModel = new DefaultComboBoxModel<>();
    /**
     * Model that backs the actions available list.
     */
    private DefaultComboBoxModel<String> availableActionsModel = new DefaultComboBoxModel<>();
    /**
     * Model that contains all the planets that the ship can travel to. Should not contain
     * the planet that is currently orbited.
     */
    private DefaultComboBoxModel<String> targetPlanetsModel = new DefaultComboBoxModel<>();
    /**
     * Model that contains all the items of the that can be consumed by the current crew
     * member.
     */
    private DefaultComboBoxModel<String> itemModel = new DefaultComboBoxModel<>();

    /**
     * Perform an action as a crew member.
     *
     * @param gameState  reference to the game state.
     * @param crewMember that is to perform the action.
     */
    PerformAction(GameState gameState, CrewMember crewMember) {
        this.gameState = gameState;
        this.primaryCrewMember = crewMember;

        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(okButton);

        crewMemberNameLabel.setText(crewMember.getName());
        actionsComboBox.setModel(availableActionsModel);
        CrewAction defaultAction = CrewAction.SEARCH;
        availableActionsModel.setSelectedItem(defaultAction);
        actionDialog(defaultAction);
        okButton.addActionListener(e -> onOK());
        cancelButton.addActionListener(e -> onCancel());
        actionsComboBox.addActionListener(e -> onActionSelected());
        additionalInput1ComboBox.addActionListener(e -> onAdditionalInput());
        additionalInput2ComboBox.addActionListener(e -> onAdditionalInput());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        // Generate the models and actions. Sets the default action to searching.
        computeListModels();
        computeAvailableActions();
        actionsComboBox.setSelectedItem(CrewAction.SEARCH.toString());
    }

    /**
     * When additional input is needed from the user. This method is called to update the
     * information displayed relating to the action to be performed.
     */
    private void onAdditionalInput() {
        CrewAction action = CrewAction.valueOf(actionsComboBox.getItemAt(actionsComboBox.getSelectedIndex()));
        actionDialog(action);
    }

    /**
     * Generates the models used to back the additional options for certain arguments. These only
     * need to be generated once per instance of this window.
     */
    private void computeListModels() {

        // Clean out any existing items. Prevents any possible duping of entries.
        additionalCrewModel.removeAllElements();
        availableActionsModel.removeAllElements();
        targetPlanetsModel.removeAllElements();
        itemModel.removeAllElements();

        // Add all crew members who are not the primary acting crew member.
        gameState.getSpaceShip().getShipCrew().forEach(additionalCrewMember -> {
            if (!additionalCrewMember.equals(primaryCrewMember) && additionalCrewMember.canPerformActions()) {
                additionalCrewModel.addElement(additionalCrewMember.toString());
            }
        });

        // Add all the planets that are not the current planet.
        gameState.getPlanets().forEach(planet -> {
            if (!planet.equals(this.gameState.getCurrentPlanet())) {
                targetPlanetsModel.addElement(planet.toString());
            }
        });

        // Add all the items that the ship has.
        gameState.getSpaceShip().getShipItems().forEach((item, qty) ->
                itemModel.addElement(String.format("%d x %s", qty, item.toString())));
    }

    /**
     * Generates the available actions that the user may do with the selected crew member. The
     * options are then added to the action model for selection.
     */
    private void computeAvailableActions() {
        CrewAction[] actionCache = CrewAction.values();

        // If the crew member is at their maximum tiredness they only action they can perform is sleep.
        if (primaryCrewMember.getTiredness() == primaryCrewMember.getMaxTiredness()) {
            availableActionsModel.addElement(CrewAction.SLEEP.toString());
            return;
        }

        // Add all actions. If the action requires 2 crew check to make sure that is possible.
        for (CrewAction action : actionCache) {
            if (action.getCrewRequired() == 1) {
                availableActionsModel.addElement(action.toString());
            } else if (additionalCrewModel.getSize() >= (action.getCrewRequired() - 1)) {
                availableActionsModel.addElement(action.toString());
            }
        }

        // Check if there are items available. If not remove the consume action as there is nothing to consume.
        boolean isItemsPresent = gameState.getSpaceShip().getShipItems().size() > 0;
        if (!isItemsPresent) {
            availableActionsModel.removeElement(CrewAction.CONSUME.toString());
        }
        // No point repairing the ship if its full health.
        if (gameState.getSpaceShip().getShipHealth() == gameState.getSpaceShip().getShipHealthMax()) {
            availableActionsModel.removeElement(CrewAction.REPAIR.toString());
        }
    }

    /**
     * Handler for the okay button. Gets the action and the crew members who are to perform the action.
     * The action is then performed.
     */
    private void onOK() {
        // Get the action we are performing.
        CrewAction actionToPerform = CrewAction.valueOf(actionsComboBox.getItemAt(actionsComboBox.getSelectedIndex()));

        // Set the crew members as required.
        if (actionToPerform.getCrewRequired() == 2) {
            String[] crewMemberDetails = additionalCrewModel.getSelectedItem().toString().split(" - ");
            this.extraCrewMember = this.gameState.getSpaceShip().crewMemberFromNameAndType(
                    crewMemberDetails[0], crewMemberDetails[1]);
        }

        // Perform the action and return control to the caller.
        performAction(actionToPerform);
        dispose();
    }

    /**
     * Gives control back to the caller.
     */
    private void onCancel() {
        dispose();
    }

    /**
     * Hide all the additional input fields.
     */
    private void hideAdditionalInput() {
        additionalInput1ComboBox.setVisible(false);
        additionalInput1Label.setVisible(false);
        additionalInput2Label.setVisible(false);
        additionalInput2ComboBox.setVisible(false);
    }

    /**
     * Sets the number of additional inputs required visible. Maximum additional
     * inputs is 2.
     *
     * @param additionalInputs number of additional inputs to display.
     */
    private void setAdditionalInputVisible(int additionalInputs) {
        switch (additionalInputs) {
            case 2:
                additionalInput2ComboBox.setVisible(true);
                additionalInput2Label.setVisible(true);
            case 1:
                additionalInput1ComboBox.setVisible(true);
                additionalInput1Label.setVisible(true);
                break;
        }
    }

    /**
     * Updates the additional input labels and options depending on the action that has been selected.
     */
    private void onActionSelected() {
        hideAdditionalInput();
        CrewAction actionToPerform = CrewAction.valueOf(actionsComboBox.getItemAt(actionsComboBox.getSelectedIndex()));
        switch (actionToPerform) {
            case PILOT:
                additionalInput1Label.setText("Co-pilot:");
                additionalInput1ComboBox.setModel(additionalCrewModel);
                additionalInput1ComboBox.setSelectedIndex(0);
                additionalInput2Label.setText("Destination Planet:");
                additionalInput2ComboBox.setModel(targetPlanetsModel);
                additionalInput2ComboBox.setSelectedIndex(0);
                setAdditionalInputVisible(2);
                break;
            case CONSUME:
                additionalInput1Label.setText("Snack on:");
                additionalInput1ComboBox.setModel(itemModel);
                additionalInput1ComboBox.setSelectedIndex(0);
                setAdditionalInputVisible(1);
                break;
        }
        actionDialog(actionToPerform);
    }

    /**
     * Updates the message displayed that informs the use of what will occur with their current selections. The
     * information displayed depends on the action to be performed. The description from the actions ENUM entry
     * is fetched and filled.
     *
     * @param action being performed.
     */
    private void actionDialog(CrewAction action) {
        switch (action) {
            case PILOT:
                if (additionalInput2ComboBox.getSelectedItem() != null) {
                    actionTextLabel.setText(String.format(CrewAction.PILOT.getActionText(), primaryCrewMember.getName(),
                            additionalInput1ComboBox.getSelectedItem(), this.gameState.getSpaceShip().getShipName(),
                            additionalInput2ComboBox.getSelectedItem()).replaceFirst(" - [a-zA-Z]*", ""));
                }
                break;
            case SEARCH:
                actionTextLabel.setText(String.format(CrewAction.SEARCH.getActionText(), primaryCrewMember.getName(),
                        this.gameState.getCurrentPlanet(), this.gameState.getCurrentPlanet().description()));
                break;
            case SLEEP:
                actionTextLabel.setText(String.format(CrewAction.SLEEP.getActionText(), primaryCrewMember.getName()));
                break;
            case CONSUME:
                actionTextLabel.setText(String.format(CrewAction.CONSUME.getActionText(), primaryCrewMember.getName(),
                        Objects.requireNonNull(additionalInput1ComboBox.getSelectedItem()).toString().replaceFirst(
                                "([0-9]+ x )", "")));
                break;
            case REPAIR:
                actionTextLabel.setText(String.format(CrewAction.REPAIR.getActionText(), primaryCrewMember.getName(),
                        this.gameState.getSpaceShip().getShipName()));
                break;
        }
        repaint();
    }

    /**
     * Notifies the event handler that an action is to occur. The required information is generated and
     * passed to the manager based on the action to be performed.
     *
     * @param action being performed.
     */
    private void performAction(CrewAction action) {

        /*
         To match the ActionHandler requirement. We only have one argument for these actions but
         this allows for future actions to use more parameters.
         */
        Object[] actionArguments;

        // Adds both possible crew. Extra crew member could be null but the handler can deal with an array of 2.
        CrewMember[] actingCrew;
        if (extraCrewMember != null) {
            actingCrew = new CrewMember[]{primaryCrewMember, extraCrewMember};
        } else {
            actingCrew = new CrewMember[]{primaryCrewMember};
        }

        // Get action arguments for actions that require them.
        switch (action) {
            case PILOT:
                // Get the destination planet.
                actionArguments = new Object[]{gameState.planetFromName(
                        (String) additionalInput2ComboBox.getSelectedItem())};
                break;
            case SEARCH:
                // Get the planet that we are currently orbiting.
                actionArguments = new Object[]{gameState.getCurrentPlanet()};
                break;

            case CONSUME:
                // Get the selected item. We need to strip the extra information added when displaying.
                actionArguments = new Object[]{SpaceItem.valueOf(((String) Objects.requireNonNull(
                        additionalInput1ComboBox.getSelectedItem())).replaceFirst("([0-9]+ x )", ""))};
                break;
            default:
                actionArguments = new Object[0];
                break;

        }

        // Notify the event manager of the action
        GameEnvironment.EVENT_MANAGER.notifyObservers(Event.CREW_MEMBER_ACTION, action, actingCrew, actionArguments,
                gameState);
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridBagLayout());
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 20, 20);
        contentPane.add(panel1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        okButton = new JButton();
        okButton.setEnabled(true);
        okButton.setText("OK");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(okButton, gbc);
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(cancelButton, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 0, 10, 0);
        contentPane.add(panel3, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Droid Sans Mono", -1, 36, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("Select Action");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel3.add(label1, gbc);
        crewMemberNameLabel = new JLabel();
        Font crewMemberNameLabelFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, crewMemberNameLabel.getFont());
        if (crewMemberNameLabelFont != null) crewMemberNameLabel.setFont(crewMemberNameLabelFont);
        crewMemberNameLabel.setText("#CrewMember");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel3.add(crewMemberNameLabel, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 7.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel4, gbc);
        actionsComboBox = new JComboBox();
        actionsComboBox.setToolTipText("Select an action to perform.");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        panel4.add(actionsComboBox, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Avaliable Actions:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(label2, gbc);
        additionalInput1ComboBox = new JComboBox();
        additionalInput1ComboBox.setToolTipText("Select an action to perform.");
        additionalInput1ComboBox.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 0, 0);
        panel4.add(additionalInput1ComboBox, gbc);
        additionalInput1Label = new JLabel();
        additionalInput1Label.setText("Additional Crew:");
        additionalInput1Label.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel4.add(additionalInput1Label, gbc);
        additionalInput2ComboBox = new JComboBox();
        additionalInput2ComboBox.setToolTipText("Select an action to perform.");
        additionalInput2ComboBox.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 0, 0);
        panel4.add(additionalInput2ComboBox, gbc);
        additionalInput2Label = new JLabel();
        additionalInput2Label.setText("Select Destination:");
        additionalInput2Label.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel4.add(additionalInput2Label, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 3.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel5, gbc);
        actionTextLabel = new JLabel();
        Font actionTextLabelFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 12, actionTextLabel.getFont());
        if (actionTextLabelFont != null) actionTextLabel.setFont(actionTextLabelFont);
        actionTextLabel.setHorizontalAlignment(0);
        actionTextLabel.setHorizontalTextPosition(0);
        actionTextLabel.setText("#ActionText");
        actionTextLabel.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel5.add(actionTextLabel, gbc);
        final JSeparator separator1 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 20, 10, 20);
        panel5.add(separator1, gbc);
        final JSeparator separator2 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 20, 10, 20);
        contentPane.add(separator2, gbc);
        label2.setLabelFor(actionsComboBox);
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
        return contentPane;
    }

}