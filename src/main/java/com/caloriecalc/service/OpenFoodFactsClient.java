package com.caloriecalc.service;

import com.caloriecalc.model.NutritionValues;
import com.caloriecalc.port.NutritionDataProvider;
import com.caloriecalc.util.EnergyConverter;
import com.caloriecalc.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class OpenFoodFactsClient implements NutritionDataProvider {
    private static final String SEARCH_URL =
            "https://world.openfoodfacts.org/cgi/search.pl?search_terms=%s&search_simple=1&action=process&json=1&page_size=50&fields=product_name,nutriments";
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private static final long TTL_SECONDS = 6 * 3600;
    private final Map<String, CacheItem> cache = new HashMap<>();

    @Override
    public NutritionValues fetchNutritionPer100(String term) {
        String key = term.toLowerCase();
        CacheItem ci = cache.get(key);
        Instant now = Instant.now();
        
        if (ci != null && now.getEpochSecond() - ci.ts <= TTL_SECONDS) {
            return ci.val;
        }
        
        try {
            String url = String.format(SEARCH_URL, URLEncoder.encode(term, StandardCharsets.UTF_8));
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(20))
                    .header("User-Agent", "CalorieCalc/0.4 (desktop; Swing)").GET().build();
            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            
            if (resp.statusCode() != 200) throw new RuntimeException("OpenFoodFacts HTTP " + resp.statusCode());
            
            JsonNode root = JsonUtil.mapper().readTree(resp.body());
            JsonNode products = root.path("products");
            
            if (!products.isArray() || products.size() == 0) return null;
            for (JsonNode p : products) {
                JsonNode nutr = p.path("nutriments");
                
                if (nutr.isMissingNode()) continue;
                Double kcal = getD(nutr, "energy-kcal_100g");
                
                if (kcal == null) {
                    Double kj = getD(nutr, "energy-kj_100g");
                    if (kj != null) kcal = EnergyConverter.kjToKcal(kj);
                }
                
                if (kcal != null) {
                    Double protein = getD(nutr, "proteins_100g");
                    Double fat = getD(nutr, "fat_100g");
                    Double carb = getD(nutr, "carbohydrates_100g");
                    Double sugar = getD(nutr, "sugars_100g");
                    Double fiber = getD(nutr, "fiber_100g");
                    Double sodium = getD(nutr, "sodium_100g");
                    NutritionValues nv = new NutritionValues(kcal, protein, fat, carb, sugar, fiber, sodium);
                    
                    cache.put(key, new CacheItem(nv, now.getEpochSecond()));
                    return nv;
                }
            }
            return null;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("OpenFoodFacts request failed: " + e.getMessage(), e);
        }
    }

    private static Double getD(JsonNode n, String field) {
        return n.has(field) && !n.get(field).isNull() ? n.get(field).asDouble() : null;
    }

    private record CacheItem(NutritionValues val, long ts) {
    }
}
