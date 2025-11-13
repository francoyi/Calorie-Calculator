package com.caloriecalc.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class TDEEDialog extends JDialog {

    // Input Fields    private final JTextField ageField = new JTextField(30);
    private final JTextField weightField = new JTextField(30);
    private final JTextField heightField = new JTextField(30);
    private final JComboBox<String> sexField = new JComboBox<>(new String[]{"Male", "Female"});
    private final JComboBox<String> acitivityLevelField = new JComboBox<>(new String[]{"Very Light", "Light", "Medium", "High", "Extreme"});
    // Convert from imperial <-> metric
    private final JRadioButton metricBtn = new JRadioButton("Metric (kg, cm)", true);
    private final JRadioButton imperialBtn = new JRadioButton("Imperial (lb, in)");
    // Add result box
    private final JTextArea resultArea = new JTextArea(5, 28);

    public static class Result {
        public final double bmr;
        public final double tdee;
        public final String formula;
        public Result(double bmr, double tdee, String formula) {
            this.bmr = bmr;
            this.tdee = tdee;
            this.formula = formula;
        }
    }

    private Result result = null;

    public TDEEDialog(Window owner) {
        super(owner, "Expected Calorie Burn", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUI();
        wireActions();
        pack();
        setLocationRelativeTo(owner);
    }


}
