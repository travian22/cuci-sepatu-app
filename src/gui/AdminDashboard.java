package gui;

import dao.TransactionDAO;
import model.Transaction;
import model.User;
import model.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.List;

public class AdminDashboard extends JFrame {
    private JTextField tfCustomerName, tfSearch;
    private JComboBox<String> cbShoeType;
    private JTextField tfPrice;
    private JButton btnAdd, btnSearch, btnShowAll, btnDelete, btnUpdateStatus, btnLogout;
    private JTable table;
    private DefaultTableModel tableModel;

    private TransactionDAO transactionDAO;
    private User currentUser; // Menyimpan user yang sedang login

    public AdminDashboard(User user) {
        this.currentUser = user; // Menyimpan user yang login
        transactionDAO = new TransactionDAO();

        setTitle("Admin Dashboard - Cuci Sepatu");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        // Panel atas: input + aksi
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input panel (GridBagLayout)
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Transaksi"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Nama Pelanggan:"), gbc);
        gbc.gridx = 1;
        tfCustomerName = new JTextField(20);
        inputPanel.add(tfCustomerName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Jenis Sepatu:"), gbc);
        gbc.gridx = 1;
        cbShoeType = new JComboBox<>(new String[]{"Sneakers", "Formal", "Boots", "Sandals"});
        cbShoeType.setPreferredSize(new Dimension(200, 25));
        inputPanel.add(cbShoeType, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Harga (Auto):"), gbc);
        gbc.gridx = 1;
        tfPrice = new JTextField(20);
        tfPrice.setEditable(false);
        inputPanel.add(tfPrice, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        btnAdd = new JButton("Tambah Transaksi");
        btnAdd.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(btnAdd, gbc);

        topPanel.add(inputPanel);

        // Action panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Kelola Transaksi"));

        tfSearch = new JTextField(10);
        btnSearch = new JButton("Cari");
        btnShowAll = new JButton("Tampilkan Semua");
        btnDelete = new JButton("Hapus Transaksi");
        btnUpdateStatus = new JButton("Ubah Status");
        btnLogout = new JButton("Logout");

        actionPanel.add(new JLabel("Nama Pelanggan:"));
        actionPanel.add(tfSearch);
        actionPanel.add(btnSearch);
        actionPanel.add(btnShowAll);
        actionPanel.add(btnDelete);
        actionPanel.add(btnUpdateStatus);
        actionPanel.add(btnLogout);

        topPanel.add(actionPanel);

        add(topPanel, BorderLayout.NORTH);

        // Tabel transaksi
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama Pelanggan", "Jenis Sepatu", "Harga", "Status", "Tanggal"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Transaksi"));
        add(scrollPane, BorderLayout.CENTER);

        // Event handlers
        cbShoeType.addActionListener(e -> updatePrice());
        btnAdd.addActionListener(this::addTransaction);
        btnSearch.addActionListener(this::searchTransaction);
        btnShowAll.addActionListener(e -> loadAllTransactions());
        btnDelete.addActionListener(this::deleteSelectedTransaction);
        btnUpdateStatus.addActionListener(this::updateTransactionStatus);
        btnLogout.addActionListener(this::logout);

        // Menyembunyikan tombol hapus jika bukan admin
        if (currentUser instanceof Admin) {
            btnDelete.setEnabled(true); // Menyembunyikan tombol hapus untuk admin
        } else {
            btnDelete.setEnabled(false); // Menyembunyikan tombol hapus untuk staff
        }

        loadAllTransactions();
    }

    // Method untuk menambah transaksi
    private void addTransaction(ActionEvent e) {
        String name = tfCustomerName.getText().trim();
        String shoeType = (String) cbShoeType.getSelectedItem();
        double price;

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama pelanggan wajib diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            price = Double.parseDouble(tfPrice.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); // Menyertakan waktu transaksi

        Transaction transaction = new Transaction(0, name, shoeType, price, timestamp, "Belum Lunas");
        transactionDAO.addTransaction(transaction);

        JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan.");

        tfCustomerName.setText("");
        updatePrice();
        loadAllTransactions();
    }

    // Method untuk memuat semua transaksi
    private void loadAllTransactions() {
        List<Transaction> list = transactionDAO.getAllTransactions();
        refreshTable(list);
    }

    // Method untuk mencari transaksi berdasarkan nama pelanggan
    private void searchTransaction(ActionEvent e) {
        String keyword = tfSearch.getText().trim().toLowerCase();
        List<Transaction> list = transactionDAO.getAllTransactions();

        List<Transaction> filtered = list.stream()
                .filter(t -> t.getCustomerName().toLowerCase().contains(keyword))
                .toList();

        refreshTable(filtered);
    }

    // Method untuk menghapus transaksi yang dipilih
    private void deleteSelectedTransaction(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin dihapus.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus transaksi ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int transactionId = (int) tableModel.getValueAt(selectedRow, 0);
        boolean success = transactionDAO.deleteTransaction(transactionId);

        if (success) {
            JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus.");
            loadAllTransactions();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menghapus transaksi.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method untuk mengubah status transaksi
    private void updateTransactionStatus(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang ingin diubah statusnya.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int transactionId = (int) tableModel.getValueAt(selectedRow, 0);
        String newStatus = "Lunas"; // Mengubah status menjadi Lunas

        boolean success = transactionDAO.updateTransactionStatus(transactionId, newStatus);

        if (success) {
            JOptionPane.showMessageDialog(this, "Status transaksi berhasil diubah.");
            loadAllTransactions();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah status transaksi.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method untuk menyegarkan tabel transaksi
    private void refreshTable(List<Transaction> transactions) {
        tableModel.setRowCount(0);
        for (Transaction t : transactions) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getCustomerName(),
                    t.getShoeType(),
                    t.getWashPrice(),
                    t.getStatus(),  // Menampilkan status transaksi
                    t.getDate()  // Tampilkan tanggal transaksi
            });
        }
    }

    // Method untuk logout
    private void logout(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }

    // Method untuk memperbarui harga berdasarkan jenis sepatu
    private void updatePrice() {
        String shoeType = (String) cbShoeType.getSelectedItem();
        double price = switch (shoeType) {
            case "Sneakers" -> 25000;
            case "Formal" -> 35000;
            case "Boots" -> 40000;
            case "Sandals" -> 15000;
            default -> 0;
        };
        tfPrice.setText(String.valueOf(price));
    }
}
