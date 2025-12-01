package com.caloriecalc.infrastructure.repo;

import com.caloriecalc.entity.UserSettings;
import com.caloriecalc.usecase.usersettings.UserSettingsRepository;

import java.nio.file.Path;

public class JsonUserSettingsRepository extends AbstractJsonRepository<UserSettings> implements UserSettingsRepository {

    public JsonUserSettingsRepository(Path file) {
        super(file);
    }

    @Override
    public UserSettings getSettings() {
        return loadOrDefault(new UserSettings(), UserSettings.class);
    }

    @Override
    public void saveSettings(UserSettings settings) {
        atomicWrite(settings);
    }
}