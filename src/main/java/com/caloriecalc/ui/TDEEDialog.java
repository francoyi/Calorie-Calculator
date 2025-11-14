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
    private final JComboBox<String> activityLevelField = new JComboBox<>(new String[]{"Very Light", "Light", "Medium", "High", "Extreme"});
    // Convert from imperial <-> metric
    private final JRadioButton metricBtn = new JRadioButton("Metric (kg, cm)", true);
    private final JRadioButton imperialBtn = new JRadioButton("Imperial (lb, in)");
    // Other Buttons
    private final JButton calcBtn = new JButton("Calculate");
    private final JButton setGoalBtn = new JButton("Set as Calorie Burn Goal");
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

        setSize(700, 500);

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

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        formPanel.add(ageField);
        formPanel.add(weightField);
        formPanel.add(heightField);
        formPanel.add(sexField);
        formPanel.add(activityLevelField);

        JPanel btns = new JPanel();
        btns.setLayout(new BoxLayout(btns, BoxLayout.X_AXIS));
        btns.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btns.add(calcBtn);
        btns.add(setGoalBtn);

        JPanel unitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        unitPanel.add(metricBtn); unitPanel.add(imperialBtn);

        JPanel root = new JPanel(new BorderLayout(5,8));
        root.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        root.add(formPanel, BorderLayout.NORTH);
        root.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        root.add(btns, BorderLayout.SOUTH);
        root.add(unitPanel, BorderLayout.SOUTH);
        setContentPane(root);



    }

    private void wireActions() {
        calcBtn.addActionListener(e -> onCalculate());
        setGoalBtn.addActionListener(e -> setAsGoal());
    }

    private void setAsGoal() {
    }

    private void onCalculate() {
    }

}
