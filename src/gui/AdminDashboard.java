package gui;

import dao.TransactionDAO;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private JTextField tfCustomerName, tfSearch;
    private JComboBox<String> cbShoeType;
    private JTextField tfPrice;
    private JButton btnAdd, btnSearch, btnShowAll, btnDelete, btnLogout;
    private JTable table;
    private DefaultTableModel tableModel;

    private TransactionDAO transactionDAO;

    public AdminDashboard() {
        setTitle("Admin Dashboard - Cuci Sepatu");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        transactionDAO = new TransactionDAO();

        // Layout utama BorderLayout
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
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder("Kelola Transaksi"));

        tfSearch = new JTextField(10);
        btnSearch = new JButton("Cari");
        btnShowAll = new JButton("Tampilkan Semua");
        btnDelete = new JButton("Hapus Transaksi");
        btnLogout = new JButton("Logout");

        actionPanel.add(new JLabel("Nama Pelanggan:"));
        actionPanel.add(tfSearch);
        actionPanel.add(btnSearch);
        actionPanel.add(btnShowAll);
        actionPanel.add(btnDelete);
        actionPanel.add(btnLogout);

        topPanel.add(actionPanel);

        add(topPanel, BorderLayout.NORTH);

        // Tabel transaksi
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama Pelanggan", "Jenis Sepatu", "Harga", "Tanggal"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Transaksi"));

        add(scrollPane, BorderLayout.CENTER);

        // Event handlers
        cbShoeType.addActionListener(e -> updatePrice());
        btnAdd.addActionListener(e -> addTransaction());
        btnSearch.addActionListener(e -> searchTransaction());
        btnShowAll.addActionListener(e -> loadAllTransactions());
        btnDelete.addActionListener(e -> deleteSelectedTransaction());
        btnLogout.addActionListener(e -> logout());

        loadAllTransactions();
    }

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

    private void addTransaction() {
        String name = tfCustomerName.getText().trim();
        String shoeType = (String) cbShoeType.getSelectedItem();
        double price;

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama pelanggan wajib diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            price = Double.parseDouble(tfPrice.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Transaction transaction = new Transaction(0, name, shoeType, price);
        transactionDAO.addTransaction(transaction);

        JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan.");

        tfCustomerName.setText("");
        updatePrice();
        loadAllTransactions();
    }

    private void loadAllTransactions() {
        List<Transaction> list = transactionDAO.getAllTransactions();
        refreshTable(list);
    }

    private void searchTransaction() {
        String keyword = tfSearch.getText().trim().toLowerCase();
        List<Transaction> list = transactionDAO.getAllTransactions();

        List<Transaction> filtered = list.stream()
                .filter(t -> t.getCustomerName().toLowerCase().contains(keyword))
                .toList();

        refreshTable(filtered);
    }

    private void deleteSelectedTransaction() {
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

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }

    private void refreshTable(List<Transaction> transactions) {
        tableModel.setRowCount(0);

        for (Transaction t : transactions) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getCustomerName(),
                    t.getShoeType(),
                    t.getWashPrice(),
                    "-" // update jika ingin tampilkan tanggal
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
