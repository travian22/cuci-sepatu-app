package dao;

import model.Transaction;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public void addTransaction(Transaction transaction) {
        String query = "INSERT INTO transactions (customer_name, shoe_type, wash_price) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transaction.getCustomerName());
            stmt.setString(2, transaction.getShoeType());
            stmt.setDouble(3, transaction.getWashPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
                        rs.getDouble("wash_price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

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
