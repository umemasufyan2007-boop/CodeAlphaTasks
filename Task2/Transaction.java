// ============================================================
//  Transaction.java  –  Records every buy / sell event
// ============================================================
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    public enum Type { BUY, SELL }

    private Type   type;
    private String stockSymbol;
    private int    quantity;
    private double pricePerShare;
    private double totalAmount;
    private String timestamp;

    // Constructor
    public Transaction(Type type, String stockSymbol,
                       int quantity, double pricePerShare) {
        this.type          = type;
        this.stockSymbol   = stockSymbol;
        this.quantity      = quantity;
        this.pricePerShare = pricePerShare;
        this.totalAmount   = quantity * pricePerShare;
        this.timestamp     = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Getters
    public Type   getType()          { return type; }
    public String getStockSymbol()   { return stockSymbol; }
    public int    getQuantity()      { return quantity; }
    public double getPricePerShare() { return pricePerShare; }
    public double getTotalAmount()   { return totalAmount; }
    public String getTimestamp()     { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %-4s | %s x%d @ $%.2f = $%.2f",
                timestamp,
                type == Type.BUY ? "BUY" : "SELL",
                stockSymbol, quantity, pricePerShare, totalAmount);
    }

    // CSV format for file persistence
    public String toCSV() {
        return type + "," + stockSymbol + "," + quantity + ","
                + pricePerShare + "," + totalAmount + "," + timestamp;
    }
}
