import java.util.HashMap;
import java.util.Map;
public class Portfolio {
    
    private Map<String, Integer> holdings = new HashMap<>();
    public void addShares(String symbol, int quantity) {
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + quantity);
    }
    public boolean removeShares(String symbol, int quantity) {
        int owned = holdings.getOrDefault(symbol, 0);
        if (owned < quantity) return false;
        if (owned == quantity)
            holdings.remove(symbol);
        else
            holdings.put(symbol, owned - quantity);
        return true;
    }

    public int getShares(String symbol) {
        return holdings.getOrDefault(symbol, 0);
    }
    public double getTotalValue(Map<String, Stock> market) {
        double total = 0;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            Stock s = market.get(entry.getKey());
            if (s != null) total += s.getPrice() * entry.getValue();
        }
        return total;
    }
    // Display all holdings
    public void display(Map<String, Stock> market) {
        if (holdings.isEmpty()) {
            System.out.println("  (No stocks owned yet)");
            return;
        }
        System.out.printf("  %-6s | %-6s | %-10s | %-10s%n",
                "Symbol", "Shares", "Curr Price", "Value");
        System.out.println("  " + "-".repeat(42));
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            Stock s = market.get(entry.getKey());
            if (s != null) {
                double value = s.getPrice() * entry.getValue();
                System.out.printf("  %-6s | %-6d | $%-9.2f | $%-9.2f%n",
                        entry.getKey(), entry.getValue(),
                        s.getPrice(), value);
            }
        }
    }
    // For file saving
    public Map<String, Integer> getHoldings() { return holdings; }
}
