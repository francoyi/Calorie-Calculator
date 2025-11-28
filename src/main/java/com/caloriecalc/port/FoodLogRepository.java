package com.caloriecalc.port;

import com.caloriecalc.model.DailyLog;
import java.time.LocalDate;
import java.util.List;

import java.time.LocalDate;

public interface FoodLogRepository {
    DailyLog getDay(LocalDate date);

    void saveDay(DailyLog day);

    java.util.List<DailyLog> getAllDays();
}
