package src;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class HomeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String profile = "me"; // default
        if (query != null && query.contains("profile=")) {
            for (String param : query.split("&")) {
                if (param.startsWith("profile=")) {
                    profile = param.split("=")[1];
                }
            }
        }

        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            handlePost(exchange, profile);
            return;
        }

        StringBuilder cardsHtml = new StringBuilder();

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT title, description, created_at FROM memories WHERE relationship = ? ORDER BY id DESC")) {
            
            pstmt.setString(1, profile);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String date = rs.getString("created_at");

                cardsHtml.append("<div class='card' onclick='toggleDetails(this)'>")
                         .append("<h3>").append(title).append("</h3>")
                         .append("<span>Click to view memory details</span>")
                         .append("<div class='details'>")
                         .append("<p><b>Description:</b> ").append(description).append("</p>")
                         .append("<p><b>Saved on:</b> ").append(date).append("</p>")
                         .append("</div></div>");
            }
        } catch (SQLException e) {
            cardsHtml.append("<p style='color:red;'>Error loading memories from database.</p>");
        }

        String html = "<!DOCTYPE html><html><head><style>" +
                      "body { font-family: 'Segoe UI', Tahoma, sans-serif; background: #fcf9f2; color: #2c3e50; padding: 30px; text-align: center; }" +
                      "h1 { color: #5a4a42; text-transform: capitalize; margin-bottom: 5px; }" +
                      ".back-btn { display: inline-block; margin-bottom: 20px; color: #d4a373; text-decoration: none; font-weight: 600; }" +
                      ".form-container, .card { background: #ffffff; border-radius: 16px; padding: 24px; margin: 20px auto; max-width: 550px; box-shadow: 0 6px 15px rgba(0,0,0,0.06); text-align: left; }" +
                      ".form-container input, .form-container textarea { width: 100%; padding: 10px; margin: 8px 0 16px 0; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; }" +
                      ".form-container button { background: #d4a373; color: white; border: none; padding: 10px 20px; border-radius: 8px; cursor: pointer; font-size: 1em; }" +
                      ".card { cursor: pointer; border-left: 6px solid #d4a373; }" +
                      ".card h3 { margin: 0 0 8px 0; color: #333; }" +
                      ".card span { font-size: 0.9em; color: #888; }" +
                      ".details { display: none; margin-top: 15px; padding-top: 15px; border-top: 1px solid #f0f0f0; color: #555; line-height: 1.6; }" +
                      "</style></head><body>" +
                      "<a href='/profiles' class='back-btn'>&larr; Back to Profiles</a>" +
                      "<h1>Memory Vault: " + profile + "</h1>" +
                      
                      "<div class='form-container'>" +
                      "<h3>Add a New Memory</h3>" +
                      "<form method='POST' action='/vault?profile=" + profile + "'>" +
                      "<label>Title / Milestone:</label><input type='text' name='title' required>" +
                      "<label>Description / Memory details:</label><textarea name='description' rows='3' required></textarea>" +
                      "<button type='submit'>Save Memory</button>" +
                      "</form></div>" +

                      cardsHtml.toString() +
                      "<script>function toggleDetails(card) { var d = card.querySelector('.details'); d.style.display = d.style.display === 'block' ? 'none' : 'block'; }</script>" +
                      "</body></html>";

        exchange.sendResponseHeaders(200, html.getBytes("UTF-8").length);
        OutputStream os = exchange.getResponseBody();
        os.write(html.getBytes("UTF-8"));
        os.close();
    }

    private void handlePost(HttpExchange exchange, String profile) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        Map<String, String> params = parseFormData(body.toString());
        String title = params.get("title");
        String description = params.get("description");

        if (title != null && description != null) {
            MemoryService.addMemory(title, profile, description);
        }

        exchange.getResponseHeaders().set("Location", "/vault?profile=" + profile);
        exchange.sendResponseHeaders(303, -1);
    }

    private Map<String, String> parseFormData(String formData) {
        Map<String, String> map = new HashMap<>();
        for (String pair : formData.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                map.put(URLDecoder.decode(kv[0], StandardCharsets.UTF_8), URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            }
        }
        return map;
    }
}