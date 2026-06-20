import java.io.*;
import java.util.Map;
public class FileManager {
    private static final String DATA_FILE = "portfolio_data.txt";
    // -- SAVE -----
    public static void savePortfolio(User user) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            // Line 1: username,cashBalance
            writer.println(user.getUsername() + "," + user.getCashBalance());
            // Next lines: HOLDING,symbol,quantity
            for (Map.Entry<String, Integer> entry :
                    user.getPortfolio().getHoldings().entrySet()) {
                writer.println("HOLDING," + entry.getKey() + "," + entry.getValue());
            }
            // Next lines: transaction CSV rows
            for (Transaction t : user.getTransactionHistory()) {
                writer.println("TXN," + t.toCSV());
            }
            System.out.println("  ✓ Portfolio saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("  ✗ Error saving: " + e.getMessage());
        }
    }
    // -- LOAD ----------
    public static User loadPortfolio() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // First line: username,cashBalance
            String firstLine = reader.readLine();
            if (firstLine == null) return null;

            String[] parts = firstLine.split(",");
            String username = parts[0];
            double cash     = Double.parseDouble(parts[1]);

            User user = new User(username, cash);
            user.setCashBalance(cash); // override default starting balance

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("HOLDING,")) {
                    String[] p = line.split(",");
                    user.getPortfolio().addShares(p[1], Integer.parseInt(p[2]));
                } else if (line.startsWith("TXN,"));
            }
            System.out.println("  OK Portfolio loaded for " + username);
            return user;
        } catch (IOException e) {
            System.out.println("  ERR Error loading: " + e.getMessage());
            return null;
        }
    }
}
