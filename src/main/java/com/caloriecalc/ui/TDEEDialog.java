package com.caloriecalc.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

import com.caloriecalc.model.ActivityLevel;
import com.caloriecalc.model.UserMetrics;
import com.caloriecalc.port.tdee.*;
import com.caloriecalc.service.CalculateTDEEInteractor;
import com.caloriecalc.service.FoodLogService;
import com.caloriecalc.service.MifflinStJeorBMR;

public class TDEEDialog extends JDialog implements CalculateTDEEOutputBoundary {

    // “memory” of last user metrics
    private static UserMetrics lastMetrics = null;
    private static ActivityLevel lastActivityLevel = ActivityLevel.MEDIUM;
    private static boolean lastInputMetric = true; // true = metric, false = imperial

    // Input Fields
    private final JTextField ageField = new JTextField(5);
    private final JTextField weightField = new JTextField(5);
    private final JTextField heightField = new JTextField(5);
    private final JComboBox<String> sexField =
            new JComboBox<>(new String[]{"Male", "Female"});
    private final JComboBox<String> activityLevelField =
            new JComboBox<>(new String[]{"Very Light", "Light", "Medium", "High", "Extreme"});

    // Units
    private final JRadioButton metricBtn = new JRadioButton("Metric (kg, cm)", true);
    private final JRadioButton imperialBtn = new JRadioButton("Imperial (lb, in)");

    // Buttons
    private final JButton calcBtn = new JButton("Calculate");
    private final JButton setGoalBtn = new JButton("Set as Calorie Burn Goal");

    // Result box
    private final JTextArea resultArea = new JTextArea(5, 15);

    private final CalculateTDEEInputBoundary interactor;
    private final FoodLogService foodService;
    private final Runnable refreshCallback;

    private boolean metricSelected = true;

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

    public TDEEDialog(Window owner, FoodLogService foodService, Runnable refreshCallback) {
        super(owner, "Expected Calorie Burn Calculator (TDEE)", ModalityType.APPLICATION_MODAL);

        this.foodService = foodService;
        this.refreshCallback = refreshCallback;

        setSize(700, 500);

        this.interactor = new CalculateTDEEInteractor(new MifflinStJeorBMR(), this);

        makeUI();
        restoreLastInputs();
        wireActions();

        pack();
        setLocationRelativeTo(owner);
    }

    private void addRow(JPanel panel, GridBagConstraints c, int row,
                        String label, JComponent field) {

        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel(label), c);

        c.gridx = 1;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(field, c);
    }

    private void makeUI() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        ButtonGroup units = new ButtonGroup();
        units.add(metricBtn);
        units.add(imperialBtn);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);

        addRow(formPanel, c, 0, "Age:", ageField);
        addRow(formPanel, c, 1, "Weight:", weightField);
        addRow(formPanel, c, 2, "Height:", heightField);
        addRow(formPanel, c, 3, "Sex:", sexField);
        addRow(formPanel, c, 4, "Activity Level:", activityLevelField);

        JPanel unitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        unitPanel.add(metricBtn);
        unitPanel.add(imperialBtn);

        JPanel btns = new JPanel();
        btns.setLayout(new BoxLayout(btns, BoxLayout.X_AXIS));
        btns.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        btns.add(calcBtn);
        btns.add(Box.createHorizontalStrut(8));
        btns.add(setGoalBtn);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(formPanel, BorderLayout.NORTH);
        top.add(unitPanel, BorderLayout.SOUTH);

        JPanel root = new JPanel(new BorderLayout(5, 8));
        root.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        root.add(btns, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void wireActions() {
        calcBtn.addActionListener(e -> onCalculate());
        setGoalBtn.addActionListener(e -> onSetGoal());

        metricBtn.addActionListener(e -> {
            if (!metricSelected) {
                convertImperialToMetric();
                metricSelected = true;
                lastInputMetric = true;
            }
        });

        imperialBtn.addActionListener(e -> {
            if (metricSelected) {
                convertMetricToImperial();
                metricSelected = false;
                lastInputMetric = false;
            }
        });
    }

    private void convertMetricToImperial() {
        try {
            double weight = Double.parseDouble(weightField.getText().trim());
            double height = Double.parseDouble(heightField.getText().trim());
            double weightLb = weight / 0.453592;
            double heightIn = height / 2.54;
            weightField.setText(formatOneDecimal(weightLb));
            heightField.setText(formatOneDecimal(heightIn));
        } catch (NumberFormatException ignored) {
        }
    }

    private void convertImperialToMetric() {
        try {
            double weight = Double.parseDouble(weightField.getText().trim());
            double height = Double.parseDouble(heightField.getText().trim());
            double weightKg = weight * 0.453592;
            double heightCm = height * 2.54;
            weightField.setText(formatOneDecimal(weightKg));
            heightField.setText(formatOneDecimal(heightCm));
        } catch (NumberFormatException ignored) {
        }
    }

    private String formatOneDecimal(double value) {
        return String.format(Locale.US, "%.1f", value);
    }

    private void restoreLastInputs() {
        if (lastMetrics == null) {
            return;
        }

        ageField.setText(Integer.toString(lastMetrics.ageYears()));
        sexField.setSelectedIndex(
                lastMetrics.sex() == UserMetrics.Sex.MALE ? 0 : 1
        );
        activityLevelField.setSelectedIndex(toIndex(lastActivityLevel));

        if (lastInputMetric) {
            metricBtn.setSelected(true);
            metricSelected = true;
            weightField.setText(formatOneDecimal(lastMetrics.weightKg()));
            heightField.setText(formatOneDecimal(lastMetrics.heightCm()));
        } else {
            imperialBtn.setSelected(true);
            metricSelected = false;
            double weightLb = lastMetrics.weightKg() / 0.453592;
            double heightIn = lastMetrics.heightCm() / 2.54;
            weightField.setText(formatOneDecimal(weightLb));
            heightField.setText(formatOneDecimal(heightIn));
        }
    }

    private int toIndex(ActivityLevel lvl) {
        return switch (lvl) {
            case VERY_LIGHT -> 0;
            case LIGHT -> 1;
            case MEDIUM -> 2;
            case HIGH -> 3;
            case EXTREME -> 4;
        };
    }

    private ActivityLevel fromIndex(int idx) {
        return switch (idx) {
            case 0 -> ActivityLevel.VERY_LIGHT;
            case 1 -> ActivityLevel.LIGHT;
            case 2 -> ActivityLevel.MEDIUM;
            case 3 -> ActivityLevel.HIGH;
            default -> ActivityLevel.EXTREME;
        };
    }

    private void onCalculate() {
        try {
            int age = Integer.parseInt(ageField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());
            double height = Double.parseDouble(heightField.getText().trim());

            boolean metric = metricBtn.isSelected();
            UserMetrics.Sex sex = (sexField.getSelectedIndex() == 0)
                    ? UserMetrics.Sex.MALE : UserMetrics.Sex.FEMALE;

            ActivityLevel level = fromIndex(activityLevelField.getSelectedIndex());


            if (age >= 18 && weight > 0 && height > 0) {
                double weightKg = metric ? weight : weight * 0.453592;
                double heightCm = metric ? height : height * 2.54;
                try {
                    lastMetrics = new UserMetrics(age, weightKg, heightCm, sex);
                    lastActivityLevel = level;
                    lastInputMetric = metric;
                } catch (IllegalArgumentException ignored) {
                }
            }

            interactor.execute(new CalculateTDEEInputData(
                    age, weight, height, metric, sex, level
            ));
        } catch (NumberFormatException ex) {
            presentValidationError("Please enter valid numeric values for age, weight, and height.");
        }
    }

    // Use the last computed TDEE as the daily goal
    private void onSetGoal() {
        if (result == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please calculate your TDEE first.",
                    "No result",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int goal = (int) Math.round(result.tdee);
        foodService.setDailyGoal(goal);


        if (refreshCallback != null) {
            refreshCallback.run();
        }

        JOptionPane.showMessageDialog(
                this,
                "Daily calorie burn goal set to " + goal + " kcal.",
                "Goal Set",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void present(CalculateTDEEOutputData output) {
        SwingUtilities.invokeLater(() -> {
            result = new Result(output.bmr(), output.tdee(), output.formulaName());
            String text = """
                    BMR (%s): %.1f kcal/day
                    Activity factor: %.2f
                    Estimated TDEE: %.1f kcal/day
                    """.formatted(
                    output.formulaName(),
                    output.bmr(),
                    output.activityFactor(),
                    output.tdee()
            );
            resultArea.setText(text);
        });
    }

    @Override
    public void presentValidationError(String message) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                this,
                message,
                "Invalid input",
                JOptionPane.ERROR_MESSAGE
        ));
    }
}
