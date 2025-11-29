package com.caloriecalc.ui.myfoods;

import com.caloriecalc.model.MyFood;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

public class MyFoodsDialog extends JDialog {

    private final FoodsTableModel tableModel;
    private final JTable table;
    private MyFood selectedFood = null;

    public MyFoodsDialog(Window owner, List<MyFood> foods) {
        super(owner, "My Foods", ModalityType.APPLICATION_MODAL);

        this.tableModel = new FoodsTableModel(foods);
        this.table = new JTable(tableModel);
        table.setRowHeight(24);

        setLayout(new BorderLayout(8, 8));
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectBtn = new JButton("Select");
        JButton cancelBtn = new JButton("Cancel");

        bottom.add(selectBtn);
        bottom.add(cancelBtn);
        add(bottom, BorderLayout.SOUTH);

        // Actions
        selectBtn.addActionListener(e -> onSelect());
        cancelBtn.addActionListener(e -> {
            selectedFood = null;
            dispose();
        });

        setSize(500, 350);
        setLocationRelativeTo(owner);
    }

    private void onSelect() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a food.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedFood = tableModel.getFoodAt(row);
        dispose();
    }

    /** Called by MealDialog */
    public MyFood showDialog() {
        setVisible(true);
        return selectedFood;
    }


    // ===================== TABLE MODEL ===================== //

    private static class FoodsTableModel extends AbstractTableModel {

        private final String[] cols = {"Name", "Total kcal", "Ingredients count"};
        private final List<MyFood> foods;

        public FoodsTableModel(List<MyFood> foods) {
            this.foods = foods;
        }

        @Override
        public int getRowCount() {
            return foods.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int col) {
            return cols[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            MyFood food = foods.get(row);
            return switch (col) {
                case 0 -> food.getName();
                case 1 -> String.format("%.1f", food.getTotalKcal());
                case 2 -> food.getIngredients().size();
                default -> "";
            };
        }

        public MyFood getFoodAt(int row) {
            return foods.get(row);
        }
    }
}