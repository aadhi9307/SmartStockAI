package com.smartstock.ai;

import com.smartstock.model.Product;
import com.smartstock.model.Sale;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class InventoryAIService {

    private static final String API_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private static final String MODEL =
            "openai/gpt-oss-120b";

    private final HttpClient httpClient;

    public InventoryAIService() {

        httpClient =
                HttpClient.newBuilder()
                        .connectTimeout(
                                Duration.ofSeconds(20)
                        )
                        .build();
    }

    public String generateInsights(
            List<Product> products,
            List<Sale> sales
    ) throws IOException, InterruptedException {

        String apiKey =
                System.getenv("GROQ_API_KEY");

        if (
                apiKey == null
                        ||
                        apiKey.isBlank()
        ) {
            throw new IllegalStateException(
                    "GROQ_API_KEY is not configured."
            );
        }

        String requestBody =
                buildRequestBody(
                        products,
                        sales
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(API_URL)
                        )
                        .timeout(
                                Duration.ofSeconds(60)
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .header(
                                "Authorization",
                                "Bearer " + apiKey
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(
                                        requestBody
                                )
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (
                response.statusCode() < 200
                        ||
                        response.statusCode() >= 300
        ) {
            throw new IOException(
                    "Groq API error "
                            + response.statusCode()
                            + ": "
                            + extractErrorMessage(
                            response.body()
                    )
            );
        }

        String result =
                extractContent(
                        response.body()
                );

        if (
                result == null
                        ||
                        result.isBlank()
        ) {
            throw new IOException(
                    "The AI returned an empty response."
            );
        }

        return result.trim();
    }

    private String buildRequestBody(
            List<Product> products,
            List<Sale> sales
    ) {

        String systemPrompt = """
                You are SmartStock AI, an inventory management
                intelligence system.

                Analyze ONLY the factual information supplied
                in the user message.

                DATA INTEGRITY RULES:

                1. Never invent facts.

                2. Product data contains:
                   - product name
                   - category
                   - unit price
                   - current stock
                   - reorder level
                   - stock status

                3. Sales data contains:
                   - sale ID
                   - sale date
                   - total sale amount

                4. The supplied sales data does NOT contain
                   product-level sale quantities or product-to-sale
                   relationships.

                5. Therefore NEVER state or imply:
                   - units sold for a specific product
                   - product demand
                   - sales velocity
                   - fastest-selling product
                   - slowest-selling product
                   - product revenue
                   - product sales frequency
                   - product-specific sales trends

                6. Do not infer customer behavior.

                7. Do not infer promotions, campaigns, external
                   events, causes or business reasons.

                8. Do not describe sales as "today", "recent",
                   "rapid", "frequent", "within a short period",
                   or similar relative descriptions.

                9. Do not infer stock-out probability from demand
                   because product-level demand data is unavailable.

                10. For stock risks, report only the factual
                    relationship between current stock and reorder
                    level.

                11. You may calculate:
                    - total number of sales
                    - total revenue
                    - average sale value
                    - number of low-stock products
                    - number of out-of-stock products
                    - inventory value

                12. Recommendations must be clearly presented as
                    recommendations, not as established facts.

                13. If information is unavailable, explicitly say
                    that the available data is insufficient.

                REQUIRED RESPONSE STRUCTURE:

                EXECUTIVE SUMMARY

                Give 2-4 concise factual observations.

                STOCK RISKS

                List products that are low stock or out of stock.
                Include current stock and reorder level.

                SALES ANALYSIS

                Analyze only the supplied sale count,
                sale dates and monetary totals.

                Do not connect sales to individual products.

                RECOMMENDED ACTIONS

                Give practical actions supported by the available
                inventory information.

                NEXT CHECK

                State what the inventory manager should check next.

                STYLE:

                - Use the exact headings above.
                - Use "-" for bullet points.
                - No Markdown tables.
                - No Markdown bold.
                - No "---".
                - No emojis.
                - No unsupported assumptions.
                - No relative time descriptions.
                - Keep the report concise.
                - Use Indian Rupee formatting such as ₹14,589.98.
                """;

        String userPrompt =
                buildDataPrompt(
                        products,
                        sales
                );

        return "{"
                + "\"model\":\""
                + MODEL
                + "\","
                + "\"temperature\":0.1,"
                + "\"max_tokens\":900,"
                + "\"messages\":["
                + "{"
                + "\"role\":\"system\","
                + "\"content\":\""
                + escapeJson(
                systemPrompt
        )
                + "\""
                + "},"
                + "{"
                + "\"role\":\"user\","
                + "\"content\":\""
                + escapeJson(
                userPrompt
        )
                + "\""
                + "}"
                + "]"
                + "}";
    }

    private String buildDataPrompt(
            List<Product> products,
            List<Sale> sales
    ) {

        StringBuilder prompt =
                new StringBuilder();

        prompt.append(
                "SMARTSTOCK CURRENT DATABASE DATA\n\n"
        );

        prompt.append(
                "PRODUCT INVENTORY\n"
        );

        if (
                products == null
                        ||
                        products.isEmpty()
        ) {

            prompt.append(
                    "No products are currently recorded.\n\n"
            );

        } else {

            for (Product product : products) {

                if (product == null) {
                    continue;
                }

                prompt.append(
                        "Product Name: "
                                + safe(
                                product.getProductName()
                        )
                                + "\n"
                );

                prompt.append(
                        "Category: "
                                + safe(
                                product.getCategory()
                        )
                                + "\n"
                );

                prompt.append(
                        "Unit Price: ₹"
                                + formatDecimal(
                                product.getPrice()
                        )
                                + "\n"
                );

                prompt.append(
                        "Current Stock: "
                                + product.getQuantity()
                                + "\n"
                );

                prompt.append(
                        "Reorder Level: "
                                + product.getReorderLevel()
                                + "\n"
                );

                prompt.append(
                        "Stock Status: "
                                + getStockStatus(
                                product
                        )
                                + "\n\n"
                );
            }
        }

        prompt.append(
                "SALES RECORDS\n"
        );

        if (
                sales == null
                        ||
                        sales.isEmpty()
        ) {

            prompt.append(
                    "No sales are currently recorded.\n\n"
            );

        } else {

            BigDecimal totalRevenue =
                    BigDecimal.ZERO;

            int validSales =
                    0;

            for (Sale sale : sales) {

                if (sale == null) {
                    continue;
                }

                prompt.append(
                        "Sale ID: "
                                + sale.getSaleId()
                                + "\n"
                );

                prompt.append(
                        "Sale Date: "
                                + String.valueOf(
                                sale.getSaleDate()
                        )
                                + "\n"
                );

                prompt.append(
                        "Sale Total: ₹"
                                + formatDecimal(
                                sale.getTotalAmount()
                        )
                                + "\n\n"
                );

                if (
                        sale.getTotalAmount() != null
                ) {

                    totalRevenue =
                            totalRevenue.add(
                                    sale.getTotalAmount()
                            );

                    validSales++;
                }
            }

            BigDecimal averageSale =
                    validSales == 0
                            ? BigDecimal.ZERO
                            : totalRevenue.divide(
                            BigDecimal.valueOf(
                                    validSales
                            ),
                            2,
                            RoundingMode.HALF_UP
                    );

            prompt.append(
                    "CALCULATED SALES TOTALS\n"
            );

            prompt.append(
                    "Number of Sales: "
                            + validSales
                            + "\n"
            );

            prompt.append(
                    "Total Revenue: ₹"
                            + formatDecimal(
                            totalRevenue
                    )
                            + "\n"
            );

            prompt.append(
                    "Average Sale Value: ₹"
                            + formatDecimal(
                            averageSale
                    )
                            + "\n\n"
            );
        }

        prompt.append(
                "IMPORTANT DATA LIMITATION:\n"
                        + "Sales records supplied above do not contain "
                        + "product-level sale item information. "
                        + "Do not attribute any sale, revenue or demand "
                        + "to an individual product."
                        + "\n\n"
        );

        prompt.append(
                "Generate the SmartStock AI analysis using only "
                        + "the supplied data."
        );

        return prompt.toString();
    }

    private String getStockStatus(
            Product product
    ) {

        if (product.isOutOfStock()) {
            return "OUT OF STOCK";
        }

        if (product.isLowStock()) {
            return "LOW STOCK";
        }

        return "AVAILABLE";
    }

    private String formatDecimal(
            BigDecimal value
    ) {

        if (value == null) {
            return "0.00";
        }

        return value.setScale(
                2,
                RoundingMode.HALF_UP
        ).toPlainString();
    }

    private String safe(
            String value
    ) {

        return value == null
                ? "N/A"
                : value;
    }

    private String escapeJson(
            String value
    ) {

        StringBuilder result =
                new StringBuilder();

        for (
                int i = 0;
                i < value.length();
                i++
        ) {

            char c =
                    value.charAt(i);

            switch (c) {

                case '\\' ->
                        result.append("\\\\");

                case '"' ->
                        result.append("\\\"");

                case '\n' ->
                        result.append("\\n");

                case '\r' ->
                        result.append("\\r");

                case '\t' ->
                        result.append("\\t");

                case '\b' ->
                        result.append("\\b");

                case '\f' ->
                        result.append("\\f");

                default -> {

                    if (c < 32) {

                        result.append(
                                String.format(
                                        "\\u%04x",
                                        (int) c
                                )
                        );

                    } else {

                        result.append(c);
                    }
                }
            }
        }

        return result.toString();
    }

    private String extractContent(
            String json
    ) {

        String key =
                "\"content\"";

        int keyIndex =
                json.indexOf(key);

        if (keyIndex < 0) {
            return null;
        }

        int colonIndex =
                json.indexOf(
                        ':',
                        keyIndex + key.length()
                );

        if (colonIndex < 0) {
            return null;
        }

        int start =
                colonIndex + 1;

        while (
                start < json.length()
                        &&
                        Character.isWhitespace(
                                json.charAt(start)
                        )
        ) {

            start++;
        }

        if (
                start >= json.length()
                        ||
                        json.charAt(start) != '"'
        ) {
            return null;
        }

        start++;

        StringBuilder result =
                new StringBuilder();

        boolean escaped =
                false;

        for (
                int i = start;
                i < json.length();
                i++
        ) {

            char c =
                    json.charAt(i);

            if (escaped) {

                switch (c) {

                    case '"' ->
                            result.append('"');

                    case '\\' ->
                            result.append('\\');

                    case '/' ->
                            result.append('/');

                    case 'n' ->
                            result.append('\n');

                    case 'r' ->
                            result.append('\r');

                    case 't' ->
                            result.append('\t');

                    case 'b' ->
                            result.append('\b');

                    case 'f' ->
                            result.append('\f');

                    default ->
                            result.append(c);
                }

                escaped = false;

                continue;
            }

            if (c == '\\') {

                escaped = true;

                continue;
            }

            if (c == '"') {

                return result.toString();
            }

            result.append(c);
        }

        return null;
    }

    private String extractErrorMessage(
            String json
    ) {

        if (
                json == null
                        ||
                        json.isBlank()
        ) {
            return "Unknown API error.";
        }

        int messageIndex =
                json.indexOf(
                        "\"message\""
                );

        if (messageIndex >= 0) {

            int colon =
                    json.indexOf(
                            ':',
                            messageIndex
                    );

            int firstQuote =
                    json.indexOf(
                            '"',
                            colon + 1
                    );

            int secondQuote =
                    json.indexOf(
                            '"',
                            firstQuote + 1
                    );

            if (
                    firstQuote >= 0
                            &&
                            secondQuote > firstQuote
            ) {

                return json.substring(
                        firstQuote + 1,
                        secondQuote
                );
            }
        }

        return json;
    }
}