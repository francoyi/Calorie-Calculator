package com.caloriecalc;

import com.caloriecalc.entity.MealRecommenderFactory;
import com.caloriecalc.entity.RecommenderFactory;
import com.caloriecalc.infrastructure.repo.JsonFoodLogRepository;
import com.caloriecalc.infrastructure.repo.JsonUserMetricsRepository;
import com.caloriecalc.infrastructure.repo.JsonUserSettingsRepository;
import com.caloriecalc.usecase.mealrecommendation.DefaultMealRecommendationService;
import com.caloriecalc.usecase.foodcalorielookup.FoodLogService;
import com.caloriecalc.entity.MealRecommendationService;
import com.caloriecalc.infrastructure.datasource.OpenFoodFactsClient;
import com.caloriecalc.ui.MainFrame;
import com.caloriecalc.ui.MainPanel;
import com.caloriecalc.usecase.foodlog.FoodLogRepository;
import com.caloriecalc.usecase.myfoods.MyFoodRepository;
import com.caloriecalc.usecase.searchfood.NutritionDataProvider;
import com.caloriecalc.usecase.searchfood.NutritionDataProviderImpl;
import com.caloriecalc.usecase.tdee.UserMetricsRepository;
import com.caloriecalc.usecase.usersettings.UserSettingsRepository;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.Window;
import java.nio.file.Path;

import com.caloriecalc.usecase.myfoods.savetomyfood.InMemoryMyFoodRepository;

public class Main {

    private static boolean isDarkMode = true;

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            applyTheme();

            FoodLogRepository foodLogRepo = new JsonFoodLogRepository(Path.of("data", "food_log.json"));
            UserSettingsRepository settingsRepo = new JsonUserSettingsRepository(Path.of("data", "user_settings.json"));
            NutritionDataProvider provider = new OpenFoodFactsClient();
            MyFoodRepository myFoodRepo = new InMemoryMyFoodRepository();
            FoodLogService service = new FoodLogService(foodLogRepo, provider, settingsRepo,myFoodRepo);
            UserMetricsRepository metricsRepo =
                    new JsonUserMetricsRepository(Path.of("data", "user_metrics.json"));
            RecommenderFactory recommenderFactory = new MealRecommenderFactory(service);
            NutritionDataProviderImpl ndpi = new NutritionDataProviderImpl();
            MealRecommendationService mealRecommendationService = new DefaultMealRecommendationService(
                    ndpi,
                    recommenderFactory
            );

            MainPanel mainPanel = new MainPanel(service, mealRecommendationService, metricsRepo, myFoodRepo);
            MainFrame mainFrame = new MainFrame(mainPanel);

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