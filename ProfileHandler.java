package src;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class ProfileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String html = "<!DOCTYPE html><html><head><style>" +
                      "body { font-family: 'Segoe UI', Tahoma, sans-serif; background: #fcf9f2; color: #2c3e50; padding: 40px; text-align: center; }" +
                      "h1 { color: #5a4a42; }" +
                      ".grid { display: flex; justify-content: center; gap: 20px; margin-top: 30px; }" +
                      ".profile-card { background: #fff; border-radius: 16px; padding: 30px; width: 220px; box-shadow: 0 6px 15px rgba(0,0,0,0.06); cursor: pointer; border-top: 6px solid #d4a373; text-decoration: none; color: #333; transition: transform 0.2s; }" +
                      ".profile-card:hover { transform: translateY(-5px); }" +
                      "</style></head><body>" +
                      "<h1>Whom would you like to see right now?</h1>" +
                      "<div class='grid'>" +
                      "<a href='/vault?profile=me' class='profile-card'><h3>Me</h3><p>Personal timeline & milestones</p></a>" +
"                      <a href='/vault?profile=wife' class='profile-card'><h3>Wife</h3><p>Shared history & cherished moments</p></a>" +
"                      <a href='/vault?profile=daughter' class='profile-card'><h3>Daughter</h3><p>Fun anecdotes & memories</p></a>" +
                      "</div>" +
                      "</body></html>";

        exchange.sendResponseHeaders(200, html.getBytes("UTF-8").length);
        OutputStream os = exchange.getResponseBody();
        os.write(html.getBytes("UTF-8"));
        os.close();
    }
}