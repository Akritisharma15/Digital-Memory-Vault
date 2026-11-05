package src;

import java.sql.*;

public class MemoryService {
    public static void addMemory(String title, String profile, String description) {
        String sql = "INSERT INTO memories (title, relationship, description, created_at) VALUES (?, ?, ?, NOW())";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, profile); // stores 'me', 'wife', or 'daughter'
            pstmt.setString(3, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}