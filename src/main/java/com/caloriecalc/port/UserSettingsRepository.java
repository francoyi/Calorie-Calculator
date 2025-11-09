package com.caloriecalc.port;

import com.caloriecalc.model.UserSettings;

public interface UserSettingsRepository {
    UserSettings getSettings();
    void saveSettings(UserSettings settings);
}
