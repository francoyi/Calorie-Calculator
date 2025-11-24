// java
package com.caloriecalc.ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.caloriecalc.model.Ingredient;
import com.caloriecalc.port.savetomyfood.SaveToMyFoodInputData;
import com.caloriecalc.service.FoodLogService;
import com.caloriecalc.service.FoodCalorieLookupService;
import com.caloriecalc.ui.myfoods.SaveToMyFoodController;
import com.caloriecalc.ui.myfoods.SaveToMyFoodPresenter;
import com.caloriecalc.ui.myfoods.MyFoodsViewModel;
import com.caloriecalc.port.MyFoodRepository;
import com.caloriecalc.repo.InMemoryMyFoodRepository;
import com.caloriecalc.service.SaveToMyFoodInteractor;
import com.caloriecalc.port.savetomyfood.SaveToMyFoodOutputBoundary;
import com.caloriecalc.port.savetomyfood.SaveToMyFoodInputBoundary;

public class CreateFoodDialog extends JDialog {
    private final JTextField nameField = new JTextField(30);
    private final IngredientTableModel tableModel = new IngredientTableModel();
    private final JLabel totalLabel = new JLabel("Total kcal: 0");
    private CreateFood result = null;
    private final FoodLogService service;
    private final FoodCalorieLookupService lookup;
    private final SaveToMyFoodController saveMyFoodController;


    public CreateFoodDialog(Window owner, String suggestedName, FoodLogService service) {
        super(owner, "Create Own Recipe", ModalityType.APPLICATION_MODAL);
        this.service = service;
        this.lookup = new FoodCalorieLookupService(service);
        setLayout(new BorderLayout(8, 8));
        MyFoodsViewModel vm = new MyFoodsViewModel();
        SaveToMyFoodPresenter presenter = new SaveToMyFoodPresenter(vm);
        SaveToMyFoodInteractor interactor =
                new SaveToMyFoodInteractor(service.getMyFoodRepository(), presenter);
        this.saveMyFoodController = new SaveToMyFoodController(interactor);

        if (suggestedName != null) nameField.setText(suggestedName);

        // TOP
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Food name:"));
        top.add(nameField);
        add(top, BorderLayout.NORTH);

        // CENTER (table)
        JTable table = new JTable(tableModel);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // CONTROLS
        JPanel controls = new JPanel(new BorderLayout());

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add ingredient");
        JButton remove = new JButton("Remove selected");
        left.add(add);
        left.add(remove);
        controls.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK and save to My Foods");
        JButton cancel = new JButton("Cancel");
        right.add(ok);
        right.add(cancel);
        controls.add(right, BorderLayout.EAST);

        // BOTTOM PANEL (total + controls)
        JPanel bottomAll = new JPanel(new BorderLayout());
        bottomAll.add(totalLabel, BorderLayout.NORTH);
        bottomAll.add(controls, BorderLayout.SOUTH);

        add(bottomAll, BorderLayout.SOUTH);

        // Actions
        add.addActionListener(e -> tableModel.addEmpty());
        remove.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) tableModel.remove(r);
        });

        ok.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a food name.",
                        "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Ingredient> list = new ArrayList<>();
            for (IngredientRow row : tableModel.rows) {
                if (row.name == null || row.name.trim().isEmpty()) continue;
                list.add(new Ingredient(row.name.trim(), row.amount, row.unit, row.kcal));
            }
            // Trigger SaveToMyFood use case
            SaveToMyFoodInputData data = new SaveToMyFoodInputData(name, list);
            saveMyFoodController.execute(data);

            // Return result for MealDialog (existing behavior)
            result = new CreateFood(name, list);
            dispose();


        });
        cancel.addActionListener(e -> { result = null; dispose(); });

        pack();
        setLocationRelativeTo(owner);
    }



    public CreateFood showDialog() {
        setVisible(true);
        return result;
    }

    public static class CreateFood {
        public final String name;
        public final java.util.List<com.caloriecalc.model.Ingredient> ingredients;
        public final double totalKcal;

        public CreateFood(String name, java.util.List<com.caloriecalc.model.Ingredient> ingredients) {
            this.name = name;
            this.ingredients = ingredients;
            double sum = 0;
            for (Ingredient i : ingredients) {
                if (i.getKcal() != null) sum += i.getKcal();

            }
            this.totalKcal = sum;
        }
    }

    private static class IngredientRow {
        String name = "";
        double amount = 100.0;
        String unit = "g";
        Double kcal = null;
    }

    private class IngredientTableModel extends AbstractTableModel {
        private final String[] cols = {"Item", "Amount", "Unit(g/ml)", "kcal(auto or manual)"};
        private final java.util.List<IngredientRow> rows = new java.util.ArrayList<>();

        public IngredientTableModel() { addEmpty(); }

        public void addEmpty() { rows.add(new IngredientRow()); fireTableRowsInserted(rows.size()-1, rows.size()-1); }
        public void remove(int idx) { if (idx>=0 && idx<rows.size()) { rows.remove(idx); fireTableRowsDeleted(idx, idx); } }

        @Override public int getRowCount() { return rows.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }
        @Override public boolean isCellEditable(int r, int c) { return true; }
        @Override public Class<?> getColumnClass(int c) { return (c==1 || c==3) ? Double.class : String.class; }

        @Override
        public Object getValueAt(int r, int c) {
            IngredientRow row = rows.get(r);
            switch (c) {
                case 0: return row.name;
                case 1: return row.amount;
                case 2: return row.unit;
                case 3: return row.kcal;
                default: return "";
            }
        }

        private void recalcTotal() {
            double sum = 0;
            for (IngredientRow row : tableModel.rows) {
                if (row.kcal != null) {
                    sum += row.kcal;
                }
            }
            totalLabel.setText("Total kcal: " + sum);
        }



        @Override
        public void setValueAt(Object aValue, int r, int c) {
            IngredientRow row = rows.get(r);
                switch (c) {
                    case 0:
                        row.name = String.valueOf(aValue).trim();
                        break;
                    case 1:
                        try { row.amount = Double.parseDouble(String.valueOf(aValue)); }
                        catch (Exception ignore) {}
                        break;
                    case 2:
                        String u = String.valueOf(aValue).trim().toLowerCase();
                        if (u.equals("g") || u.equals("ml")) row.unit = u;
                        break;
                    case 3:
                        try {
                            String s = String.valueOf(aValue).trim();
                            row.kcal = (s.isEmpty() ? null : Double.parseDouble(s));
                        } catch (Exception ignore) {}
                        break;
                }

                fireTableCellUpdated(r, c);

                // Auto-fetch kcal from lookup service
                if ((c == 0 || c == 1 || c == 2) &&
                        row.kcal == null &&
                        row.name != null &&
                        !row.name.isBlank()) {

                    new Thread(() -> {
                        try {
                            Double k = lookup.lookupKcal(row.name, row.amount, row.unit);
                            if (k != null) {
                                row.kcal = k;
                                SwingUtilities.invokeLater(() -> fireTableRowsUpdated(r, r));
                                recalcTotal();
                            }
                        } catch (Exception ignored) {}
                    }).start();
                }
            recalcTotal();
            }
        }

    }

