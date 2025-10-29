
package com.caloriecalc.ui;
import com.caloriecalc.model.Meal;
import com.caloriecalc.model.MealEntry;
import com.caloriecalc.service.FoodLogService;
import javax.swing.*; import javax.swing.border.LineBorder;
import java.awt.*;
public class MealPanel extends JPanel {
  private final FoodLogService service; private final Meal meal;
  public MealPanel(FoodLogService service, Meal meal){
    this.service = service; this.meal = meal;
    setLayout(new BorderLayout(5,5)); setBorder(new LineBorder(new Color(220,220,220)));
    JLabel title = new JLabel(meal.getLabel()+" — "+meal.getDate()+"  (Total: "+String.format("%.1f", meal.getTotalKcal())+" kcal)");
    title.setBorder(BorderFactory.createEmptyBorder(5,5,5,5)); add(title, BorderLayout.NORTH);
    JTextArea area = new JTextArea(); area.setEditable(false);
    StringBuilder sb = new StringBuilder();
    for (MealEntry e : meal.getEntries()){
      sb.append("- ").append(e.input()).append("  →  ");
      sb.append(e.kcalForServing()==null?"N/A":String.format("%.1f kcal", e.kcalForServing()));
      if (e.source()!=null) sb.append("  (").append(e.source()).append(")"); sb.append("\n");
    }
    area.setText(sb.toString()); add(new JScrollPane(area), BorderLayout.CENTER);
    JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton edit = new JButton("Edit"); JButton del = new JButton("Delete"); btns.add(edit); btns.add(del);
    add(btns, BorderLayout.SOUTH);
    edit.addActionListener(e -> { MealDialog dlg = new MealDialog(SwingUtilities.getWindowAncestor(this), service, meal.getDate(), meal); dlg.setVisible(true); SwingUtilities.getWindowAncestor(this).dispose(); });
    del.addActionListener(e -> { int res = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this meal?", "Confirm delete", JOptionPane.YES_NO_OPTION);
      if (res == JOptionPane.YES_OPTION){ service.deleteMeal(meal.getDate(), meal.getId()); SwingUtilities.getWindowAncestor(this).dispose(); } });
  }
}
