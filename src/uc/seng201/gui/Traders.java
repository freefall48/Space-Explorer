package uc.seng201.gui;

import uc.seng201.environment.GameEnvironment;
import uc.seng201.environment.GameState;
import uc.seng201.items.SpaceItem;
import uc.seng201.utils.observerable.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Traders extends JDialog {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton btnBuy;
    private JButton btnLeave;
    private JList<ItemModelEntry> listAvailableItems;
    private JLabel lblBalance;

    private DefaultListModel<ItemModelEntry> availableItems;
    private GameState gameState;

    public Traders(GameState gameState) {
        this.gameState = gameState;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnBuy);

        this.availableItems = new DefaultListModel<>();
        this.listAvailableItems.setModel(this.availableItems);

        this.listAvailableItems.addListSelectionListener(e -> onBuyMenuSelection());
        repaintWindow();

        btnBuy.addActionListener(e -> onBuy());
        btnLeave.addActionListener(e -> onLeave());

        // call onLeave() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onLeave();
            }
        });

        // call onLeave() on ESCAPE
        contentPane.registerKeyboardAction(e -> onLeave(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void repaintWindow() {
        lblBalance.setText(String.format("$%s", gameState.getSpaceShip().getBalance()));
        int currentlySelected = listAvailableItems.getSelectedIndex();
        availableItems.clear();
        gameState.getTrader().getAvailableItems().forEach((item, quantity) -> availableItems.addElement
                (new ItemModelEntry(item, quantity)));

        if (currentlySelected == -1 || currentlySelected >= availableItems.size()) {
            listAvailableItems.setSelectedIndex(0);
        } else {
            listAvailableItems.setSelectedIndex(currentlySelected);
        }
        contentPane.repaint();
    }


    private void onBuyMenuSelection() {
        if (listAvailableItems.getSelectedIndex() >= 0) {
            btnBuy.setEnabled(false);
            SpaceItem item = listAvailableItems.getSelectedValue().spaceItem;
            btnBuy.setText("Buy: $" + item.getPrice());
            btnBuy.repaint();
            if (item.getPrice() <= gameState.getSpaceShip().getBalance()) {
                btnBuy.setEnabled(true);
            }
        }
    }

    private void onBuy() {
        ItemModelEntry itemModelEntry = listAvailableItems.getSelectedValue();
        if (itemModelEntry.quantity > 0) {
            GameEnvironment.eventManager.notifyObservers(Event.BUY_FROM_TRADERS, itemModelEntry.spaceItem);
        }
        if (availableItems.size() == 0) {
            btnBuy.setEnabled(false);
        }
        repaintWindow();
    }

    private void onLeave() {
        dispose();
    }

    final class ItemModelEntry {
        final int quantity;
        final SpaceItem spaceItem;

        ItemModelEntry(SpaceItem spaceItem, int quantity) {
            this.spaceItem = spaceItem;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return String.format("%d x %s - %s", quantity, spaceItem, spaceItem.getItemDescription());
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
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 20, 0);
        contentPane.add(panel1, gbc);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        btnLeave = new JButton();
        btnLeave.setText("Leave");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 0);
        panel2.add(btnLeave, gbc);
        btnBuy = new JButton();
        btnBuy.setEnabled(false);
        btnBuy.setText("Buy");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel2.add(btnBuy, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 0, 20);
        contentPane.add(panel3, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(panel4, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, -1, 36, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        label1.setText("Space Traders");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel4.add(label1, gbc);
        final JSeparator separator1 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel4.add(separator1, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, -1, 16, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setHorizontalAlignment(0);
        label2.setText("Balance:");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 10.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel4.add(label2, gbc);
        lblBalance = new JLabel();
        Font lblBalanceFont = this.$$$getFont$$$(null, -1, 16, lblBalance.getFont());
        if (lblBalanceFont != null) lblBalance.setFont(lblBalanceFont);
        lblBalance.setText("####");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 0);
        panel4.add(lblBalance, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 10.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel4.add(spacer1, gbc);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.ipadx = 10;
        gbc.ipady = 10;
        panel3.add(panel5, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 10.0;
        gbc.weighty = 10.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel5.add(scrollPane1, gbc);
        listAvailableItems = new JList();
        Font listAvailableItemsFont = this.$$$getFont$$$(null, -1, 14, listAvailableItems.getFont());
        if (listAvailableItemsFont != null) listAvailableItems.setFont(listAvailableItemsFont);
        listAvailableItems.setSelectionMode(0);
        scrollPane1.setViewportView(listAvailableItems);
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
