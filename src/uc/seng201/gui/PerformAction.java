package uc.seng201.gui;

import uc.seng201.GameState;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.actions.*;
import uc.seng201.items.ItemType;
import uc.seng201.items.SpaceItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Objects;
import java.util.List;

public class PerformAction extends JDialog {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> comboActions;
    private JLabel lblName;
    private JLabel lblActionText;
    private JComboBox<String> comboAdditionalInfo1;
    private JLabel lblAdditionalInfo1;
    private JLabel lblAdditionalInfo2;
    private JComboBox<String> comboAdditionalInfo2;

    private GameState gameState;
    private CrewMember primaryCrewMember;
    private CrewMember extraCrewMember;

    private DefaultComboBoxModel<String> additionalCrewModal = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<String> availableActionsModel = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<String> targetPlanetsModel = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<String> itemModel = new DefaultComboBoxModel<>();

    PerformAction(GameState gameState, CrewMember crewMember) {
        this.gameState = gameState;
        this.primaryCrewMember = crewMember;

        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(buttonOK);

        lblName.setText(crewMember.getName());
        comboActions.setModel(availableActionsModel);
        CrewAction defaultAction = CrewAction.SEARCH;
        availableActionsModel.setSelectedItem(defaultAction);
        actionDialog(defaultAction);
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        comboActions.addActionListener(e -> onActionSelected());
        comboAdditionalInfo1.addActionListener(e -> onAdditionalInput());
        comboAdditionalInfo2.addActionListener(e -> onAdditionalInput());
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

        updateModels();
        generateAvailableActions();
        comboActions.setSelectedItem(CrewAction.SEARCH.toString());
    }

    private void onAdditionalInput() {
        CrewAction action = CrewAction.valueOf(comboActions.getItemAt(comboActions.getSelectedIndex()));
        actionDialog(action);
    }

    private void updateModels() {
        additionalCrewModal.removeAllElements();
        availableActionsModel.removeAllElements();
        targetPlanetsModel.removeAllElements();

        this.gameState.getSpaceShip().getShipCrew().forEach(additionalCrewMember -> {
            if (!additionalCrewMember.equals(primaryCrewMember) && additionalCrewMember.canPerformActions()) {
                additionalCrewModal.addElement(additionalCrewMember.toString());
            }
        });

        this.gameState.getPlanets().forEach(planet -> {
            if (!planet.equals(this.gameState.getCurrentPlanet())) {
                targetPlanetsModel.addElement(planet.getPlanetName());
            }
        });
        this.revalidate();
        this.repaint();

    }

    private void generateAvailableActions() {
        CrewAction[] actionCache = CrewAction.values();
        if (primaryCrewMember.getTiredness() == primaryCrewMember.getMaxTiredness()) {
            availableActionsModel.addElement(CrewAction.SLEEP.toString());
            return;
        }

        for (CrewAction action : actionCache) {
            if (action.getCrewRequired() == 1) {
                availableActionsModel.addElement(action.toString());
            } else if (additionalCrewModal.getSize() >= (action.getCrewRequired() - 1)) {
                availableActionsModel.addElement(action.toString());
            }
        }

        boolean foodPresent = false;
        boolean medicalPresent = false;
        for (Map.Entry<SpaceItem, Integer> entry : gameState.getSpaceShip().getShipItems().entrySet()) {
            if (entry.getKey().getItemType().equals(ItemType.FOOD)) {
                foodPresent = true;
            } else if (entry.getKey().getItemType().equals(ItemType.MEDICAL)) {
                medicalPresent = true;
            }
            if (foodPresent && medicalPresent) {
                break;
            }
        }
        if (!foodPresent) {
            availableActionsModel.removeElement(CrewAction.EAT.toString());
        }
        if (!medicalPresent) {
            availableActionsModel.removeElement(CrewAction.MEDICAL.toString());
        }
    }

    private void onOK() {
        CrewAction actionToPerform = CrewAction.valueOf(comboActions.getItemAt(comboActions.getSelectedIndex()));
        if (actionToPerform.getCrewRequired() == 2) {
            String[] crewMemberDetails = additionalCrewModal.getSelectedItem().toString().split(" - ");
            this.extraCrewMember = this.gameState.getSpaceShip().crewMemberFromNameAndType(
                    crewMemberDetails[0], crewMemberDetails[1]);
        }
        if (actionToPerform.getCostsActionPoint()) {
            this.primaryCrewMember.performAction();
            if (extraCrewMember != null) {
                extraCrewMember.performAction();
            }
        }
        performAction(actionToPerform);
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void hideAdditionalInput() {
        comboAdditionalInfo1.setVisible(false);
        lblAdditionalInfo1.setVisible(false);
        lblAdditionalInfo2.setVisible(false);
        comboAdditionalInfo2.setVisible(false);
    }

    private void setAdditionalInputVisible(int additionalInputs) {
        switch (additionalInputs) {
            case 2:
                comboAdditionalInfo2.setVisible(true);
                lblAdditionalInfo2.setVisible(true);
            case 1:
                comboAdditionalInfo1.setVisible(true);
                lblAdditionalInfo1.setVisible(true);
                break;
        }
    }

    private void onActionSelected() {
        hideAdditionalInput();
        CrewAction actionToPerform = CrewAction.valueOf(comboActions.getItemAt(comboActions.getSelectedIndex()));
        switch (actionToPerform) {
            case PILOT:
                lblAdditionalInfo1.setText("Co-pilot:");
                comboAdditionalInfo1.setModel(additionalCrewModal);
                comboAdditionalInfo1.setSelectedIndex(0);
                lblAdditionalInfo2.setText("Destination Planet:");
                comboAdditionalInfo2.setModel(targetPlanetsModel);
                comboAdditionalInfo2.setSelectedIndex(0);
                setAdditionalInputVisible(2);
                break;
            case EAT:
                setItemsModel(ItemType.FOOD);
                lblAdditionalInfo1.setText("Snack on:");
                comboAdditionalInfo1.setModel(itemModel);
                comboAdditionalInfo1.setSelectedIndex(0);
                setAdditionalInputVisible(1);
                break;
            case MEDICAL:
                setItemsModel(ItemType.MEDICAL);
                lblAdditionalInfo1.setText("Apply:");
                comboAdditionalInfo1.setModel(itemModel);
                comboAdditionalInfo1.setSelectedIndex(0);
                setAdditionalInputVisible(1);
                break;
            default:
                break;
        }
        actionDialog(actionToPerform);
    }

    private void setItemsModel(ItemType itemType) {
        itemModel.removeAllElements();
        gameState.getSpaceShip().getShipItems().forEach((item, qty) -> {
            if (item.getItemType().equals(itemType)) {
                itemModel.addElement(String.format("%d x %s", qty, item.toString()));
            }
        });
    }

    private void actionDialog(CrewAction action) {
        switch (action) {
            case PILOT:
                if (comboAdditionalInfo2.getSelectedItem() != null) {
                    lblActionText.setText(String.format(CrewAction.PILOT.getActionText(), primaryCrewMember.getName(),
                        comboAdditionalInfo1.getSelectedItem(), this.gameState.getSpaceShip().getShipName(),
                        comboAdditionalInfo2.getSelectedItem()).replaceFirst(" - [a-zA-Z]*", ""));
                }

                break;

            case SEARCH:
                lblActionText.setText(String.format(CrewAction.SEARCH.getActionText(), primaryCrewMember.getName(),
                        this.gameState.getCurrentPlanet(), this.gameState.getCurrentPlanet().description()));
                break;

            case SLEEP:
                lblActionText.setText(String.format(CrewAction.SLEEP.getActionText(), primaryCrewMember.getName()));
                break;

            case EAT:
                lblActionText.setText(String.format(CrewAction.EAT.getActionText(), primaryCrewMember.getName(),
                        Objects.requireNonNull(comboAdditionalInfo1.getSelectedItem()).toString().replaceFirst(
                                "([0-9]+ x )", "")));
                break;

            case MEDICAL:
                lblActionText.setText(String.format(CrewAction.MEDICAL.getActionText(), primaryCrewMember.getName(),
                        Objects.requireNonNull(comboAdditionalInfo1.getSelectedItem()).toString().replaceFirst(
                                "([0-9]+ x )", "")));
                break;

            case REPAIR:
                lblActionText.setText(String.format(CrewAction.REPAIR.getActionText(), primaryCrewMember.getName(),
                        this.gameState.getSpaceShip().getShipName()));
                break;
        }
        repaint();
    }

    private void performAction(CrewAction action) {
        String message = null;
        switch (action) {
            case PILOT:
                message = new ActionPilot().perform(gameState, new Object[]{gameState.planetFromName(
                        (String) comboAdditionalInfo2.getSelectedItem())}, primaryCrewMember, extraCrewMember);
                break;
            case SEARCH:
                message = new ActionSearch().perform(gameState, new Object[]{gameState.getCurrentPlanet()},
                        primaryCrewMember);
                break;
            case SLEEP:
                message = new ActionSleep().perform(gameState, null, primaryCrewMember);
                break;
            case EAT:
            case MEDICAL:
                SpaceItem item = SpaceItem.valueOf(Objects.requireNonNull(
                        comboAdditionalInfo1.getSelectedItem()).toString().replaceFirst("([0-9]+ x )", ""));
                message = new ActionConsumeItem().perform(gameState, new Object[]{item}, primaryCrewMember);
                break;
            case REPAIR:
                message = new ActionRepair().perform(gameState, null, primaryCrewMember);
                break;
        }
        if (message != null) {
            JOptionPane.showMessageDialog(this, message);
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
        buttonOK = new JButton();
        buttonOK.setEnabled(true);
        buttonOK.setText("OK");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonOK, gbc);
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonCancel, gbc);
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
        lblName = new JLabel();
        Font lblNameFont = this.$$$getFont$$$("Droid Sans Mono", -1, 16, lblName.getFont());
        if (lblNameFont != null) lblName.setFont(lblNameFont);
        lblName.setText("#CrewMember");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel3.add(lblName, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 7.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel4, gbc);
        comboActions = new JComboBox();
        comboActions.setToolTipText("Select an action to perform.");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        panel4.add(comboActions, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Avaliable Actions:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel4.add(label2, gbc);
        comboAdditionalInfo1 = new JComboBox();
        comboAdditionalInfo1.setToolTipText("Select an action to perform.");
        comboAdditionalInfo1.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 0, 0);
        panel4.add(comboAdditionalInfo1, gbc);
        lblAdditionalInfo1 = new JLabel();
        lblAdditionalInfo1.setText("Additional Crew:");
        lblAdditionalInfo1.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel4.add(lblAdditionalInfo1, gbc);
        comboAdditionalInfo2 = new JComboBox();
        comboAdditionalInfo2.setToolTipText("Select an action to perform.");
        comboAdditionalInfo2.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 0, 0);
        panel4.add(comboAdditionalInfo2, gbc);
        lblAdditionalInfo2 = new JLabel();
        lblAdditionalInfo2.setText("Select Destination:");
        lblAdditionalInfo2.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel4.add(lblAdditionalInfo2, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 3.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel5, gbc);
        lblActionText = new JLabel();
        Font lblActionTextFont = this.$$$getFont$$$("Droid Sans Mono", Font.ITALIC, 12, lblActionText.getFont());
        if (lblActionTextFont != null) lblActionText.setFont(lblActionTextFont);
        lblActionText.setHorizontalAlignment(0);
        lblActionText.setHorizontalTextPosition(0);
        lblActionText.setText("#ActionText");
        lblActionText.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel5.add(lblActionText, gbc);
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
        label2.setLabelFor(comboActions);
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