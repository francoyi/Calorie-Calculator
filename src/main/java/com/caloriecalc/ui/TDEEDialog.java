package com.caloriecalc.ui;

import javax.swing.*;
import java.awt.*;

import com.caloriecalc.port.tdee.CalculateTDEEInputBoundary;
import com.caloriecalc.port.tdee.CalculateTDEEOutputBoundary;
import com.caloriecalc.service.CalculateTDEEInteractor;
import com.caloriecalc.service.MifflinStJeorBMR;

public class TDEEDialog extends JDialog {

    // Input Fields
    private final JTextField ageField = new JTextField(30);
    private final JTextField weightField = new JTextField(30);
    private final JTextField heightField = new JTextField(30);
    private final JComboBox<String> sexField = new JComboBox<>(new String[]{"Male", "Female"});
    private final JComboBox<String> acitivityLevelField = new JComboBox<>(new String[]{"Very Light", "Light", "Medium", "High", "Extreme"});
    // Convert from imperial <-> metric
    private final JRadioButton metricBtn = new JRadioButton("Metric (kg, cm)", true);
    private final JRadioButton imperialBtn = new JRadioButton("Imperial (lb, in)");
    // Other Buttons
    private final JButton calcBtn = new JButton("Calculate");
    private final JButton closeBtn = new JButton("Close Window");
    private final JButton setGoalBtn = new JButton("Set as calorie Goal");
    // Add result box
    private final JTextArea resultArea = new JTextArea(5, 28);


    private final CalculateTDEEInputBoundary interactor;
    private final CalculateTDEEOutputBoundary presenter = null;

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
        super(owner, "Expected Calorie Burn Calculator (TDEE)", ModalityType.APPLICATION_MODAL);

        this.interactor = new CalculateTDEEInteractor(new MifflinStJeorBMR(), presenter);

        makeUI();
        wireActions();

        pack();
        setLocationRelativeTo(owner);
    }

    private void makeUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        ButtonGroup units = new ButtonGroup();
        units.add(metricBtn);
        units.add(imperialBtn);

        JPanel form = new JPanel(new GridBagLayout());

    }

    private void wireActions() {
        calcBtn.addActionListener(e -> onCalculate());
        closeBtn.addActionListener(e -> dispose());
        setGoalBtn.addActionListener(e -> setAsGoal());
    }

    private void setAsGoal() {
    }

    private void onCalculate() {
    }

}
