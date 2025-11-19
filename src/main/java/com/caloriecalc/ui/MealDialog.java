
package com.caloriecalc.ui;
import com.caloriecalc.model.Meal;
import com.caloriecalc.model.MealEntry;
import com.caloriecalc.model.Serving;
import com.caloriecalc.service.FoodCalorieLookupService;
import com.caloriecalc.model.*;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.service.FoodLogService;
import com.caloriecalc.service.OpenFoodFactsClient;

import javax.swing.*; import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*; import java.time.LocalDate; import java.util.ArrayList; import java.util.List;


public class MealDialog extends JDialog {
  private final FoodLogService service;
  private final LocalDate date;
  private final Meal meal;
  private final JTextField labelField = new JTextField(20);
  private final JTextField notesField = new JTextField(30);
  private final MealTableModel tableModel;
  private final JLabel totalLabel = new JLabel("Total: 0 kcal");
  private final JButton recommendMealBtn = new JButton("Recommend Meal");
  private final FoodCalorieLookupService lookup;



    public MealDialog(Window owner, FoodLogService service, LocalDate date, Meal existing) {
        super(owner, existing == null ? "Add Meal" : "Edit Meal", ModalityType.APPLICATION_MODAL);
        this.service = service;
        this.date = date;
        this.meal = existing == null ? service.newEmptyMeal(date, "Meal") : existing;
        this.lookup = new FoodCalorieLookupService(service);
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

    private void onRecommendMeal() {
        // TODO: magic constant for now, more permanent solution later
        List<String> foodNames = List.of("apple", "banana", "steak", "salmon", "soybeans", "cereal", "cookie");
        List<FoodItem> recommendedFoods = new ArrayList<>();
        NutritionDataProvider provider = new OpenFoodFactsClient();
        for (String foodName : foodNames) {
            NutritionValues nv = provider.fetchNutritionPer100(foodName);
            APIFoodItem apifi = new APIFoodItem(foodName, nv, 200);
            recommendedFoods.add(apifi);
        }

        MealRecommender mr = new MealRecommender(recommendedFoods, service.getDailyGoal());
        List<Recommendation> lr = mr.getTopFoodRecommendations(1);
        if (lr.isEmpty()) { // Alternative flow: calorie goal unrealistic, must handle
            JOptionPane.showMessageDialog(this, "Unable to recommend meal within your goal. Attempt setting another goal.", "Error", JOptionPane.ERROR_MESSAGE);
        } else { // Main flow
            // Get the top recommendation
            Recommendation topRec = lr.get(0);
            for (FoodItem recommendedFood : topRec.getFoodItems()) {
                // The Recommender should ideally specify the suggested amount.
                double suggestedAmount = 100.0;
                String unit = "g";

                Double kcalForServing = null;
                NutritionValues nv = null;

                if (recommendedFood instanceof APIFoodItem apifi) {
                    nv = apifi.per100g();
                    if (nv != null) {
                        // Calculate kcal for the suggested amount (e.g., 100g)
                        kcalForServing = (nv.energyKcal() / 100.0) * suggestedAmount;
                    }
                }

                MealEntry recommendedEntry = new MealEntry(
                        recommendedFood.getName(), // name
                        recommendedFood.getName() + " " + suggestedAmount + unit, // input
                        new Serving(suggestedAmount, unit), // serving
                        kcalForServing, // kcalForServing
                        "Recommended", // source
                        java.time.ZonedDateTime.now(java.time.ZoneId.of("America/Toronto")), // fetchedAt
                        nv // nutritionPer100g
                );

                tableModel.addFromEntry(recommendedEntry);
            }

            recalcTotal();
        }
    }





  private void onSave(){
    meal.setLabel(labelField.getText().trim().isEmpty()? "Meal": labelField.getText().trim());
    meal.setNotes(notesField.getText().trim());
    List<MealEntry> entries = new ArrayList<>();
    for (MealRow r : tableModel.rows){
      if (r.item==null || r.item.isBlank()) continue;
      Double kcal = r.kcalManual;
      if (kcal == null){
        kcal = lookup.lookupKcal(r.item, r.amount, r.unit);}
      entries.add(new MealEntry(r.item, r.item+" "+r.amount+(r.unit==null?"":r.unit),
          new Serving(r.amount, r.unit), kcal, "Manual", java.time.ZonedDateTime.now(java.time.ZoneId.of("America/Toronto")), null));
    }
    meal.setEntries(entries); service.saveMeal(date, meal); dispose();
  }
  private void recalcTotal(){
    double total = 0.0; for (MealRow r : tableModel.rows){ if (r.kcalManual != null) total += r.kcalManual; }
    totalLabel.setText(String.format("Total: %.1f kcal", total));
  }
  private class MealRow {
    String item = ""; double amount = 100.0; String unit = "g"; Double kcalManual = null;
    public String toInput(){
        return item+" "+amount+unit;}

    boolean fromApi = false;
    boolean suggestionAvailable = false;
  }

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
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    showSuggestions();
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    showSuggestions();
                }

                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                }
            });
        }

        private void showSuggestions() {
            popup.setVisible(false);
            popup.removeAll();
            String text = field.getText();
            if (text.isEmpty()) return;

            List<String> suggestions = List.of();


            for (String s : suggestions) {
                JMenuItem item = new JMenuItem(s);
                item.setFocusable(false);
                item.setRequestFocusEnabled(false);
                item.addActionListener(e -> {
                    field.setText(s);
                    popup.setVisible(false);
                    SwingUtilities.invokeLater(()-> field.requestFocusInWindow());
                });
                popup.add(item);
            }

            JMenuItem create = new JMenuItem("Create own food");

            create.setFocusable(false);
            create.setRequestFocusEnabled(false);

            create.addActionListener(e ->
            {
                popup.setVisible(false);

                CreateFoodDialog dlg =
                        new CreateFoodDialog(SwingUtilities.getWindowAncestor(table), text,model.getService());
                CreateFoodDialog.CreateFood result = dlg.showDialog();

                if (result != null && !result.ingredients.isEmpty()) {
                    int row = table.getEditingRow();
                    MealRow r = model.getRows().get(row);
                    r.item = result.name;
                    r.kcalManual = result.totalKcal;
                    r.fromApi = false;

                    model.fireTableRowsUpdated(row, row);
                    field.setText(result.name);
                }
                SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
            });
            popup.addSeparator();
            popup.add(create);

            popup.show(field, 0, field.getHeight());

            SwingUtilities.invokeLater(() -> field.requestFocusInWindow());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int col) {
            field.setText(value == null ? "" : value.toString());
            SwingUtilities.invokeLater(() -> {
                showSuggestions();
                field.requestFocusInWindow();
            });
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

    public MealTableModel (FoodCalorieLookupService service, FoodCalorieLookupService lookup){
        this.service = service;
        this.lookup = lookup;
    }

      public FoodLogService getService() {
          return service;
      }

      public void addEmpty(){
        rows.add(new MealRow());
        fireTableRowsInserted(rows.size()-1, rows.size()-1); }

    public void addFromEntry(MealEntry e){
        MealRow r = new MealRow();
        r.item=e.name();
        r.amount=e.serving()!=null? e.serving().amount():100.0;
        r.unit=e.serving()!=null? e.serving().unit():"g";
        r.kcalManual=e.kcalForServing();
        try {
            r.fromApi = e.fetchedAt()!=null;
            r.suggestionAvailable = (r.kcalManual == null)&& !r.fromApi;
        }catch (Exception ignore){
            r.fromApi = false;
            r.suggestionAvailable = (r.kcalManual == null);
        }
        rows.add(r);
        fireTableRowsInserted(rows.size()-1, rows.size()-1); }

    public void remove(int row){
        rows.remove(row);
        fireTableRowsDeleted(row,row);
    }


    @Override public int getRowCount(){
        return rows.size();
    }


    @Override public int getColumnCount(){
        return cols.length;
    }
    @Override public String getColumnName(int c){
        return cols[c];
    }
    @Override public boolean isCellEditable(int r, int c){
        //return c != 4;
        return true;
    }
    @Override public Object getValueAt(int r, int c){
      MealRow row = rows.get(r);
      switch (c){
          case 0: row.item;
          case 1: row.amount;
          case 2: row.unit;
          case 3: row.kcalManual==null? "": String.format("%.1f", row.kcalManual);

          default:
              return "";
          };
    }
    @Override public void setValueAt(Object aValue, int r, int c){
      MealRow row = rows.get(r);
      switch (c){
        case 0 -> row.item = String.valueOf(aValue).trim();
        case 1 -> { try { row.amount = Double.parseDouble(String.valueOf(aValue)); } catch (Exception ignore){} }
        case 2 -> { String u = String.valueOf(aValue).trim().toLowerCase(); if (u.equals("g") || u.equals("ml")) row.unit = u; }
        case 3 -> { String s = String.valueOf(aValue).trim(); if (s.isEmpty()) row.kcalManual = null; else { try { row.kcalManual = Double.parseDouble(s); } catch (Exception ignore){} } }
      }
      fireTableCellUpdated(r,c);
      if ((c==0 || c==1 || c==2) && (row.kcalManual==null) && row.item!=null && !row.item.isBlank()){
        new Thread(() -> {
          Double kcal = lookup.lookupKcal(row.item, row.amount, row.unit);

          if (kcal != null) {
              row.kcalManual = kcal;
              row.fromApi = true;
              SwingUtilities.invokeLater(() -> {
                  fireTableRowsUpdated(r,r);
                  recalcTotal();
              });
            } else{
              row.suggestionAvailable = true;
              SwingUtilities.invokeLater(() -> fireTableRowsUpdated(r,r,));
          }
         }).start();
        } else{
          recalcTotal();
      }

    }
    @Override public Class<?> getColumnClass(int c){
        return (c==1)? Double.class : String.class;
    }

      private class ButtonEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor, java.awt.event.ActionListener {
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
                      CreateFoodDialog dlg = new CreateFoodDialog(SwingUtilities.getWindowAncestor(tableRef), r.item,service);
                      result = dlg.showDialog();
                  } catch (Exception ignored) {
                  }
                  if (result != null && result.ingredients != null && !result.ingredients.isEmpty()) {
                      r.kcalManual = result.ingredients.get(0).getKcal();
                      r.fromApi = false;
                      r.suggestionAvailable = false;
                      SwingUtilities.invokeLater(() -> {
                          fireTableRowsUpdated(editingRow, editingRow);
                          MealDialog.this.recalcTotal();
                      });
                  } else {
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
                              }
                          } catch (Exception ignored) {
                          }
                      }).start();
                  }
              }

              editingRow = -1;
          }
      }
  }
}




