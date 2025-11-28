package Test.usecase.ApiTestAndDeleteMeal;

import com.caloriecalc.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class FoodApiTest {

    /**
     * Test 1:
     * Directly access the OpenFoodFacts search interface
     * Confirm that HTTP 200 can be obtained and the body is non-empty JSON
     * There is an array field called "products" in JSON
     */
    @Test
    public void openFoodFactsSearch_returnsJsonWithProductsArray() throws Exception {

        String term = "cake";

        String encoded = URLEncoder.encode(term, StandardCharsets.UTF_8);
        String url = String.format(
                "https://world.openfoodfacts.org/cgi/search.pl" +
                        "?search_terms=%s" +
                        "&search_simple=1" +
                        "&action=process" +
                        "&json=1" +
                        "&page_size=5" +
                        "&fields=product_name,nutriments",
                encoded
        );

        // create HttpClient
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // build request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // send request
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // basic assertions
        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
        assertFalse(response.body().isBlank());

        // parse JSON
        JsonNode root = JsonUtil.mapper().readTree(response.body());
        assertTrue(root.has("products"));

        JsonNode products = root.get("products");
        assertTrue(products.isArray());
        assertTrue(products.size() > 0);
    }

    /**
     * Test 2：
     * The verification can obtain non-empty nutrient values and an energy value greater than 0
     *
     */
    @Test
    public void openFoodFactsClient_fetchNutritionPer100_returnsNonNullValues() {
        // create client
        com.caloriecalc.service.OpenFoodFactsClient client =
                new com.caloriecalc.service.OpenFoodFactsClient();

        String term = "cake";

        com.caloriecalc.model.NutritionValues values =
                client.fetchNutritionPer100(term);

        assertNotNull(values); //should return non-empty nutrient values.

        assertNotNull(values.energyKcal()); // energyKcal should not null
        assertTrue(values.energyKcal() > 0); //energyKcal should > 0
    }
}
