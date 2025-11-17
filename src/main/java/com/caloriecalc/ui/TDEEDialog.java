package com.caloriecalc.ui;

import javax.swing.*;
import java.awt.*;

import com.caloriecalc.model.ActivityLevel;
import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.*;
import com.caloriecalc.service.CalculateTDEEInteractor;
import com.caloriecalc.service.MifflinStJeorBMR;

public class TDEEDialog extends JDialog implements CalculateTDEEOutputBoundary{

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

    @Override
    public void present(CalculateTDEEOutputData output) {

    }

    @Override
    public void presentValidationError(String message) {

    }

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
        //FlatLightLaf.setup();


        setSize(700, 500);

        this.interactor = new CalculateTDEEInteractor(new MifflinStJeorBMR(), this);

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
        formPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        formPanel.add(new JLabel("Enter your Age:"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Enter your Weight:"));
        formPanel.add(weightField);
        formPanel.add(new JLabel("Enter your Height:"));
        formPanel.add(heightField);
        formPanel.add(new JLabel("Select your Sex."));
        formPanel.add(sexField);
        formPanel.add(new JLabel("Select your Activity Level:"));
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
        // root.add(unitPanel, BorderLayout.SOUTH);
        setContentPane(root);



    }

    private void wireActions() {
        calcBtn.addActionListener(e -> onCalculate());
        setGoalBtn.addActionListener(e -> setAsGoal());
    }

    private void onCalculate() {
        try {
            int age = Integer.parseInt(ageField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());
            double height = Double.parseDouble(heightField.getText().trim());

            boolean metric = metricBtn.isSelected();
            UserMetrics.Sex sex = (sexField.getSelectedIndex() == 0)
                    ? UserMetrics.Sex.MALE : UserMetrics.Sex.FEMALE;

            ActivityLevel level = switch (activityLevelField.getSelectedIndex()) {
                case 0 -> ActivityLevel.VERY_LIGHT;
                case 1 -> ActivityLevel.LIGHT;
                case 2 -> ActivityLevel.MEDIUM;
                case 3 -> ActivityLevel.HIGH;
                default -> ActivityLevel.EXTREME;
            };

            interactor.execute(new CalculateTDEEInputData(
                    age, weight, height, metric, sex, level
            ));
        } catch (NumberFormatException ex) {
            //presentValidationError("please enter valid nums.");
        }
    }

    private void setAsGoal() {
    }


}
