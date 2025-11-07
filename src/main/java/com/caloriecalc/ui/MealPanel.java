
package com.caloriecalc.ui;
import com.caloriecalc.model.Meal;
import com.caloriecalc.model.MealEntry;
import com.caloriecalc.service.FoodLogService;
import javax.swing.*; import javax.swing.border.LineBorder;
import java.awt.*;
public class MealPanel extends JPanel {
  private final FoodLogService service;
  private final Meal meal;
  private Runnable onChanged = () -> {};

  public MealPanel(FoodLogService service, Meal meal){
    this.service = service;
    this.meal = meal;
    setLayout(new BorderLayout(5,5)); setBorder(new LineBorder(new Color(220,220,220)));
    JLabel title = new JLabel(meal.getLabel()+" — "+meal.getDate()+"  (Total: "+String.format("%.1f", meal.getTotalKcal())+" kcal)");
    title.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    add(title, BorderLayout.NORTH);
    JTextArea area = new JTextArea();
    area.setEditable(false);
    StringBuilder sb = new StringBuilder();
    for (MealEntry e : meal.getEntries()){
      sb.append("- ").append(e.input()).append("  →  ");
      sb.append(e.kcalForServing()==null?"N/A":String.format("%.1f kcal", e.kcalForServing()));
      if (e.source()!=null) sb.append("  (").append(e.source()).append(")");
      sb.append("\n");
    }
    area.setText(sb.toString());
    add(new JScrollPane(area), BorderLayout.CENTER);
    JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton edit = new JButton("Edit");
    JButton del = new JButton("Delete");
    btns.add(edit);
    btns.add(del);
    add(btns, BorderLayout.SOUTH);

    //Implement the meal Panel edit method
    edit.addActionListener(e -> {
      Window owner = SwingUtilities.getWindowAncestor(this);
      MealDialog dlg = new MealDialog(owner, service, meal.getDate(), meal);
      dlg.setVisible(true);
      if (onChanged != null) onChanged.run();   //only refresh
    });

    //Implement the meal Panel delete method
    del.addActionListener(e -> {
      int res = JOptionPane.showConfirmDialog(this, "Delete this meal?", "Confirm delete",
              JOptionPane.YES_NO_OPTION);
      if (res == JOptionPane.YES_OPTION) {
        try {
          service.deleteMeal(meal.getDate(), meal.getId());
          if (onChanged != null) onChanged.run();  // only refresh
        } catch (RuntimeException ex) {
          JOptionPane.showMessageDialog(this, "Failed to delete: " + ex.getMessage(),
                  "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
  }

  public void setOnChanged(Runnable onChanged) {
    this.onChanged = (onChanged != null) ? onChanged : () -> {};
  }

}