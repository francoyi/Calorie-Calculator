package com.caloriecalc.usecase.usersettings;

import com.caloriecalc.entity.UserSettings;

public interface UserSettingsRepository {
    UserSettings getSettings();

    void saveSettings(UserSettings settings);
}
