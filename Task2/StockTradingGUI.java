// ============================================================
//  StockTradingGUI.java  -  Full GUI Stock Trading Platform
//  Uses Java Swing - no extra libraries needed
// ============================================================
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class StockTradingGUI extends JFrame {

    // ── Constants ─────────────────────────────────────────
    private static final double STARTING_BALANCE = 10_000.00;

    // Colors
    private static final Color BG_DARK       = new Color(18, 24, 38);
    private static final Color BG_PANEL      = new Color(26, 34, 52);
    private static final Color BG_CARD       = new Color(34, 44, 66);
    private static final Color ACCENT_GREEN  = new Color(0, 200, 100);
    private static final Color ACCENT_RED    = new Color(220, 60, 60);
    private static final Color ACCENT_BLUE   = new Color(66, 133, 244);
    private static final Color TEXT_PRIMARY  = new Color(230, 235, 245);
    private static final Color TEXT_SECONDARY= new Color(140, 155, 185);
    private static final Color GOLD          = new Color(255, 200, 60);

    // ── State ─────────────────────────────────────────────
    private User   user;
    private Market market;

    // ── UI Components ─────────────────────────────────────
    private JLabel  lblCash, lblNetWorth, lblProfitLoss, lblUsername;
    private JTable  marketTable, portfolioTable, historyTable;
    private DefaultTableModel marketModel, portfolioModel, historyModel;
    private JTextField txtQty;
    private JComboBox<String> cmbSymbol;
    private JLabel  statusLabel;
    private JPanel  cardPanel;
    private CardLayout cardLayout;

    // ── Constructor ───────────────────────────────────────
    public StockTradingGUI() {
        market = new Market();
        showLoginDialog();
        if (user == null) System.exit(0);

        initUI();
        refreshAll();

        // Auto-refresh market prices every 5 seconds
        Timer timer = new Timer(5000, e -> {
            market.updatePrices();
            refreshMarketTable();
            refreshPortfolioTable();
            refreshStatsBar();
            setStatus("Market prices updated.");
        });
        timer.start();
    }

    // ── Login Dialog ──────────────────────────────────────
    private void showLoginDialog() {
        JDialog dialog = new JDialog((Frame) null, "Welcome", true);
        dialog.setSize(400, 280);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_DARK);
        panel.setBorder(BorderFactory.createLineBorder(ACCENT_BLUE, 2));

        // Title
        JLabel title = new JLabel("STOCK TRADING PLATFORM", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(GOLD);
        title.setBorder(new EmptyBorder(20, 10, 5, 10));
        panel.add(title, BorderLayout.NORTH);

        // Center form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_DARK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblName = new JLabel("Enter Username:");
        lblName.setForeground(TEXT_PRIMARY);
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTextField txtName = new JTextField(15);
        styleTextField(txtName);

        JCheckBox chkLoad = new JCheckBox("Load saved portfolio");
        chkLoad.setForeground(TEXT_SECONDARY);
        chkLoad.setBackground(BG_DARK);
        chkLoad.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(lblName, gbc);
        gbc.gridy = 1;
        form.add(txtName, gbc);
        gbc.gridy = 2;
        form.add(chkLoad, gbc);
        panel.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(BG_DARK);

        JButton btnStart = new JButton("Start Trading");
        styleButton(btnStart, ACCENT_GREEN);
        JButton btnExit = new JButton("Exit");
        styleButton(btnExit, ACCENT_RED);

        btnStart.addActionListener(e -> {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter a username.");
                return;
            }
            if (chkLoad.isSelected()) {
                user = FileManager.loadPortfolio();
                if (user == null)
                    JOptionPane.showMessageDialog(dialog, "No saved portfolio found. Starting fresh.");
            }
            if (user == null) user = new User(name, STARTING_BALANCE);
            dialog.dispose();
        });

        btnExit.addActionListener(e -> System.exit(0));

        // Allow Enter key
        txtName.addActionListener(e -> btnStart.doClick());

        btnPanel.add(btnStart);
        btnPanel.add(btnExit);
        panel.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // ── Main UI ───────────────────────────────────────────
    private void initUI() {
        setTitle("Java Stock Trading Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(0, 0));

        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildSideBar(),   BorderLayout.WEST);
        add(buildMainPanel(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ── Top Bar ───────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_PANEL);
        bar.setBorder(new EmptyBorder(10, 20, 10, 20));
        bar.setPreferredSize(new Dimension(0, 60));

        JLabel logo = new JLabel("  Stock Trading Platform");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(GOLD);

        lblUsername = new JLabel("User: " + user.getUsername());
        lblUsername.setForeground(TEXT_SECONDARY);
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        bar.add(logo, BorderLayout.WEST);
        bar.add(lblUsername, BorderLayout.EAST);
        return bar;
    }

    // ── Side Bar ──────────────────────────────────────────
    private JPanel buildSideBar() {
        JPanel side = new JPanel();
        side.setBackground(BG_PANEL);
        side.setPreferredSize(new Dimension(180, 0));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(20, 10, 20, 10));

        String[] labels = {"Market", "Portfolio", "History"};
        for (String label : labels) {
            JButton btn = new JButton(label);
            btn.setMaximumSize(new Dimension(160, 45));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(new Color(18, 30, 58));   // dark navy
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBorderPainted(true);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 1),
                new EmptyBorder(8, 15, 8, 15)));
            btn.setFocusPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> cardLayout.show(cardPanel, label));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(ACCENT_BLUE);
                    btn.setForeground(Color.WHITE);
                }
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(new Color(18, 30, 58));
                    btn.setForeground(Color.WHITE);
                }
            });
            side.add(btn);
            side.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        side.add(Box.createVerticalGlue());

        JButton btnSave = new JButton("Save");
        btnSave.setMaximumSize(new Dimension(160, 40));
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSave.setBackground(new Color(34, 160, 74));   // green
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSave.setOpaque(true);
        btnSave.setContentAreaFilled(true);
        btnSave.setBorderPainted(false);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.addActionListener(e -> {
            FileManager.savePortfolio(user);
            setStatus("Portfolio saved successfully.");
        });

        JButton btnExit = new JButton("Exit");
        btnExit.setMaximumSize(new Dimension(160, 40));
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExit.setBackground(new Color(200, 50, 50));   // red
        btnExit.setForeground(Color.WHITE);
        btnExit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExit.setOpaque(true);
        btnExit.setContentAreaFilled(true);
        btnExit.setBorderPainted(false);
        btnExit.setFocusPainted(false);
        btnExit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnExit.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(this,
                "Save before exiting?", "Exit", JOptionPane.YES_NO_CANCEL_OPTION);
            if (r == JOptionPane.YES_OPTION) FileManager.savePortfolio(user);
            if (r != JOptionPane.CANCEL_OPTION) System.exit(0);
        });

        side.add(btnSave);
        side.add(Box.createRigidArea(new Dimension(0, 8)));
        side.add(btnExit);
        return side;
    }

    // ── Main Panel (Cards) ────────────────────────────────
    private JPanel buildMainPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_DARK);

        // Stats bar at top
        wrapper.add(buildStatsBar(), BorderLayout.NORTH);

        // Card panel
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(BG_DARK);
        cardPanel.add(buildMarketCard(),    "Market");
        cardPanel.add(buildPortfolioCard(), "Portfolio");
        cardPanel.add(buildHistoryCard(),   "History");

        wrapper.add(cardPanel, BorderLayout.CENTER);
        return wrapper;
    }

    // ── Stats Bar ─────────────────────────────────────────
    private JPanel buildStatsBar() {
        JPanel bar = new JPanel(new GridLayout(1, 3, 10, 0));
        bar.setBackground(BG_DARK);
        bar.setBorder(new EmptyBorder(10, 15, 10, 15));

        lblCash       = statCard("Cash Balance", "$0.00", ACCENT_BLUE);
        lblNetWorth   = statCard("Net Worth",    "$0.00", GOLD);
        lblProfitLoss = statCard("Profit / Loss","$0.00", ACCENT_GREEN);

        bar.add(lblCash.getParent());
        bar.add(lblNetWorth.getParent());
        bar.add(lblProfitLoss.getParent());
        return bar;
    }

    private JLabel statCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1),
            new EmptyBorder(10, 15, 10, 15)));

        JLabel lTitle = new JLabel(title);
        lTitle.setForeground(TEXT_SECONDARY);
        lTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel lValue = new JLabel(value);
        lValue.setForeground(color);
        lValue.setFont(new Font("Segoe UI", Font.BOLD, 20));

        card.add(lTitle, BorderLayout.NORTH);
        card.add(lValue, BorderLayout.CENTER);
        return lValue;
    }

    // ── Market Card ───────────────────────────────────────
    private JPanel buildMarketCard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Table
        String[] cols = {"Symbol", "Company", "Price ($)", "Change %"};
        marketModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        marketTable = buildTable(marketModel);
        // Color positive/negative in change column
        marketTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                String val = v == null ? "" : v.toString();
                setForeground(val.startsWith("+") ? ACCENT_GREEN : ACCENT_RED);
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(marketTable);
        styleScrollPane(scroll);

        // Trade panel
        JPanel tradePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tradePanel.setBackground(BG_CARD);
        tradePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_BLUE, 1),
            new EmptyBorder(10, 15, 10, 15)));

        JLabel lblSym = new JLabel("Symbol:");
        lblSym.setForeground(Color.WHITE);
        lblSym.setFont(new Font("Segoe UI", Font.BOLD, 13));

        cmbSymbol = new JComboBox<>(market.getAllStocks().keySet().toArray(new String[0]));
        cmbSymbol.setBackground(Color.WHITE);
        cmbSymbol.setForeground(Color.BLACK);
        cmbSymbol.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cmbSymbol.setPreferredSize(new Dimension(100, 30));

        JLabel lblQty = new JLabel("Quantity:");
        lblQty.setForeground(Color.WHITE);
        lblQty.setFont(new Font("Segoe UI", Font.BOLD, 13));

        txtQty = new JTextField("1", 5);
        styleTextField(txtQty);

        // BUY - green background, white text
        JButton btnBuy = new JButton("BUY");
        btnBuy.setBackground(new Color(34, 160, 74));
        btnBuy.setForeground(Color.WHITE);
        btnBuy.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuy.setOpaque(true);
        btnBuy.setContentAreaFilled(true);
        btnBuy.setBorderPainted(false);
        btnBuy.setFocusPainted(false);
        btnBuy.setBorder(new EmptyBorder(8, 18, 8, 18));
        btnBuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // SELL - red background, white text
        JButton btnSell = new JButton("SELL");
        btnSell.setBackground(new Color(200, 50, 50));
        btnSell.setForeground(Color.WHITE);
        btnSell.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSell.setOpaque(true);
        btnSell.setContentAreaFilled(true);
        btnSell.setBorderPainted(false);
        btnSell.setFocusPainted(false);
        btnSell.setBorder(new EmptyBorder(8, 18, 8, 18));
        btnSell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Refresh - white background, blue text
        JButton btnRefresh = new JButton("Refresh Prices");
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.setForeground(ACCENT_BLUE);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.setOpaque(true);
        btnRefresh.setContentAreaFilled(true);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(new EmptyBorder(8, 18, 8, 18));
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnBuy.addActionListener(e -> executeTrade(true));
        btnSell.addActionListener(e -> executeTrade(false));
        btnRefresh.addActionListener(e -> {
            market.updatePrices();
            refreshMarketTable();
            refreshPortfolioTable();
            refreshStatsBar();
            setStatus("Prices refreshed.");
        });

        tradePanel.add(lblSym);
        tradePanel.add(cmbSymbol);
        tradePanel.add(lblQty);
        tradePanel.add(txtQty);
        tradePanel.add(btnBuy);
        tradePanel.add(btnSell);
        tradePanel.add(Box.createHorizontalStrut(20));
        tradePanel.add(btnRefresh);

        panel.add(scroll,      BorderLayout.CENTER);
        panel.add(tradePanel,  BorderLayout.SOUTH);
        return panel;
    }

    // ── Portfolio Card ────────────────────────────────────
    private JPanel buildPortfolioCard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        String[] cols = {"Symbol", "Shares Owned", "Current Price ($)", "Total Value ($)"};
        portfolioModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        portfolioTable = buildTable(portfolioModel);

        JScrollPane scroll = new JScrollPane(portfolioTable);
        styleScrollPane(scroll);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── History Card ──────────────────────────────────────
    private JPanel buildHistoryCard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        String[] cols = {"Time", "Type", "Symbol", "Qty", "Price ($)", "Total ($)"};
        historyModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        historyTable = buildTable(historyModel);
        // Color BUY/SELL column
        historyTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setForeground("BUY".equals(v) ? ACCENT_GREEN : ACCENT_RED);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(getFont().deriveFont(Font.BOLD));
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(historyTable);
        styleScrollPane(scroll);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Status Bar ────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_PANEL);
        bar.setBorder(new EmptyBorder(5, 15, 5, 15));

        statusLabel = new JLabel("Ready. Welcome, " + user.getUsername() + "!");
        statusLabel.setForeground(TEXT_SECONDARY);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bar.add(statusLabel, BorderLayout.WEST);

        JLabel hint = new JLabel("Prices auto-refresh every 5 seconds");
        hint.setForeground(new Color(80, 100, 130));
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        bar.add(hint, BorderLayout.EAST);
        return bar;
    }

    // ── Trade Logic ───────────────────────────────────────
    private void executeTrade(boolean isBuy) {
        String symbol = (String) cmbSymbol.getSelectedItem();
        int qty;
        try {
            qty = Integer.parseInt(txtQty.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            showError("Please enter a valid quantity (positive number).");
            return;
        }

        Stock stock = market.getStock(symbol);
        if (stock == null) { showError("Stock not found."); return; }

        boolean success = isBuy
                ? user.buyStock(stock, qty)
                : user.sellStock(stock, qty);

        if (success) {
            refreshAll();
            setStatus((isBuy ? "Bought " : "Sold ") + qty + " share(s) of "
                    + symbol + " @ $" + String.format("%.2f", stock.getPrice()));
        } else {
            setStatus(isBuy
                ? "Buy failed: insufficient funds."
                : "Sell failed: not enough shares.");
        }
    }

    // ── Refresh Methods ───────────────────────────────────
    private void refreshAll() {
        refreshStatsBar();
        refreshMarketTable();
        refreshPortfolioTable();
        refreshHistoryTable();
    }

    private void refreshStatsBar() {
        double portfolioVal = user.getPortfolio().getTotalValue(market.getAllStocks());
        double netWorth     = user.getCashBalance() + portfolioVal;
        double pl           = netWorth - STARTING_BALANCE;

        lblCash.setText(String.format("$%.2f", user.getCashBalance()));
        lblNetWorth.setText(String.format("$%.2f", netWorth));
        lblProfitLoss.setText(String.format("%s$%.2f", pl >= 0 ? "+" : "", pl));
        lblProfitLoss.setForeground(pl >= 0 ? ACCENT_GREEN : ACCENT_RED);
    }

    private void refreshMarketTable() {
        marketModel.setRowCount(0);
        for (Stock s : market.getAllStocks().values()) {
            double change = ((s.getPrice() - s.getInitialPrice()) / s.getInitialPrice()) * 100;
            marketModel.addRow(new Object[]{
                s.getSymbol(),
                s.getCompanyName(),
                String.format("%.2f", s.getPrice()),
                String.format("%s%.2f%%", change >= 0 ? "+" : "", change)
            });
        }
    }

    private void refreshPortfolioTable() {
        portfolioModel.setRowCount(0);
        Map<String, Integer> holdings = user.getPortfolio().getHoldings();
        for (Map.Entry<String, Integer> e : holdings.entrySet()) {
            Stock s = market.getStock(e.getKey());
            if (s != null) {
                portfolioModel.addRow(new Object[]{
                    e.getKey(),
                    e.getValue(),
                    String.format("%.2f", s.getPrice()),
                    String.format("%.2f", s.getPrice() * e.getValue())
                });
            }
        }
    }

    private void refreshHistoryTable() {
        historyModel.setRowCount(0);
        for (Transaction t : user.getTransactionHistory()) {
            historyModel.addRow(new Object[]{
                t.getTimestamp(),
                t.getType().toString(),
                t.getStockSymbol(),
                t.getQuantity(),
                String.format("%.2f", t.getPricePerShare()),
                String.format("%.2f", t.getTotalAmount())
            });
        }
    }

    // ── UI Helpers ────────────────────────────────────────
    private JTable buildTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setGridColor(new Color(50, 65, 95));
        table.setSelectionBackground(new Color(66, 133, 244, 80));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(BG_PANEL);
        header.setForeground(ACCENT_BLUE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBorder(BorderFactory.createLineBorder(BG_DARK));

        // Center all columns
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < model.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(center);

        return table;
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(new Color(10, 30, 80));   // dark blue text
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void styleTextField(JTextField tf) {
        tf.setBackground(BG_CARD);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(TEXT_PRIMARY);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_BLUE, 1),
            new EmptyBorder(5, 8, 5, 8)));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    private void styleScrollPane(JScrollPane sp) {
        sp.setBorder(BorderFactory.createLineBorder(new Color(50, 65, 95), 1));
        sp.getViewport().setBackground(BG_CARD);
    }

    private void setStatus(String msg) { statusLabel.setText(msg); }
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ── Main ──────────────────────────────────────────────
    public static void main(String[] args) {
        try {
            // Use cross-platform L&F so custom colors are always respected
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("Button.select", BG_CARD);
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            StockTradingGUI gui = new StockTradingGUI();
            gui.setVisible(true);
        });
    }
}
