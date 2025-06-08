package dao;

import model.Transaction;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    // Menambahkan transaksi
    public void addTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (customer_name, shoe_type, wash_price, date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transaction.getCustomerName());
            stmt.setString(2, transaction.getShoeType());
            stmt.setDouble(3, transaction.getWashPrice());
            stmt.setTimestamp(4, transaction.getDate());  // Menyertakan waktu saat transaksi
            stmt.setString(5, transaction.getStatus());  // Menyertakan status transaksi
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mengambil semua transaksi
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                transactions.add(new Transaction(rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("shoe_type"),
                        rs.getDouble("wash_price"),
                        rs.getTimestamp("date"),
                        rs.getString("status"))); // Menambahkan status
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Mengupdate status transaksi
    public boolean updateTransactionStatus(int id, String newStatus) {
        String query = "UPDATE transactions SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newStatus);  // Set status baru
            stmt.setInt(2, id);  // Set ID transaksi
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;  // Jika ada baris yang diupdate, return true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Menghapus transaksi
    public boolean deleteTransaction(int id) {
        String query = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
