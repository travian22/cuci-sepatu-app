package model;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Admin extends User {

    public Admin(int id, String username, String password) {
        super(id, username, password);  // Memanggil konstruktor User untuk inisialisasi id, username, password
    }

    // Method untuk menghapus user Staff
    public void deleteUser(Staff staff) {
        System.out.println("Admin menghapus user: " + staff.getUsername());
        // Menghapus user dari database
        if (deleteUserFromDatabase(staff.getId())) {
            System.out.println("User dengan username " + staff.getUsername() + " berhasil dihapus.");
        } else {
            System.out.println("Gagal menghapus user dengan username " + staff.getUsername() + ".");
        }
    }

    // Method untuk menghapus user dari database
    private boolean deleteUserFromDatabase(int userId) {
        String query = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // Mengembalikan true jika ada baris yang terhapus
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
