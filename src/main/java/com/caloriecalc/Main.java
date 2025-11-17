package com.caloriecalc;

import com.caloriecalc.port.FoodLogRepository;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.port.UserSettingsRepository;
import com.caloriecalc.repo.JsonFoodLogRepository;
import com.caloriecalc.repo.JsonUserSettingsRepository;
import com.caloriecalc.service.FoodLogService;
import com.caloriecalc.service.OpenFoodFactsClient;
import com.caloriecalc.ui.MainFrame;
import com.caloriecalc.ui.MainPanel;
import com.caloriecalc.ui.GraphPanel;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.Window;
import java.nio.file.Path;

public class Main {

    private static boolean isDarkMode = true;

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            applyTheme();

            FoodLogRepository foodLogRepo = new JsonFoodLogRepository(Path.of("data", "food_log.json"));
            UserSettingsRepository settingsRepo = new JsonUserSettingsRepository(Path.of("data", "user_settings.json"));
            NutritionDataProvider provider = new OpenFoodFactsClient();
            FoodLogService service = new FoodLogService(foodLogRepo, provider, settingsRepo);

            MainPanel mainPanel = new MainPanel(service);
            GraphPanel graphPanel = new GraphPanel(service);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Dashboard", mainPanel);
            tabbedPane.addTab("Progress", graphPanel);

            MainFrame mainFrame = new MainFrame(tabbedPane);

            mainFrame.setVisible(true);
        });
    }

    public static void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme();
        updateAllFrames();
    }

    private static void applyTheme() {
        try {
            if (isDarkMode) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
    }

    private static void updateAllFrames() {
        for (Window window : Window.getWindows()) {
            SwingUtilities.updateComponentTreeUI(window);
        }
    }
}