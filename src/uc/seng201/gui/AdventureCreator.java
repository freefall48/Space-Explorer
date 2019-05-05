package uc.seng201.gui;

import uc.seng201.GameState;
import uc.seng201.SpaceExplorer;
import uc.seng201.SpaceShip;
import uc.seng201.crew.CrewMember;
import uc.seng201.destinations.traders.SpaceTraders;
import uc.seng201.helpers.Helpers;
import uc.seng201.destinations.Planet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

class AdventureCreator extends ScreenComponent {
    private JTextField textShipName;
    private JSlider sliderDuration;
    private JPanel panelCreator;
    private JButton btnContinue;
    private JButton btnAddCrewMember;
    private JList<CrewMember> listCrew;
    private JCheckBox checkboxCustomShipFile;
    private JButton btnBack;
    private JButton btnUpdateCrewMember;
    private JButton btnRemoveCrewMember;

    private SpaceExplorer spaceExplorer;
    private DefaultListModel<CrewMember> listCrewModal = new DefaultListModel<>();

    AdventureCreator(SpaceExplorer spaceExplorer) {
        this.spaceExplorer = spaceExplorer;

        textShipName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                validateState();
            }
        });

        listCrew.setModel(listCrewModal);

        listCrew.addListSelectionListener(e -> {
            btnRemoveCrewMember.setEnabled(true);
            btnUpdateCrewMember.setEnabled(true);
        });

        btnBack.addActionListener(e -> this.spaceExplorer.changeScreen(Screen.MAIN_MENU));
        btnAddCrewMember.addActionListener(e -> onAddCrewMember());
        btnContinue.addActionListener(e -> onContinue());
        btnUpdateCrewMember.addActionListener(e -> onUpdateCrewMember());
        btnRemoveCrewMember.addActionListener(e -> onRemoveCrewMember());
        checkboxCustomShipFile.addActionListener(e -> onAddCustomShipFile());
    }

    @Override
    public JComponent getRootComponent() {
        return panelCreator;
    }

    private void onContinue() {
        int gameDuration = sliderDuration.getValue();
        SpaceShip spaceShip = new SpaceShip(textShipName.getText(), Helpers.calcPartsToFind(gameDuration));
        spaceShip.add(listCrewModal.toArray());

        List<Planet> planets = Helpers.generatePlanets(gameDuration);

        this.spaceExplorer.setGameState(new GameState(spaceShip, gameDuration, planets));
        this.spaceExplorer.getGameState().setTraders(new SpaceTraders());
        this.spaceExplorer.getGameState().getTrader().generateAvailableItemsToday(false);
        this.spaceExplorer.changeScreen(Screen.MAIN_SCREEN);
    }

    private void onAddCrewMember() {
        CreateCrewMember createDialog = new CreateCrewMember();
        createDialog.setLocationRelativeTo(this);
        this.listCrewModal.addElement(createDialog.showDialog());
        resetCrewButtons();
        validateState();

    }

    private void onRemoveCrewMember() {
        this.listCrewModal.removeElementAt(listCrew.getSelectedIndex());
        resetCrewButtons();
        validateState();
    }

    private void onUpdateCrewMember() {
        CrewMember currentCrewMember = listCrewModal.get(listCrew.getSelectedIndex());
        CrewMember newCrewMember = new CreateCrewMember(currentCrewMember).showDialog();
        if (newCrewMember != null) {
            listCrewModal.removeElement(currentCrewMember);
            listCrewModal.addElement(newCrewMember);
            resetCrewButtons();
            validateState();
        }

    }

    private void resetCrewButtons() {
        this.btnUpdateCrewMember.setEnabled(false);
        this.btnRemoveCrewMember.setEnabled(false);
        revalidate();
        repaint();
    }

    private void validateState() {
        btnContinue.setEnabled(this.listCrewModal.size() >= 2 && this.listCrewModal.size() <= 4 && !textShipName.getText().equals("")
                && textShipName.getText().matches("^([a-zA-Z0-9_]( [a-zA-Z0-9_]+)*){3,24}$"));
    }

    private void onAddCustomShipFile() {

//        if (checkboxCustomShipFile.isSelected()) {
//            FileDialog fd = new FileDialog(SpaceExplorer.getRootFrame(), "Choose a file", FileDialog.LOAD);
//            fd.setFile("*.png");
//            fd.setMultipleMode(false);
//            fd.setVisible(true);
//            if (fd.getFile() != null) {
//                try {
//                    SpaceExplorer.shipImage = ImageIO.read(fd.getFiles()[0]);
//                    SpaceExplorer.shipImageLocation = fd.getDirectory() + fd.getFile();
//                } catch (IOException error) {
//                    JOptionPane.showMessageDialog(SpaceExplorer.getControlFrame(),
//                            "Failed to load the selected image!", "Error", JOptionPane.ERROR_MESSAGE);
//                    checkboxCustomShipFile.setSelected(false);
//                }
//            } else {
//                checkboxCustomShipFile.setSelected(false);
//            }
//            fd.dispose();
//        }
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
        panelCreator = new JPanel();
        panelCreator.setLayout(new GridBagLayout());
        panelCreator.setPreferredSize(new Dimension(650, 500));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        panel1.setMinimumSize(new Dimension(-1, -1));
        panel1.setPreferredSize(new Dimension(-1, -1));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 4.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelCreator.add(panel1, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Droid Sans Mono", -1, 36, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Create New Adventure");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel1.add(label1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        panel2.setAutoscrolls(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 7.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 0, 20);
        panelCreator.add(panel2, gbc);
        final JSeparator separator1 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel2.add(separator1, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Droid Sans Mono", -1, 20, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Current Crew");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel2.add(label2, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(30);
        scrollPane1.setPreferredSize(new Dimension(0, 150));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 10.0;
        gbc.weighty = 7.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(scrollPane1, gbc);
        listCrew = new JList();
        listCrew.setAutoscrolls(true);
        listCrew.setFixedCellHeight(-1);
        listCrew.setFixedCellWidth(-1);
        Font listCrewFont = this.$$$getFont$$$("Droid Sans Mono", -1, 12, listCrew.getFont());
        if (listCrewFont != null) listCrew.setFont(listCrewFont);
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        listCrew.setModel(defaultListModel1);
        listCrew.setSelectionMode(2);
        listCrew.setVisibleRowCount(4);
        scrollPane1.setViewportView(listCrew);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel3, gbc);
        btnRemoveCrewMember = new JButton();
        btnRemoveCrewMember.setEnabled(false);
        btnRemoveCrewMember.setText("Remove");
        btnRemoveCrewMember.setToolTipText("Add a crew member to our force");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 10, 2, 0);
        panel3.add(btnRemoveCrewMember, gbc);
        btnUpdateCrewMember = new JButton();
        btnUpdateCrewMember.setEnabled(false);
        btnUpdateCrewMember.setText("Update");
        btnUpdateCrewMember.setToolTipText("Add a crew member to our force");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 10, 2, 0);
        panel3.add(btnUpdateCrewMember, gbc);
        btnAddCrewMember = new JButton();
        btnAddCrewMember.setEnabled(true);
        btnAddCrewMember.setText("Add");
        btnAddCrewMember.setToolTipText("Add a crew member to our force");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 10, 2, 0);
        panel3.add(btnAddCrewMember, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel3.add(spacer1, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel2.add(panel4, gbc);
        textShipName = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 100;
        panel4.add(textShipName, gbc);
        sliderDuration = new JSlider();
        sliderDuration.setMajorTickSpacing(1);
        sliderDuration.setMaximum(10);
        sliderDuration.setMinimum(3);
        sliderDuration.setPaintLabels(true);
        sliderDuration.setPaintTicks(false);
        sliderDuration.setSnapToTicks(true);
        sliderDuration.setValue(5);
        sliderDuration.setValueIsAdjusting(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.ipadx = 150;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel4.add(sliderDuration, gbc);
        final JLabel label3 = new JLabel();
        label3.setText("Ship Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel4.add(label3, gbc);
        final JLabel label4 = new JLabel();
        label4.setText("Duration:");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 15, 0, 5);
        panel4.add(label4, gbc);
        checkboxCustomShipFile = new JCheckBox();
        checkboxCustomShipFile.setText("Custom Ship");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 15, 0, 0);
        panel4.add(checkboxCustomShipFile, gbc);
        final JSeparator separator2 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel4.add(separator2, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 20, 20, 20);
        panelCreator.add(panel5, gbc);
        btnBack = new JButton();
        btnBack.setText("Main Menu");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 10);
        panel5.add(btnBack, gbc);
        btnContinue = new JButton();
        btnContinue.setEnabled(false);
        btnContinue.setText("Start Adventure");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel5.add(btnContinue, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel5.add(spacer2, gbc);
        final JSeparator separator3 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel5.add(separator3, gbc);
        label3.setLabelFor(textShipName);
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
        return panelCreator;
    }


}
