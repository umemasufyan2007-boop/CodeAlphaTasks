// ============================================================
//  Stock.java  –  Represents a single stock in the market
// ============================================================
public class Stock {

    private String symbol;      // e.g. "AAPL"
    private String companyName; // e.g. "Apple Inc."
    private double price;       
    private double initialPrice;

    public Stock(String symbol, String companyName, double price) {
        this.symbol       = symbol;
        this.companyName  = companyName;
        this.price        = price;
        this.initialPrice = price;
    }

    public void fluctuatePrice() {
        double changePercent = (Math.random() * 10) - 5; 
        price = price * (1 + changePercent / 100);
        price = Math.round(price * 100.0) / 100.0;       
    }

    public String getSymbol()      { return symbol; }
    public String getCompanyName() { return companyName; }
    public double getPrice()       { return price; }
    public double getInitialPrice(){ return initialPrice; }

    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        double change = ((price - initialPrice) / initialPrice) * 100;
        String arrow  = change >= 0 ? "UP" : "DOWN";
        return String.format("%-6s | %-20s | $%-8.2f | %s %.2f%%",
                symbol, companyName, price, arrow, Math.abs(change));
    }
}
