import java.util.LinkedHashMap;
import java.util.Map;
public class Market {
    private Map<String, Stock> stocks = new LinkedHashMap<>();
    // Constructor – pre-load some stocks
    public Market() {
        stocks.put("AAPL", new Stock("AAPL", "Apple Inc.",        178.50));
        stocks.put("GOOGL",new Stock("GOOGL","Alphabet Inc.",     135.20));
        stocks.put("MSFT", new Stock("MSFT", "Microsoft Corp.",   310.75));
        stocks.put("TSLA", new Stock("TSLA", "Tesla Inc.",        245.60));
        stocks.put("AMZN", new Stock("AMZN", "Amazon.com Inc.",   175.30));
        stocks.put("META", new Stock("META", "Meta Platforms",    320.40));
        stocks.put("NVDA", new Stock("NVDA", "NVIDIA Corp.",      430.00));
        stocks.put("NFLX", new Stock("NFLX", "Netflix Inc.",      450.80));
    }
    // Display all stocks
    public void displayMarket() {
        System.out.println("\n  +--------------------------------------------------+");
        System.out.println("  |              LIVE MARKET DATA                   |");
        System.out.println("  +--------------------------------------------------+");
        System.out.printf("  ║  %-6s | %-20s | %-8s | %s%n",
                "Symbol","Company","Price","Change");
        System.out.println("  +---------------------------------------------------+");
        for (Stock s : stocks.values())
            System.out.println("  |  " + s);
        System.out.println("  +------------------------------------------------------+");
    }
    // Fluctuate all stock prices (called after each trade or on refresh)
    public void updatePrices() {
        for (Stock s : stocks.values())
            s.fluctuatePrice();
    }
    public Stock getStock(String symbol) { return stocks.get(symbol.toUpperCase()); }
    public Map<String, Stock> getAllStocks() { return stocks; }
}
