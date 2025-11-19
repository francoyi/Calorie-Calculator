
package com.caloriecalc.ui;
import javax.swing.*; import java.awt.*;
public class MainFrame extends JFrame {
    public MainFrame(JPanel mainPanel){
    super("CalorieCalc v3 — FoodItem Polymorphism");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(900, 600); setLocationRelativeTo(null);
    setLayout(new BorderLayout()); add(mainPanel, BorderLayout.CENTER);
  }
}
