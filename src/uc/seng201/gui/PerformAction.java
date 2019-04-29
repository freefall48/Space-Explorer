package uc.seng201.gui;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.Action;
import uc.seng201.events.EventTrigger;
import uc.seng201.events.RandomEvents;
import uc.seng201.helpers.Helpers;
import uc.seng201.targets.Planet;

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
    private JComboBox<String> comboAdditionalCrew;
    private JLabel lblAdditionalCrew;
    private JLabel lblTargetPlanet;
    private JComboBox<Planet> comboPlanets;

    private CrewMember primaryCrewMember;
    private CrewMember additionalCrewMember;

    private DefaultComboBoxModel<String> additionalCrewModal = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<Action> availableActionsModel = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<Planet> targetPlanetsModel = new DefaultComboBoxModel<>();

    PerformAction(CrewMember crewMember) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.primaryCrewMember = crewMember;
        updateModels();
        lblName.setText(crewMember.getName());
        lblActionText.setText(String.format("What will %s do?", primaryCrewMember.getName()));
        comboActions.setModel(availableActionsModel);
        comboPlanets.setModel(targetPlanetsModel);
        comboAdditionalCrew.setModel(additionalCrewModal);
        comboAdditionalCrew.addActionListener(e -> {
            this.additionalCrewMember = SpaceExplorerGui.spaceShip.findCrewMember(comboAdditionalCrew.getItemAt(
                    comboAdditionalCrew.getSelectedIndex()));
            this.onAdditionalCrewSelected();
        });

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());
        comboActions.addActionListener(e -> onActionSelected());

        comboPlanets.addActionListener(e -> {
            buttonOK.setEnabled(true);
            lblActionText.setText(String.format(Action.PILOT.getActionText(), primaryCrewMember.getName(), additionalCrewMember.getName(),
                    SpaceExplorerGui.spaceShip.getShipName(), comboPlanets.getSelectedItem()));
        });

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

        SpaceExplorerGui.spaceShip.getShipCrew().forEach(coActor -> {
            if (!coActor.equals(primaryCrewMember) && coActor.canPerformActions()) {
                additionalCrewModal.addElement(coActor.getName());
            }
        });

        SpaceExplorerGui.planets.forEach(planet -> {
            if (!planet.equals(SpaceExplorerGui.currentPlanet)) {
                targetPlanetsModel.addElement(planet);
            }
        });

        Action[] actionCache = Action.values();
        for (Action action : actionCache) {
            if (action.getCrewRequired() == 1) {
                availableActionsModel.addElement(action);
            } else if (additionalCrewModal.getSize() >= (action.getCrewRequired() - 1)) {
                availableActionsModel.addElement(action);
            }
        }

        this.revalidate();
        this.repaint();

    }

    private void onOK() {
        Action actionToPerform = comboActions.getItemAt(comboActions.getSelectedIndex());
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

    private void onAdditionalCrewSelected() {
        Action actionToPerform = comboActions.getItemAt(comboActions.getSelectedIndex());
        if (actionToPerform == Action.PILOT) {
            lblActionText.setText(lblActionText.getText() + " " + additionalCrewMember.getName() + " takes the co-pilot seat.");
        }
    }

    private void setAdditionalInputVisible(boolean visible) {
        comboAdditionalCrew.setVisible(visible);
        lblAdditionalCrew.setVisible(visible);
        lblTargetPlanet.setVisible(visible);
        comboPlanets.setVisible(visible);
    }

    private void onActionSelected() {
        setAdditionalInputVisible(false);
        buttonOK.setEnabled(false);
        this.additionalCrewMember = null;
        lblActionText.setText("");
        boolean enableOkButton = false;

        Action actionToPerform = comboActions.getItemAt(comboActions.getSelectedIndex());
        if (actionToPerform.getCrewRequired() == 2) {
            if (this.comboAdditionalCrew.getItemCount() != 0) {
                setAdditionalInputVisible(true);
            } else {
                availableActionsModel.removeElement(Action.PILOT);
                this.revalidate();
                this.repaint();
            }
        } else {
            enableOkButton = true;
        }
        setActionText(actionToPerform);
        buttonOK.setEnabled(enableOkButton);
    }

    private void setActionText(Action action) {
        switch (action) {
            case PILOT:
                lblActionText.setText(String.format("%s readies %s for flight!", primaryCrewMember.getName(),
                        SpaceExplorerGui.spaceShip.getShipName()));
                break;
            case SEARCH:
                lblActionText.setText(String.format(Action.SEARCH.getActionText(), primaryCrewMember.getName(),
                        SpaceExplorerGui.currentPlanet, SpaceExplorerGui.currentPlanet.getPartFound()));
                break;
            case SLEEP:
                lblActionText.setText(String.format(Action.SLEEP.getActionText(), primaryCrewMember.getName()));
                break;
            case EAT:
                lblActionText.setText(String.format(Action.EAT.getActionText(), primaryCrewMember.getName(), "Food"));
                break;
        }
    }

    private void performAction(Action action) {
        switch (action) {
            case PILOT:
                SpaceExplorerGui.currentPlanet = (Planet) comboPlanets.getSelectedItem();
                if (Helpers.randomGenerator.nextBoolean()) {
                    RandomEvents event = RandomEvents.values()[Helpers.randomGenerator.nextInt(RandomEvents.values().length)];
                    if (event.getTrigger().equals(EventTrigger.START_DAY)) {
                        JOptionPane.showMessageDialog(SpaceExplorerGui.getControlFrame(),
                                String.format(event.getEventDescription(), SpaceExplorerGui.spaceShip.getShipName(),
                                        primaryCrewMember.getName(), additionalCrewMember.getName()));
                        event.onTrigger(SpaceExplorerGui.spaceShip);
                    }
                }
                break;
            case SEARCH:
                String foundMessage = SpaceExplorerGui.currentPlanet.onSearch(primaryCrewMember, SpaceExplorerGui.spaceShip);
                JOptionPane.showMessageDialog(SpaceExplorerGui.getControlFrame(), foundMessage);
                break;
            case SLEEP:
                primaryCrewMember.alterTiredness(0 - primaryCrewMember.getMaxTiredness());
                break;
            case EAT:
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
        buttonOK.setEnabled(false);
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
        comboAdditionalCrew = new JComboBox();
        comboAdditionalCrew.setToolTipText("Select an action to perform.");
        comboAdditionalCrew.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 0, 0);
        panel4.add(comboAdditionalCrew, gbc);
        lblAdditionalCrew = new JLabel();
        lblAdditionalCrew.setText("Additional Crew:");
        lblAdditionalCrew.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel4.add(lblAdditionalCrew, gbc);
        comboPlanets = new JComboBox();
        comboPlanets.setToolTipText("Select an action to perform.");
        comboPlanets.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 0, 0);
        panel4.add(comboPlanets, gbc);
        lblTargetPlanet = new JLabel();
        lblTargetPlanet.setText("Select Destination:");
        lblTargetPlanet.setVisible(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel4.add(lblTargetPlanet, gbc);
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