package model;

public class Transaction {
    private int id;
    private String customerName;
    private String shoeType;
    private double washPrice;
    
    public Transaction(int id, String customerName, String shoeType, double washPrice) {
        this.id = id;
        this.customerName = customerName;
        this.shoeType = shoeType;
        this.washPrice = washPrice;
    }

    // Getters and Setters
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
}
