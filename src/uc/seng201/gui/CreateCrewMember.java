package uc.seng201.gui;

import uc.seng201.crew.CrewMember;
import uc.seng201.crew.CrewType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

/**
 * Provides the ability to create or alter a crew member.
 */
public class CreateCrewMember extends JDialog {

    /**
     * Root panel.
     */
    private JPanel contentPane;
    /**
     * Allow the user to confirm they wish to create or alter the current
     * crew member.
     */
    private JButton okButton;
    /**
     * Allow the user to exit without creating or altering a crew member.
     */
    private JButton cancelButton;
    /**
     * The name of the crew member.
     */
    private JTextField NameText;
    /**
     * The available crew types that the user can choose from.
     */
    private JComboBox<CrewType> typeComboBox;
    /**
     * The title of the screen.
     */
    private JLabel titleLabel;
    /**
     * The crew member that is represented by the current user selections.
     */
    private CrewMember crewMember;

    /**
     * Allows the user to update a crew member. The crew member
     * is as the default values for the new crew member created.
     *
     * @param crewMember to be altered.
     */
    CreateCrewMember(CrewMember crewMember) {
        this();
        this.crewMember = crewMember;
        NameText.setText(crewMember.getName());
        typeComboBox.setSelectedItem(crewMember.getCrewType());
        titleLabel.setText("Update Crew Member");
    }

    /**
     * Allows the user to create a new crew member.
     */
    CreateCrewMember() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(okButton);
        titleLabel.setText("Add Crew Member");
        typeComboBox.setModel(new DefaultComboBoxModel<>(CrewType.values()));
        typeComboBox.setSelectedIndex(0);

        okButton.addActionListener(e -> onOK());

        cancelButton.addActionListener(e -> onCancel());

        NameText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                if (isValidState()) {
                    okButton.setEnabled(true);
                } else {
                    okButton.setEnabled(false);
                }
            }
        });

        typeComboBox.addActionListener(e -> okButton.setEnabled(isValidState()));

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

    /**
     * Checks if the crew member has a valid name. If the user is updating
     * their crew member then make sure the crew member has actually changed.
     *
     * @return true if the crew member has a valid name (and has changed from previous details)
     */
    private boolean isValidState() {
        if (crewMember != null) {
            if (NameText.getText().equals(crewMember.getName())
                    && Objects.equals(typeComboBox.getSelectedItem(), crewMember.getCrewType())) {
                return false;
            }
        }
        return (!NameText.getText().equals("")
                && NameText.getText().matches("^([a-zA-Z]){3,12}$"));
    }

    /**
     * Handles when the user has created a crew member.
     */
    private void onOK() {
        CrewType crewType = (CrewType) typeComboBox.getSelectedItem();
        if (crewType == null) {
            return;
        }
        this.crewMember = crewType.getInstance(NameText.getText());
        dispose();
    }

    /**
     * Handles when the user wants to exit and not
     * create a new crew member.
     */
    private void onCancel() {
        this.crewMember = null;
        dispose();
    }

    /**
     * Returns the crew member created by the user or null
     * if they have exited.
     *
     * @return new CrewMember
     */
    CrewMember showDialog() {
        setSize(450, 250);
        setVisible(true);
        return crewMember;
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
        contentPane.setMinimumSize(new Dimension(450, 230));
        contentPane.setPreferredSize(new Dimension(450, 230));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        okButton = new JButton();
        okButton.setEnabled(false);
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
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel3, gbc);
        NameText = new JTextField();
        NameText.setToolTipText("Name must only contain letters and be 3-12 characters long.");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 200;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel3.add(NameText, gbc);
        final JLabel label1 = new JLabel();
        label1.setHorizontalAlignment(4);
        label1.setText("Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 5, 5);
        panel3.add(label1, gbc);
        final JLabel label2 = new JLabel();
        label2.setHorizontalAlignment(4);
        label2.setText("Type:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 0, 5);
        panel3.add(label2, gbc);
        typeComboBox = new JComboBox();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 0, 0);
        panel3.add(typeComboBox, gbc);
        final JSeparator separator1 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 10.0;
        gbc.weighty = 2.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 0, 20);
        panel3.add(separator1, gbc);
        final JSeparator separator2 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        gbc.weightx = 10.0;
        gbc.weighty = 2.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 20, 0, 20);
        panel3.add(separator2, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(spacer2, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel4, gbc);
        titleLabel = new JLabel();
        Font titleLabelFont = this.$$$getFont$$$("Droid Sans Mono", -1, 36, titleLabel.getFont());
        if (titleLabelFont != null) titleLabel.setFont(titleLabelFont);
        titleLabel.setHorizontalAlignment(0);
        titleLabel.setPreferredSize(new Dimension(396, 43));
        titleLabel.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel4.add(titleLabel, gbc);
        label1.setLabelFor(NameText);
        label2.setLabelFor(typeComboBox);
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
