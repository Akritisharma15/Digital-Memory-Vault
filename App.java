package src;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws IOException {
        int port = 4567;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Map routes to their respective handlers
        server.createContext("/", new HomeHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/profiles", new ProfileHandler());
        server.createContext("/vault", new VaultHandler());
        
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Memory Vault web server started at http://localhost:" + port);
    }
}