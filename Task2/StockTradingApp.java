import java.util.Scanner;
public class StockTradingApp {
    private static final double STARTING_BALANCE = 10_000.00;
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        printBanner();
        // -- Setup user --------
        User user = null;
        System.out.print("  Do you want to load a saved portfolio? (y/n): ");
        String loadChoice = scanner.nextLine().trim().toLowerCase();
        if (loadChoice.equals("y")) {
            user = FileManager.loadPortfolio();
        }
        if (user == null) {
            System.out.print("  Enter your username: ");
            String name = scanner.nextLine().trim();
            user = new User(name, STARTING_BALANCE);
            System.out.printf("  Welcome, %s! Starting balance: $%.2f%n",
                    name, STARTING_BALANCE);
        }
        // -- Create market -----
        Market market = new Market();
        // -- Main menu loop -----
        boolean running = true;
        while (running) {
            printMenu(user);
            System.out.print("  Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1": // View market
                    market.updatePrices();
                    market.displayMarket();
                    break;

                case "2": // Buy stock
                    market.displayMarket();
                    System.out.print("\n  Enter stock symbol to BUY: ");
                    String buySymbol = scanner.nextLine().trim().toUpperCase();
                    Stock buyStock = market.getStock(buySymbol);
                    if (buyStock == null) {
                        System.out.println("  ERR Stock not found.");
                        break;
                    }
                    System.out.printf("  Current price of %s: $%.2f%n",
                            buySymbol, buyStock.getPrice());
                    System.out.print("  How many shares? ");
                    int buyQty = readInt();
                    if (buyQty > 0) user.buyStock(buyStock, buyQty);
                    break;

                case "3": // Sell stock
                    System.out.print("\n  Enter stock symbol to SELL: ");
                    String sellSymbol = scanner.nextLine().trim().toUpperCase();
                    Stock sellStock = market.getStock(sellSymbol);
                    if (sellStock == null) {
                        System.out.println("  ERR Stock not found.");
                        break;
                    }
                    System.out.printf("  Current price of %s: $%.2f | You own: %d shares%n",
                            sellSymbol, sellStock.getPrice(),
                            user.getPortfolio().getShares(sellSymbol));
                    System.out.print("  How many shares to sell? ");
                    int sellQty = readInt();
                    if (sellQty > 0) user.sellStock(sellStock, sellQty);
                    break;

                case "4": // View portfolio
                    System.out.println("\n  -- Portfolio: " + user.getUsername() + " --");
                    user.showPortfolioSummary(market.getAllStocks(), STARTING_BALANCE);
                    break;

                case "5": // Transaction history
                    System.out.println("\n  -- Transaction History --");
                    user.showTransactionHistory();
                    break;

                case "6": // Save portfolio
                    FileManager.savePortfolio(user);
                    break;

                case "7": // Exit
                    System.out.print("\n  Save before exiting? (y/n): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("y"))
                        FileManager.savePortfolio(user);
                    System.out.println("\n  Goodbye! Happy Trading! 📈");
                    running = false;
                    break;

                default:
                    System.out.println("  ERR Invalid option. Try again.");
            }
        }
        scanner.close();
    }

    // -- Helper: print menu ------
    private static void printMenu(User user) {
        System.out.println("\n  +-----------------------------+");
        System.out.printf ("  |  User: %-10s Cash: $%-6.0f |%n",
                user.getUsername(), user.getCashBalance());
        System.out.println("  |------------------------------|");
        System.out.println("  |  1. View Market              |");
        System.out.println("  |  2. Buy Stock                |");
        System.out.println("  |  3. Sell Stock               |");
        System.out.println("  |  4. View Portfolio           |");
        System.out.println("  |  5. Transaction History      |");
        System.out.println("  |  6. Save Portfolio           |");
        System.out.println("  |  7. Exit                     |");
        System.out.println("  +------------------------------+");
    }

    // -- Helper: print banner -------
    private static void printBanner() {
        System.out.println("+---------------------------------------+");
        System.out.println("|     JAVA STOCK TRADING PLATFORM       |");
        System.out.println("|     Simulated Market Environment      |");
        System.out.println("+---------------------------------------+");
    }

    // -- Helper: safe integer input ------
    private static int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  ERR Invalid number.");
            return 0;
        }
    }
}
