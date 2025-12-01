package com.caloriecalc.usecase.foodlog;

import com.caloriecalc.entity.DailyLog;
import java.time.LocalDate;

public interface FoodLogRepository {
    DailyLog getDay(LocalDate date);

    void saveDay(DailyLog day);

    java.util.List<DailyLog> getAllDays();
}
