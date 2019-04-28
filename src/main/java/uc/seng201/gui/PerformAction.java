package uc.seng201.gui;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.CrewType;
import uc.seng201.crew.actions.Action;

import javax.swing.*;
import java.awt.event.*;

public class PerformAction extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboActions;
    private JLabel lblName;
    private JLabel lblActionText;
    private JComboBox comboAdditionalCrew;
    private JLabel lblAdditionalCrew;

    private CrewMember primaryCrewMember;
    private CrewMember additionalCrewMember;

    public static Action actionSelected;

    PerformAction(CrewMember crewMember) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.primaryCrewMember = crewMember;
        lblName.setText(crewMember.getName());
        lblActionText.setText(String.format("What will %s do?", primaryCrewMember.getName()));
        comboActions.setModel(new DefaultComboBoxModel<>(Action.values()));
        DefaultComboBoxModel<String> additionalCrewModal = new DefaultComboBoxModel<>();
        SpaceExplorerGui.spaceShip.getShipCrew().forEach(additionalCrewMember -> {
            if (!additionalCrewMember.equals(crewMember)) {
                additionalCrewModal.addElement(additionalCrewMember.getName());
            }
        });
        comboAdditionalCrew.setModel(additionalCrewModal);
        comboAdditionalCrew.addActionListener(e -> additionalCrewMember = SpaceExplorerGui.spaceShip.findCrewMember(
                (String) comboAdditionalCrew.getItemAt(comboAdditionalCrew.getSelectedIndex())));

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());
        comboActions.addActionListener(e -> onActionSelected());

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

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        actionSelected = null;
        dispose();
    }

    private void onActionSelected() {
        buttonOK.setEnabled(false);
        lblActionText.setText("");
        Action actionToPerform = (Action) comboActions.getItemAt(comboActions.getSelectedIndex());
        if (actionToPerform.getCrewRequired() == 2 && this.additionalCrewMember == null) {
            lblAdditionalCrew.setVisible(true);
            comboAdditionalCrew.setVisible(true);
            return;
        } else {
            lblAdditionalCrew.setVisible(false);
            comboAdditionalCrew.setVisible(false);
        }
        if (actionToPerform.getCostsActionPoint()) {
            this.primaryCrewMember.performAction();
        }
        buttonOK.setEnabled(true);
        switch (actionToPerform) {
            case PILOT:
                break;
            case SEARCH:
                break;
            case SLEEP:
                lblActionText.setText(String.format(Action.SLEEP.getActionText(), primaryCrewMember.getName()));
                break;
            case EAT:
                break;
        }
    }
}
