package com.caloriecalc.port;
import com.caloriecalc.model.DailyLog;
import java.time.LocalDate;
import java.util.List;
public interface FoodLogRepository {
  DailyLog getDay(LocalDate date);
  void saveDay(DailyLog day);
  List<DailyLog> getAllDays();
}
