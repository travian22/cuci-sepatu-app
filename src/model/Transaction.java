package model;

import java.sql.Timestamp;

public class Transaction {
    private int id;
    private String customerName;
    private String shoeType;
    private double washPrice;
    private Timestamp date;
    private String status; // Menambahkan status

    public Transaction(int id, String customerName, String shoeType, double washPrice, Timestamp date, String status) {
        this.id = id;
        this.customerName = customerName;
        this.shoeType = shoeType;
        this.washPrice = washPrice;
        this.date = date;
        this.status = status;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getShoeType() {
        return shoeType;
    }

    public void setShoeType(String shoeType) {
        this.shoeType = shoeType;
    }

    public double getWashPrice() {
        return washPrice;
    }

    public void setWashPrice(double washPrice) {
        this.washPrice = washPrice;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
