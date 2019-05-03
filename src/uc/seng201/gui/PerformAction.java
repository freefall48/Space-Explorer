package uc.seng201.gui;

import uc.seng201.SpaceExplorer;
import uc.seng201.crew.CrewMember;
import uc.seng201.crew.Action;
import uc.seng201.events.EventTrigger;
import uc.seng201.events.IRandomEvent;
import uc.seng201.events.RandomEvent;
import uc.seng201.helpers.Helpers;
import uc.seng201.items.ItemType;
import uc.seng201.items.Items;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PerformAction extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Action> comboActions;
    private JLabel lblName;
    private JLabel lblActionText;
    private JComboBox<String> comboAdditionalInfo1;
    private JLabel lblAdditionalInfo1;
    private JLabel lblAdditionalInfo2;
    private JComboBox<String> comboAdditionalInfo2;

    private CrewMember primaryCrewMember;
    private CrewMember additionalCrewMember;

    private DefaultComboBoxModel<String> additionalCrewModal = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<Action> availableActionsModel = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<String> targetPlanetsModel = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<String> itemModel = new DefaultComboBoxModel<>();

    PerformAction(CrewMember crewMember) {
        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        getRootPane().setDefaultButton(buttonOK);
        this.primaryCrewMember = crewMember;
        updateModels();
        lblName.setText(crewMember.getName());

        comboActions.setModel(availableActionsModel);

        Action defaultAction = Action.SEARCH;
        availableActionsModel.setSelectedItem(defaultAction);
        setActionDialog(defaultAction);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        comboActions.addActionListener(e -> onActionSelected());
        comboAdditionalInfo1.addActionListener(e -> setActionDialog((Action) comboActions.getSelectedItem()));
        comboAdditionalInfo2.addActionListener(e -> setActionDialog((Action) comboActions.getSelectedItem()));

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
    }

    private void updateModels() {
        additionalCrewModal.removeAllElements();
        availableActionsModel.removeAllElements();
        targetPlanetsModel.removeAllElements();

        SpaceExplorer.spaceShip.getShipCrew().forEach(coActor -> {
            if (!coActor.equals(primaryCrewMember) && coActor.canPerformActions()) {
                additionalCrewModal.addElement(coActor.getName());
            }
        });

        SpaceExplorer.planets.forEach(planet -> {
            if (!planet.equals(SpaceExplorer.currentPlanet)) {
                targetPlanetsModel.addElement(planet.getPlanetName());
            }
        });

        generateAvailableActions();
        this.revalidate();
        this.repaint();

    }

    private void generateAvailableActions() {
        Action[] actionCache = Action.values();
        for (Action action : actionCache) {
            if (action.getCrewRequired() == 1) {
                availableActionsModel.addElement(action);
            } else if (additionalCrewModal.getSize() >= (action.getCrewRequired() - 1)) {
                availableActionsModel.addElement(action);
            }
        }
        boolean foodPresent = false;
        boolean medicalPresent = false;
        for (Items item : SpaceExplorer.spaceShip.getShipItems()) {
            if (item.getItemType().equals(ItemType.FOOD)) {
                foodPresent = true;
            } else if (item.getItemType().equals(ItemType.MEDICAL)) {
                medicalPresent = true;
            }
        }
        if (!foodPresent) {
            availableActionsModel.removeElement(Action.EAT);
        }
        if (!medicalPresent) {
            availableActionsModel.removeElement(Action.MEDICAL);
        }
    }

    private void onOK() {
        Action actionToPerform = comboActions.getItemAt(comboActions.getSelectedIndex());
        if (actionToPerform.getCrewRequired() == 2) {
            this.additionalCrewMember = SpaceExplorer.spaceShip.findCrewMember(
                    (String) additionalCrewModal.getSelectedItem());
        }
        if (actionToPerform.getCostsActionPoint()) {
            this.primaryCrewMember.performAction();
            if (additionalCrewMember != null) {
                additionalCrewMember.performAction();
            }
        }
        performAction(actionToPerform);
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void setAdditionalInputVisible(boolean visible) {
        comboAdditionalInfo1.setVisible(visible);
        lblAdditionalInfo1.setVisible(visible);
        lblAdditionalInfo2.setVisible(visible);
        comboAdditionalInfo2.setVisible(visible);
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
        setAdditionalInputVisible(false);

        Action actionToPerform = comboActions.getItemAt(comboActions.getSelectedIndex());
        setActionDialog(actionToPerform);
    }

    private void setItemsModel(ItemType itemType) {
        itemModel.removeAllElements();
        for (Items item : SpaceExplorer.spaceShip.getShipItems()) {
            if (item.getItemType().equals(itemType) && !itemModelContains(item)) {
                itemModel.addElement(item.toString());
            }
        }
    }

    //TODO: Move this logic to the spaceship
    private boolean itemModelContains(Items item) {
        for (int i = 0; i < itemModel.getSize(); i++) {
            if (itemModel.getElementAt(i).equals(item.toString())) {
                return true;
            }
        }
        return false;
    }

    private void setActionDialog(Action action) {
        buttonOK.setEnabled(true);
        switch (action) {
            case PILOT:
                // TODO: need to fix action text not updating for change in other combos
                lblAdditionalInfo1.setText("Co-pilot:");
                comboAdditionalInfo1.setModel(additionalCrewModal);
                comboAdditionalInfo1.setSelectedIndex(0);
                lblAdditionalInfo2.setText("Destination Planet:");
                comboAdditionalInfo2.setModel(targetPlanetsModel);
                comboAdditionalInfo2.setSelectedIndex(0);
                setAdditionalInputVisible(2);
                lblActionText.setText(String.format(Action.PILOT.getActionText(), primaryCrewMember.getName(),
                        comboAdditionalInfo1.getSelectedItem(), SpaceExplorer.spaceShip.getShipName(),
                        comboAdditionalInfo2.getSelectedItem()));
                break;
            case SEARCH:
                lblActionText.setText(String.format(Action.SEARCH.getActionText(), primaryCrewMember.getName(),
                        SpaceExplorer.currentPlanet, SpaceExplorer.currentPlanet.description()));
                break;
            case SLEEP:
                lblActionText.setText(String.format(Action.SLEEP.getActionText(), primaryCrewMember.getName()));
                break;
            case EAT:
                setItemsModel(ItemType.FOOD);
                lblAdditionalInfo1.setText("Snack on:");
                comboAdditionalInfo1.setModel(itemModel);
                comboAdditionalInfo1.setSelectedIndex(0);
                setAdditionalInputVisible(1);
                lblActionText.setText(String.format(Action.EAT.getActionText(), primaryCrewMember.getName(),
                        comboAdditionalInfo1.getSelectedItem()));
                break;
            case MEDICAL:
                setItemsModel(ItemType.MEDICAL);
                lblAdditionalInfo1.setText("Apply:");
                comboAdditionalInfo1.setModel(itemModel);
                comboAdditionalInfo1.setSelectedIndex(0);
                setAdditionalInputVisible(1);
                lblActionText.setText(String.format(Action.MEDICAL.getActionText(), primaryCrewMember.getName(),
                        comboAdditionalInfo1.getSelectedItem()));
                break;
            case REPAIR:
                lblActionText.setText(String.format(Action.REPAIR.getActionText(), primaryCrewMember.getName(),
                        SpaceExplorer.spaceShip.getShipName()));
                break;
        }
        repaint();
        revalidate();
    }

    private void performAction(Action action) {
        switch (action) {
            case PILOT:
                SpaceExplorer.currentPlanet = SpaceExplorer.getPlanetFromName((String) comboAdditionalInfo2.getSelectedItem());
                if (Helpers.randomGenerator.nextBoolean()) {
                    RandomEvent event = IRandomEvent.eventToTrigger(EventTrigger.TRAVEL);
                    event.getInstance().onTrigger(SpaceExplorer.spaceShip);
                    SpaceExplorer.popup(event.getEventDescription());
                }
                break;
            case SEARCH:
                String foundMessage = SpaceExplorer.currentPlanet.onSearch(primaryCrewMember, SpaceExplorer.spaceShip);
                JOptionPane.showMessageDialog(SpaceExplorer.getControlFrame(), foundMessage);
                break;
            case SLEEP:
                primaryCrewMember.alterTiredness(0 - primaryCrewMember.getMaxTiredness());
                break;
            case EAT:
            case MEDICAL:
                Items item = Items.valueOf((String) comboAdditionalInfo1.getSelectedItem());
                SpaceExplorer.spaceShip.remove(item);
                item.onConsume(primaryCrewMember);
                break;
            case REPAIR:
                SpaceExplorer.spaceShip.alterShield(primaryCrewMember.getRepairAmount());
                break;
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
        gbc.gridy = 3;
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
        gbc.gridy = 1;
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
        gbc.gridy = 2;
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