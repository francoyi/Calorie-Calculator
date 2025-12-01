
package com.caloriecalc.ui;
import com.caloriecalc.entity.Meal;
import com.caloriecalc.entity.MealEntry;
import com.caloriecalc.entity.MyFood;
import com.caloriecalc.entity.Serving;
import com.caloriecalc.interfaceadapters.ListMyFoods.ListMyFoodsController;
import com.caloriecalc.interfaceadapters.ListMyFoods.ListMyFoodsPresenter;
import com.caloriecalc.interfaceadapters.ListMyFoods.MyFoodsListViewModel;
import com.caloriecalc.interfaceadapters.SaveToMyFood.MyFoodsViewModel;
import com.caloriecalc.interfaceadapters.SaveToMyFood.SaveToMyFoodController;
import com.caloriecalc.interfaceadapters.SaveToMyFood.SaveToMyFoodPresenter;
import com.caloriecalc.usecase.foodcalorielookup.FoodCalorieLookupService;
import com.caloriecalc.usecase.foodcalorielookup.FoodLogService;
import com.caloriecalc.entity.MealRecommendationService;
import com.caloriecalc.usecase.myfoods.listmyfoods.ListMyFoodsInteractor;
import com.caloriecalc.usecase.myfoods.savetomyfood.SaveToMyFoodInteractor;
import com.caloriecalc.usecase.myfoods.listmyfoods.ListMyFoodsInputBoundary;
import com.caloriecalc.model.*;

import com.caloriecalc.usecase.myfoods.MyFoodRepository;
import com.caloriecalc.usecase.myfoods.savetomyfood.SaveToMyFoodOutputBoundary;
import com.caloriecalc.usecase.myfoods.savetomyfood.SaveToMyFoodInputBoundary;



import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MealDialog extends JDialog {
    private final FoodLogService service;
    private final LocalDate date;
    private final Meal meal;
    private final JTextField labelField = new JTextField(20);
    private final JTextField notesField = new JTextField(30);
    private MealTableModel tableModel;
    private final JLabel totalLabel = new JLabel("Total: 0 kcal");
    private final JButton recommendMealBtn = new JButton("Recommend Meal");
    private final MealRecommendationService mealRecommendationService;
    private final FoodCalorieLookupService lookup;
    private boolean autoLookupOnEdit = true;
    private final MyFoodRepository myFoodRepository;
    private final SaveToMyFoodController saveController;


    /** For external calls, used to turn off/on the automatic API lookup */
    public void setAutoLookupOnEdit(boolean enabled) {
        this.autoLookupOnEdit = enabled;
    }

    public MealDialog(Window owner, FoodLogService service, LocalDate date, Meal existing,
                      MealRecommendationService mealRecommendationService, MyFoodRepository myFoodRepository) {
        super(owner, existing == null ? "Add Meal" : "Edit Meal", ModalityType.APPLICATION_MODAL);
        this.service = service;
        this.date = date;
        this.mealRecommendationService = mealRecommendationService;
        this.meal = existing == null ? service.newEmptyMeal(date, "Meal") : existing;
        this.lookup = new FoodCalorieLookupService(service);
        this.myFoodRepository = myFoodRepository;
        MyFoodsViewModel vm = new MyFoodsViewModel();
        SaveToMyFoodOutputBoundary presenter = new SaveToMyFoodPresenter(vm);
        SaveToMyFoodInputBoundary interactor = new SaveToMyFoodInteractor(myFoodRepository, presenter);
        this.saveController = new SaveToMyFoodController(interactor);
        setSize(700, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));
        this.tableModel = new MealTableModel(service,lookup);
        JPanel top = new JPanel(new GridLayout(2, 2, 8, 8));
        top.add(new JLabel("Meal label:"));
        labelField.setText(this.meal.getLabel() == null ? "Meal" : this.meal.getLabel());
        top.add(labelField);
        top.add(new JLabel("Notes:"));
        notesField.setText(this.meal.getNotes() == null ? "" : this.meal.getNotes());
        top.add(notesField);
        add(top, BorderLayout.NORTH);
        JTable table = new JTable(tableModel);
        table.setRowHeight(24);

        TableColumn itemColumn = table.getColumnModel().getColumn(0);
        itemColumn.setCellEditor(new AutoCompleteEditor(tableModel, table));

        TableColumn fetchCol = table.getColumnModel().getColumn(4);
        fetchCol.setCellEditor(tableModel.new ButtonEditor(table));

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        JButton addRow = new JButton("Add Row");
        JButton removeRow = new JButton("Remove Row");
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.add(addRow);
        left.add(removeRow);
        bottom.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        right.add(totalLabel);
        right.add(recommendMealBtn);
        right.add(save);
        right.add(cancel);
        bottom.add(right, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        addRow.addActionListener(e -> tableModel.addEmpty());
        removeRow.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) tableModel.remove(row);
        });
        save.addActionListener(e -> onSave());
        cancel.addActionListener(e -> dispose());

        recommendMealBtn.addActionListener(e -> onRecommendMeal());
        if (existing != null) {
            for (MealEntry me : existing.getEntries()) {
                tableModel.addFromEntry(me);
            }
            recalcTotal();
        }
    }

    private void addChosenFoodToMealTable(MyFood chosen) {
        int row = tableModel.getRows().size() - 1;  // default to last row

        // If table is actively editing a row, update THAT row instead
        KeyboardFocusManager fm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        Component c = fm.getFocusOwner();
        JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, c);
        if (table != null && table.getEditingRow() >= 0) {
            row = table.getEditingRow();
        }

        MealRow r = tableModel.getRows().get(row);
        r.item = chosen.getName();
        r.amount = chosen.getAmount();      // ✔ THIS is the fix you wanted
        r.unit = chosen.getUnit();
        r.kcalManual = chosen.getTotalKcal();

        tableModel.fireTableRowsUpdated(row, row);
        recalcTotal();
    }



    private void onRecommendMeal() {
        while (tableModel.getRowCount() > 0) {
            tableModel.remove(0);
        }
        List<MealEntry> recommendedEntries = mealRecommendationService.recommendMealEntries();

        if (recommendedEntries.isEmpty()) {
            // Alternative flow: calorie goal unrealistic, must handle
            JOptionPane.showMessageDialog(this, "Unable to recommend meal within your goal. Attempt setting another goal.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Main flow: UI display logic
            HashMap<String, MealRow> meMap = new HashMap<>();
            for (MealEntry entry : recommendedEntries) {
                if (meMap.containsKey(entry.name())) {
                    meMap.get(entry.name()).amount = meMap.get(entry.name()).amount + 100;
                } else {
                    tableModel.addFromEntry(entry);
                    meMap.put(entry.name(), tableModel.rows.get(tableModel.rows.size() - 1));
                }
            }

            recalcTotal();
        }
    }

    private void onBrowseMyFoods() {
        // === Clean Architecture wiring ===
        MyFoodsListViewModel vm = new MyFoodsListViewModel();
        ListMyFoodsPresenter presenter = new ListMyFoodsPresenter(vm);
        ListMyFoodsInputBoundary interactor =
                new ListMyFoodsInteractor(myFoodRepository, presenter);
        ListMyFoodsController controller = new ListMyFoodsController(interactor);

        // Trigger the use case
        controller.listFoods();

        // === Open the dialog ===
        MyFoodsDialog dlg = new MyFoodsDialog(this, vm.getFoods());
        MyFood chosen = dlg.showDialog();

        if (chosen != null) {
            // Insert the chosen food into the table
            addChosenFoodToMealTable(chosen);
        }
    }

    private void onSave() {
        meal.setLabel(labelField.getText().trim().isEmpty() ? "Meal" : labelField.getText().trim());
        meal.setNotes(notesField.getText().trim());
        List<MealEntry> entries = new ArrayList<>();
        for (MealRow r : tableModel.rows) {
            if (r.item == null || r.item.isBlank()) continue;
            Double kcal = r.kcalManual;
            if (kcal == null) {
                try {
                    MealEntry tmp = service.resolveEntryFromInput(r.item + " " + r.amount + (r.unit == null ? "" : r.unit));
                    kcal = tmp.kcalForServing();
                    entries.add(new MealEntry(tmp.name(), r.item + " " + r.amount + (r.unit == null ? "" : r.unit),
                            new Serving(r.amount, r.unit), kcal, kcal == null ? "Manual" : "OpenFoodFacts", tmp.fetchedAt(), tmp.nutritionPer100g()));
                    continue;
                } catch (Exception ex) {
                }
            }
            entries.add(new MealEntry(r.item, r.item + " " + r.amount + (r.unit == null ? "" : r.unit),
                    new Serving(r.amount, r.unit), kcal, "Manual", java.time.ZonedDateTime.now(java.time.ZoneId.of("America/Toronto")), null));
        }
        meal.setEntries(entries);
        service.saveMeal(date, meal);
        dispose();
    }

    private void recalcTotal() {
        double total = 0.0;
        for (MealRow r : tableModel.rows) {
            if (r.kcalManual != null) total += r.kcalManual;
        }
        totalLabel.setText(String.format("Total: %.1f kcal", total));
    }

    private class MealRow {
        String item = "";
        double amount = 0.0;
        String unit = "g";
        Double kcalManual = null;

        boolean fromApi = false;
        boolean suggestionAvailable = false;

        public String toInput() {
            return item + " " + amount + unit;
        }
    }

    /**
     * The editor for the Item column: A menu pops up when you enter the cell.
     * The menu disappears when inputting.
     * Selecting "Create own food" will open
     * the CreateFoodDialog and write totalKcal to the current line
     */
    private class AutoCompleteEditor extends AbstractCellEditor implements TableCellEditor {

        private final JTextField field = new JTextField();
        private final MealTableModel model;
        private final JPopupMenu popup = new JPopupMenu();
        private final JTable table;

        public AutoCompleteEditor(MealTableModel model, JTable table) {
            this.model = model;
            this.table = table;
            popup.setFocusable(false);

            field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    pushToModel();
                    SwingUtilities.invokeLater(() -> showSuggestions());
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    pushToModel();
                    SwingUtilities.invokeLater(() -> showSuggestions());
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    // no-op
                }
            });

        }
        private void hidePopup() {
            if (popup.isVisible()) popup.setVisible(false);
        }

        private void updateCreateButton() {
            pushToModel();
        }

        // Push edits INTO the model while typing
        private void pushToModel() {
            int row = table.getEditingRow();
            if (row < 0) return;

            MealRow r = model.getRows().get(row);

            r.item = field.getText();
            r.suggestionAvailable = true;

            model.fireTableCellUpdated(row, 0);
            model.fireTableCellUpdated(row, 4); // repaint button column
        }


        private void showSuggestions() {
            if (!field.isDisplayable() || !field.isShowing()) {
                return;
            }

            popup.setVisible(false);
            popup.removeAll();

            String text = field.getText();

            List<String> suggestions = List.of();
            for (String s : suggestions) {
                JMenuItem item = new JMenuItem(s);
                item.setFocusable(false);
                item.setRequestFocusEnabled(false);
                item.addActionListener(e -> {
                    popup.setVisible(false);
                    field.setText(s);
                    SwingUtilities.invokeLater(field::requestFocusInWindow);
                });
                popup.add(item);
            }
            JMenuItem browse = new JMenuItem("Browse My Foods");
            browse.addActionListener(e -> {

                // CA wiring for ListMyFoods
                MyFoodsListViewModel vm = new MyFoodsListViewModel();
                ListMyFoodsPresenter presenter = new ListMyFoodsPresenter(vm);
                ListMyFoodsInputBoundary interactor =
                        new ListMyFoodsInteractor(MealDialog.this.myFoodRepository, presenter);
                ListMyFoodsController controller = new ListMyFoodsController(interactor);

                controller.listFoods();

                // Display dialog
                MyFoodsDialog dlg = new MyFoodsDialog(SwingUtilities.getWindowAncestor(table), vm.getFoods());
                MyFood chosen = dlg.showDialog();

                if (chosen != null) {
                    // Insert into Meal table
                    int row = table.getEditingRow();
                    if (row >= 0) {
                        MealRow r = model.getRows().get(row);

                        // ✔ Update values in the existing row
                        r.item = chosen.getName();
                        r.amount = chosen.getAmount();
                        r.unit = chosen.getUnit();
                        r.kcalManual = chosen.getTotalKcal();

                        r.fromApi = false;
                        r.suggestionAvailable = false;

                        // Update editor field to show correct name
                        field.setText(chosen.getName());

                        //  Refresh table + totals
                        model.fireTableRowsUpdated(row, row);
                        MealDialog.this.recalcTotal();
                    }
                }
            });
            popup.add(browse);




            JMenuItem create = new JMenuItem("Create own recipe");
            create.setFocusable(false);
            create.setRequestFocusEnabled(false);
            create.addActionListener(e -> {
                popup.setVisible(false);

                SaveToMyFoodController saveController = MealDialog.this.saveController;

                CreateFoodDialog dlg =
                        new CreateFoodDialog(
                                SwingUtilities.getWindowAncestor(table),
                                text,
                                model.getService(),
                                saveController
                        );
                CreateFoodDialog.CreateFood result = dlg.showDialog();

                if (result != null && !result.ingredients.isEmpty()) {
                    int row = table.getEditingRow();
                    if (row >= 0) {
                        MealRow r = model.getRows().get(row);
                        r.item = result.name;
                        r.kcalManual = result.totalKcal;
                        r.fromApi = false;
                        r.suggestionAvailable = false;

                        model.fireTableRowsUpdated(row, row);
                        field.setText(result.name);
                        recalcTotal();
                    }
                }

                MyFoodsListViewModel refreshVM = new MyFoodsListViewModel();
                ListMyFoodsPresenter refreshPresenter = new ListMyFoodsPresenter(refreshVM);
                ListMyFoodsInputBoundary refreshInteractor =
                        new ListMyFoodsInteractor(MealDialog.this.myFoodRepository, refreshPresenter);
                new ListMyFoodsController(refreshInteractor).listFoods();

                SwingUtilities.invokeLater(field::requestFocusInWindow);
            });
            popup.addSeparator();
            popup.add(create);

            popup.show(field, 0, field.getHeight());
            SwingUtilities.invokeLater(field::requestFocusInWindow);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int col) {
            field.setText(value == null ? "" : value.toString());
            // 一点进单元格就弹 popup
            SwingUtilities.invokeLater(this::showSuggestions);
            return field;
        }

        @Override
        public Object getCellEditorValue() {
            return field.getText();
        }
    }

    private class MealTableModel extends AbstractTableModel {
        private final String[] cols = {"Item (e.g., banana)", "Amount", "Unit (g/ml)", "kcal (auto or manual)","Fetch"};
        private final java.util.List<MealRow> rows = new java.util.ArrayList<>();
        private final FoodLogService service;
        private final FoodCalorieLookupService lookup;

        public MealTableModel (FoodLogService service, FoodCalorieLookupService lookup){
            this.service = service;
            this.lookup = lookup;
        }

        public java.util.List<MealRow> getRows() {
            return rows;
        }

        public FoodLogService getService() {
          return service;
      }

        public void addEmpty() {
            MealRow r = new MealRow();
            r.amount = 100.0;       // ✔ GOOD: neutral default
            r.unit = "g";         // still okay or leave empty
            rows.add(r);
            fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
        }

        public void addFromEntry(MealEntry e) {
            MealRow r = new MealRow();
            r.item = e.name();
            r.amount = e.serving() != null ? e.serving().amount() : 100.0;
            r.unit = e.serving() != null ? e.serving().unit() : "g";
            r.kcalManual = e.kcalForServing();
            rows.add(r);
            fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
        }

        public void remove(int row) {
            rows.remove(row);
            fireTableRowsDeleted(row, row);
        }

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public int getColumnCount() {
            return cols.length;
        }

        @Override
        public String getColumnName(int c) {
            return cols[c];
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c != 4;
        }

        @Override
        public Object getValueAt(int r, int c) {
            MealRow row = rows.get(r);
            return switch (c) {
                case 0 -> row.item;
                case 1 -> row.amount;
                case 2 -> row.unit;
                case 3 -> row.kcalManual == null ? "" : String.format("%.1f", row.kcalManual);
                case 4 -> "Fetch";
                default -> "";
            };
        }

        @Override
        public void setValueAt(Object aValue, int r, int c) {
            MealRow row = rows.get(r);

            switch (c) {
                case 0 -> {
                    String newName = String.valueOf(aValue).trim();
                    if (!newName.equals(row.item)) {
                        row.item = newName;
                        row.kcalManual = null;
                        row.fromApi = false;
                        row.suggestionAvailable = true;
                    }
                }
                case 1 -> {
                    try {
                        row.amount = Double.parseDouble(String.valueOf(aValue));
                    } catch (Exception ignore) {}
                }
                case 2 -> {
                    String u = String.valueOf(aValue).trim().toLowerCase();
                    if (u.equals("g") || u.equals("ml")) row.unit = u;
                }
                case 3 -> {
                    String s = String.valueOf(aValue).trim();
                    if (s.isEmpty()) {
                        row.kcalManual = null;
                    } else {
                        try {
                            row.kcalManual = Double.parseDouble(s);
                        } catch (Exception ignore) {}
                    }
                    row.fromApi = false;
                    row.suggestionAvailable = false;
                }
                default -> {}
            }

            fireTableCellUpdated(r, c);
            fireTableCellUpdated(r, 4);

            boolean needLookup = false;

            if (c == 0) {
                needLookup = row.item != null && !row.item.isBlank();
            } else if (c == 1 || c == 2) {

                needLookup = row.fromApi
                        && row.item != null
                        && !row.item.isBlank();
            }

            if (autoLookupOnEdit && needLookup) {
                new Thread(() -> {
                    try {
                        MealEntry tmp = service.resolveEntryFromInput(
                                row.item + " " + row.amount + row.unit
                        );
                        if (tmp.kcalForServing() != null) {
                            row.kcalManual = tmp.kcalForServing();
                            row.fromApi = true;
                            row.suggestionAvailable = false;

                            SwingUtilities.invokeLater(() -> {
                                fireTableRowsUpdated(r, r);
                                recalcTotal();
                            });
                        }
                    } catch (Exception ignored) {}
                }).start();
            } else {
                recalcTotal();
            }
        }


        @Override
        public Class<?> getColumnClass(int c) {
            return (c == 1) ? Double.class : String.class;
        }

        /** The button editor for the Fetch column */
        public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, java.awt.event.ActionListener {
            private final JButton button = new JButton();
            private int editingRow = -1;
            private final JTable tableRef;

            public ButtonEditor(JTable table) {
                this.tableRef = table;
                button.addActionListener(this);
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                editingRow = row;
                MealRow r = rows.get(row);
                if (r.suggestionAvailable) {
                    button.setText("Create local food");
                } else if (r.fromApi) {
                    button.setText("From API");
                } else {
                    button.setText("Fetch");
                }
                button.setEnabled(!r.fromApi);
                return button;
            }

            @Override
            public Object getCellEditorValue() {
                return "";
            }

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                stopCellEditing();
                if (editingRow < 0) return;
                MealRow r = rows.get(editingRow);
                if (r.fromApi) {
                    editingRow = -1;
                    return;
                }

                if (r.suggestionAvailable) {
                    CreateFoodDialog.CreateFood result = null;
                    try {
                        SaveToMyFoodController saveController = MealDialog.this.saveController;

                        CreateFoodDialog dlg =
                                new CreateFoodDialog(SwingUtilities.getWindowAncestor(tableRef), r.item, service,saveController);
                        result = dlg.showDialog();
                    } catch (Exception ignored) {
                    }

                    if (result != null && result.ingredients != null && !result.ingredients.isEmpty()) {
                        r.item = result.name;
                        r.kcalManual = result.totalKcal;
                        r.fromApi = false;
                        r.suggestionAvailable = false;

                        SwingUtilities.invokeLater(() -> {
                            fireTableRowsUpdated(editingRow, editingRow);
                            MealDialog.this.recalcTotal();
                        });
                    } else if (MealDialog.this.autoLookupOnEdit) {
                        new Thread(() -> {
                            try {
                                Double kcal = lookup.lookupKcal(
                                        r.item,
                                        r.amount,
                                        r.unit == null ? "" : r.unit
                                );

                                if (kcal != null) {
                                    r.kcalManual = kcal;
                                    r.fromApi = true;
                                    r.suggestionAvailable = false;

                                    SwingUtilities.invokeLater(() -> {
                                        fireTableRowsUpdated(editingRow, editingRow);
                                        MealDialog.this.recalcTotal();
                                    });
                                } else {
                                    r.suggestionAvailable = true;
                                    SwingUtilities.invokeLater(
                                            () -> fireTableRowsUpdated(editingRow, editingRow)
                                    );
                                }
                            } catch (Exception ignored) {
                            }
                        }).start();
                    }

                    editingRow = -1;
                }
            }
        }
    }
}