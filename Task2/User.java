// ============================================================
//  User.java  –  Represents a trader / account holder
// ============================================================
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class User {

    private String    username;
    private double    cashBalance;
    private Portfolio portfolio;
    private List<Transaction> transactionHistory;

    // Constructor
    public User(String username, double initialBalance) {
        this.username           = username;
        this.cashBalance        = initialBalance;
        this.portfolio          = new Portfolio();
        this.transactionHistory = new ArrayList<>();
    }

    // ── BUY ────────────────────────────────────────────────
    public boolean buyStock(Stock stock, int quantity) {
        double cost = stock.getPrice() * quantity;
        if (cost > cashBalance) {
            System.out.printf("  ERR Insufficient funds. Need $%.2f, have $%.2f%n",
                    cost, cashBalance);
            return false;
        }
        cashBalance -= cost;
        portfolio.addShares(stock.getSymbol(), quantity);

        Transaction t = new Transaction(
                Transaction.Type.BUY, stock.getSymbol(), quantity, stock.getPrice());
        transactionHistory.add(t);

        System.out.printf("  OK Bought %d share(s) of %s @ $%.2f  |  Remaining cash: $%.2f%n",
                quantity, stock.getSymbol(), stock.getPrice(), cashBalance);
        return true;
    }

    // ── SELL ───────────────────────────────────────────────
    public boolean sellStock(Stock stock, int quantity) {
        if (!portfolio.removeShares(stock.getSymbol(), quantity)) {
            System.out.printf("  ERR You only own %d share(s) of %s.%n",
                    portfolio.getShares(stock.getSymbol()), stock.getSymbol());
            return false;
        }
        double earned = stock.getPrice() * quantity;
        cashBalance += earned;

        Transaction t = new Transaction(
                Transaction.Type.SELL, stock.getSymbol(), quantity, stock.getPrice());
        transactionHistory.add(t);

        System.out.printf("  OK Sold %d share(s) of %s @ $%.2f  |  New cash: $%.2f%n",
                quantity, stock.getSymbol(), stock.getPrice(), cashBalance);
        return true;
    }

    // ── PORTFOLIO SUMMARY ──────────────────────────────────
    public void showPortfolioSummary(Map<String, Stock> market, double startingBalance) {
        double portfolioValue = portfolio.getTotalValue(market);
        double netWorth       = cashBalance + portfolioValue;
        double profitLoss     = netWorth - startingBalance;

        System.out.println("\n  -- Holdings --------------------------------------------");
        portfolio.display(market);
        System.out.println("  " + "-".repeat(42));
        System.out.printf("  Cash Balance   : $%.2f%n", cashBalance);
        System.out.printf("  Portfolio Value: $%.2f%n", portfolioValue);
        System.out.printf("  Net Worth      : $%.2f%n", netWorth);
        System.out.printf("  Profit / Loss  : %s$%.2f%n",
                profitLoss >= 0 ? "+" : "", profitLoss);
    }

    // ── TRANSACTION HISTORY ────────────────────────────────
    public void showTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("  No transactions yet.");
            return;
        }
        for (Transaction t : transactionHistory)
            System.out.println("  " + t);
    }

    // Getters / Setters
    public String    getUsername()    { return username; }
    public double    getCashBalance() { return cashBalance; }
    public Portfolio getPortfolio()   { return portfolio; }
    public List<Transaction> getTransactionHistory() { return transactionHistory; }
    public void setCashBalance(double b) { cashBalance = b; }
}
