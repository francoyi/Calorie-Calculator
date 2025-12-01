package com.caloriecalc.infrastructure.repo;

import com.caloriecalc.entity.DailyLog;
import com.caloriecalc.usecase.foodlog.FoodLogRepository;
import com.fasterxml.jackson.core.type.TypeReference;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class JsonFoodLogRepository extends AbstractJsonRepository<JsonFoodLogRepository.Root>
        implements FoodLogRepository {
    public JsonFoodLogRepository(Path file) {
        super(file);
    }

    public static class Root {
        public List<DailyLog> days = new ArrayList<>();
    }

    private Root load() {
        return loadOrDefault(new Root(), new TypeReference<Root>() {
        }.getType());
    }

    @Override
    public synchronized DailyLog getDay(LocalDate date) {
        Root r = load();
        for (DailyLog d : r.days) if (date.equals(d.getDate())) return d;
        return null;
    }

    @Override
    public synchronized void saveDay(DailyLog day) {
        Root r = load();
        boolean replaced = false;
        for (int i = 0; i < r.days.size(); i++) {
            if (day.getDate().equals(r.days.get(i).getDate())) {
                r.days.set(i, day);
                replaced = true;
                break;
            }
        }
        if (!replaced) r.days.add(day);
        atomicWrite(r);
    }

    @Override
    public synchronized java.util.List<DailyLog> getAllDays() {
        return load().days;
    }
}
