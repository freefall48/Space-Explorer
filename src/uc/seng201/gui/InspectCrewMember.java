package uc.seng201.gui;

import uc.seng201.crew.CrewMember;

import javax.swing.*;
import java.awt.*;

public class InspectCrewMember extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel lblType;
    private JLabel lblActions;
    private JLabel lblHealth;
    private JLabel lblHealthRegen;
    private JLabel lblFood;
    private JLabel lblFoodDecay;
    private JLabel lblTiredness;
    private JLabel lblTirednessRate;
    private JList<String> listModifications;
    private JLabel lblName;
    private JLabel lblRepair;

    private CrewMember crewMember;

    public InspectCrewMember(CrewMember crewMember) {
        this.crewMember = crewMember;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        displayInformation();
    }

    private void displayInformation() {
        lblName.setText(crewMember.getName());

        lblType.setText(crewMember.getCrewType().toString());
        lblActions.setText(String.valueOf(crewMember.getActionsLeftToday()));

        lblHealth.setText(String.format("%d/%d", crewMember.getHealth(), crewMember.getMaxHealth()));
        lblHealthRegen.setText(String.format("%d per day", crewMember.getCurrentHealthRegen()));

        lblFood.setText(String.format("%d/%d", crewMember.getFoodLevel(), crewMember.getMaxFoodLevel()));
        lblFoodDecay.setText(String.format("%d per day", crewMember.getFoodDecayRate()));

        lblTiredness.setText(String.format("%d/%d", crewMember.getTiredness(), crewMember.getMaxTiredness()));
        lblTirednessRate.setText(String.format("%d per day", crewMember.getTirednessRate()));

        lblRepair.setText(String.format("%d per repair", crewMember.getRepairAmount()));

        DefaultListModel<String> listModificationModel = new DefaultListModel<>();
        crewMember.getModifications().forEach(modification -> listModificationModel.addElement(String.format("%s - %s",
                modification.toString(), modification.getDescription())));
        listModifications.setModel(listModificationModel);
    }

    private void onOK() {
        // add your code here
        dispose();
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
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 10, 20);
        contentPane.add(panel1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        buttonOK = new JButton();
        buttonOK.setText("Done");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(buttonOK, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(panel3, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 0, 20);
        panel3.add(panel4, gbc);
        lblName = new JLabel();
        Font lblNameFont = this.$$$getFont$$$(null, -1, 36, lblName.getFont());
        if (lblNameFont != null) lblName.setFont(lblNameFont);
        lblName.setText("##############");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 5.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 0);
        panel4.add(lblName, gbc);
        final JSeparator separator1 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel4.add(separator1, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, 36, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Crew:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 5.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel4.add(label1, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 5.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 0, 20);
        panel3.add(panel5, gbc);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 2.0;
        gbc.weighty = 10.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel5.add(panel6, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, -1, 16, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Health:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 2, 5);
        panel6.add(label2, gbc);
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, -1, 16, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Food:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 2, 5);
        panel6.add(label3, gbc);
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, -1, 16, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Tiredness:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 2, 5);
        panel6.add(label4, gbc);
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, -1, 16, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Actions:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 2, 5);
        panel6.add(label5, gbc);
        final JLabel label6 = new JLabel();
        Font label6Font = this.$$$getFont$$$(null, -1, 16, label6.getFont());
        if (label6Font != null) label6.setFont(label6Font);
        label6.setText("Health Regen:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 2, 5);
        panel6.add(label6, gbc);
        final JLabel label7 = new JLabel();
        Font label7Font = this.$$$getFont$$$(null, -1, 16, label7.getFont());
        if (label7Font != null) label7.setFont(label7Font);
        label7.setText("Food Decay:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 2, 5);
        panel6.add(label7, gbc);
        final JLabel label8 = new JLabel();
        Font label8Font = this.$$$getFont$$$(null, -1, 16, label8.getFont());
        if (label8Font != null) label8.setFont(label8Font);
        label8.setText("Tiredness Rate:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 2, 5);
        panel6.add(label8, gbc);
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$(null, -1, 16, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setText("Type:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 2, 5);
        panel6.add(label9, gbc);
        lblHealth = new JLabel();
        Font lblHealthFont = this.$$$getFont$$$(null, -1, 16, lblHealth.getFont());
        if (lblHealthFont != null) lblHealth.setFont(lblHealthFont);
        lblHealth.setText("Health");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 2, 0);
        panel6.add(lblHealth, gbc);
        lblFood = new JLabel();
        Font lblFoodFont = this.$$$getFont$$$(null, -1, 16, lblFood.getFont());
        if (lblFoodFont != null) lblFood.setFont(lblFoodFont);
        lblFood.setText("Food");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 2, 0);
        panel6.add(lblFood, gbc);
        lblTiredness = new JLabel();
        Font lblTirednessFont = this.$$$getFont$$$(null, -1, 16, lblTiredness.getFont());
        if (lblTirednessFont != null) lblTiredness.setFont(lblTirednessFont);
        lblTiredness.setText("Tiredness");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 2, 0);
        panel6.add(lblTiredness, gbc);
        lblActions = new JLabel();
        Font lblActionsFont = this.$$$getFont$$$(null, -1, 16, lblActions.getFont());
        if (lblActionsFont != null) lblActions.setFont(lblActionsFont);
        lblActions.setText("Actions");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 2, 0);
        panel6.add(lblActions, gbc);
        lblHealthRegen = new JLabel();
        Font lblHealthRegenFont = this.$$$getFont$$$(null, -1, 16, lblHealthRegen.getFont());
        if (lblHealthRegenFont != null) lblHealthRegen.setFont(lblHealthRegenFont);
        lblHealthRegen.setText("Health Regen");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 2, 0);
        panel6.add(lblHealthRegen, gbc);
        lblFoodDecay = new JLabel();
        Font lblFoodDecayFont = this.$$$getFont$$$(null, -1, 16, lblFoodDecay.getFont());
        if (lblFoodDecayFont != null) lblFoodDecay.setFont(lblFoodDecayFont);
        lblFoodDecay.setText("Food Decay");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 2, 0);
        panel6.add(lblFoodDecay, gbc);
        lblTirednessRate = new JLabel();
        Font lblTirednessRateFont = this.$$$getFont$$$(null, -1, 16, lblTirednessRate.getFont());
        if (lblTirednessRateFont != null) lblTirednessRate.setFont(lblTirednessRateFont);
        lblTirednessRate.setText("Tiredness Rate");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 2, 0);
        panel6.add(lblTirednessRate, gbc);
        lblType = new JLabel();
        Font lblTypeFont = this.$$$getFont$$$(null, -1, 16, lblType.getFont());
        if (lblTypeFont != null) lblType.setFont(lblTypeFont);
        lblType.setText("Type");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 2, 0);
        panel6.add(lblType, gbc);
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, -1, 16, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("Repair Ability:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 2, 5);
        panel6.add(label10, gbc);
        lblRepair = new JLabel();
        Font lblRepairFont = this.$$$getFont$$$(null, -1, 16, lblRepair.getFont());
        if (lblRepairFont != null) lblRepair.setFont(lblRepairFont);
        lblRepair.setText("Repair");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 2, 0);
        panel6.add(lblRepair, gbc);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add(panel7, gbc);
        final JLabel label11 = new JLabel();
        Font label11Font = this.$$$getFont$$$(null, -1, 16, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setText("Active Modifications:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel7.add(label11, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel7.add(scrollPane1, gbc);
        listModifications = new JList();
        listModifications.setFixedCellHeight(30);
        Font listModificationsFont = this.$$$getFont$$$(null, -1, 14, listModifications.getFont());
        if (listModificationsFont != null) listModifications.setFont(listModificationsFont);
        listModifications.setSelectionMode(0);
        scrollPane1.setViewportView(listModifications);
        final JSeparator separator2 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel5.add(separator2, gbc);
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
