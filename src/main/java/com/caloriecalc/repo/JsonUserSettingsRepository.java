package com.caloriecalc.repo;

import com.caloriecalc.model.UserSettings;
import com.caloriecalc.port.UserSettingsRepository;
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