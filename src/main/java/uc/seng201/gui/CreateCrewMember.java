package uc.seng201.gui;

import uc.seng201.crew.*;

import javax.swing.*;
import java.awt.event.*;

public class CreateCrewMember extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtName;
    private JComboBox<CrewType> comboType;

    CreateCrewMember() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        comboType.setModel(new DefaultComboBoxModel<>(CrewType.values()));

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        txtName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!txtName.getText().equals("") && txtName.getText().matches("^([a-zA-Z0-9_]+( [a-zA-Z0-9_]+)*){1,12}$")) {
                    buttonOK.setEnabled(true);
                } else {
                    buttonOK.setEnabled(false);
                }
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        CrewType crewType = (CrewType) comboType.getSelectedItem();
        if(crewType == null) {
            return;
        }
        switch (crewType) {
            case HUMAN:
                AdventureCreator.listCrewModal.addElement(new Human(txtName.getText()));
                break;
            case CRYSTAL:
                AdventureCreator.listCrewModal.addElement(new Crystal(txtName.getText()));
                break;
            case ENGI:
                AdventureCreator.listCrewModal.addElement(new Engi((txtName.getText())));
                break;
            case SLUG:
                AdventureCreator.listCrewModal.addElement(new Slug(txtName.getText()));
                break;
            case LANIUS:
                AdventureCreator.listCrewModal.addElement(new Lanius(txtName.getText()));
                break;
            case ZOLTAN:
                AdventureCreator.listCrewModal.addElement(new Zoltan(txtName.getText()));
                break;
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
