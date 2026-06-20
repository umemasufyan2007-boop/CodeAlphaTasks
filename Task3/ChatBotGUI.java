//  ChatBotGUI.java  -  Modern Chat Interface (Java Swing)
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class ChatBotGUI extends JFrame {

    // ---Color Palette--------
    private static final Color BG_MAIN       = new Color(15, 17, 26);
    private static final Color BG_SIDEBAR    = new Color(22, 25, 40);
    private static final Color BG_CHAT       = new Color(18, 21, 33);
    private static final Color BG_INPUT_AREA = new Color(25, 29, 46);
    private static final Color BUBBLE_BOT    = new Color(42, 48, 78);
    private static final Color BUBBLE_USER   = new Color(108, 59, 181);  // rich purple
    private static final Color ACCENT_PURPLE = new Color(138, 79, 221);
    private static final Color ACCENT_TEAL   = new Color(56, 189, 168);  // teal
    private static final Color ACCENT_CORAL  = new Color(255, 107, 107); // coral
    private static final Color ACCENT_AMBER  = new Color(255, 184, 77);  // amber
    private static final Color TEXT_WHITE    = new Color(235, 237, 250);
    private static final Color TEXT_MUTED    = new Color(130, 140, 175);
    private static final Color SEND_BTN      = new Color(108, 59, 181);
    private static final Color SEND_HOVER    = new Color(138, 79, 221);

    // -- Fonts ------------------
    private static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_BOLD    = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FONT_MONO    = new Font("Consolas", Font.PLAIN, 13);

    // --State--------------------------
    private ChatBotEngine engine = new ChatBotEngine();
    private JPanel        chatPanel;
    private JScrollPane   chatScroll;
    private JTextField    inputField;
    private JButton       sendButton;
    private JLabel        statusLabel;

    // ---Constructor---------------------------------
    public ChatBotGUI() {
        initFrame();
        buildUI();
        sendWelcomeMessage();
    }
    private void initFrame() {
        setTitle("Nova AI - Chat Assistant");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout());
    }
    private void buildUI() {
        add(buildSidebar(),   BorderLayout.WEST);
        add(buildChatArea(),  BorderLayout.CENTER);
    }

    // ---Sidebar---------------------
    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setBackground(BG_SIDEBAR);
        side.setPreferredSize(new Dimension(220, 0));
        side.setLayout(new BorderLayout());
        side.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(40, 46, 70)));

        // Top: bot avatar + name
        JPanel topPanel = new JPanel();
        topPanel.setBackground(BG_SIDEBAR);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(new EmptyBorder(28, 20, 20, 20));

        // Avatar circle
        JPanel avatar = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient circle
                GradientPaint gp = new GradientPaint(0, 0, ACCENT_PURPLE, getWidth(), getHeight(), ACCENT_TEAL);
                g2.setPaint(gp);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                // Letter N
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                FontMetrics fm = g2.getFontMetrics();
                String letter = "N";
                int x = (getWidth() - fm.stringWidth(letter)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(letter, x, y);
                // Online dot
                g2.setColor(ACCENT_TEAL);
                g2.fillOval(getWidth() - 16, getHeight() - 16, 14, 14);
                g2.setColor(BG_SIDEBAR);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(getWidth() - 17, getHeight() - 17, 15, 15);
            }
        };
        avatar.setPreferredSize(new Dimension(68, 68));
        avatar.setMaximumSize(new Dimension(68, 68));
        avatar.setOpaque(false);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel("Nova AI");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(TEXT_WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel("Your Smart Assistant");
        roleLabel.setFont(FONT_SMALL);
        roleLabel.setForeground(ACCENT_TEAL);
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Online badge
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        badge.setBackground(BG_SIDEBAR);
        JPanel dot = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT_TEAL);
                g2.fillOval(0, 2, 8, 8);
            }
        };
        dot.setPreferredSize(new Dimension(8, 14));
        dot.setOpaque(false);
        JLabel onlineLbl = new JLabel("Online");
        onlineLbl.setFont(FONT_SMALL);
        onlineLbl.setForeground(ACCENT_TEAL);
        badge.add(dot); badge.add(onlineLbl);
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(avatar);
        topPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        topPanel.add(nameLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        topPanel.add(roleLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        topPanel.add(badge);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(45, 52, 80));
        sep.setBackground(new Color(45, 52, 80));

        // Quick topic buttons
        JPanel quickPanel = new JPanel();
        quickPanel.setBackground(BG_SIDEBAR);
        quickPanel.setLayout(new BoxLayout(quickPanel, BoxLayout.Y_AXIS));
        quickPanel.setBorder(new EmptyBorder(16, 14, 14, 14));

        JLabel topicsLbl = new JLabel("  Quick Topics");
        topicsLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        topicsLbl.setForeground(TEXT_MUTED);
        topicsLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        quickPanel.add(topicsLbl);
        quickPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        String[][] topics = {
            {"Tell me a joke",     "#FF6B6B"},
            {"Fun fact",           "#38BDA8"},
            {"Motivate me",        "#FFB84D"},
            {"Programming help",   "#8A4FDD"},
            {"Study tips",         "#5B9CF6"},
            {"How are you?",       "#F472B6"}
        };

        for (String[] topic : topics) {
            JButton btn = createTopicButton(topic[0], Color.decode(topic[1]));
            quickPanel.add(btn);
            quickPanel.add(Box.createRigidArea(new Dimension(0, 7)));
        }

        // Bottom: clear chat
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_SIDEBAR);
        bottomPanel.setBorder(new EmptyBorder(10, 14, 20, 14));

        JButton clearBtn = new JButton("Clear Chat");
        clearBtn.setBackground(new Color(50, 30, 80));
        clearBtn.setForeground(new Color(180, 140, 255));
        clearBtn.setFont(FONT_BOLD);
        clearBtn.setBorder(new EmptyBorder(10, 0, 10, 0));
        clearBtn.setFocusPainted(false);
        clearBtn.setOpaque(true);
        clearBtn.setContentAreaFilled(true);
        clearBtn.setBorderPainted(false);
        clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> {
            chatPanel.removeAll();
            chatPanel.revalidate();
            chatPanel.repaint();
            sendWelcomeMessage();
        });

        bottomPanel.add(clearBtn, BorderLayout.CENTER);

        side.add(topPanel,    BorderLayout.NORTH);
        side.add(sep,         BorderLayout.CENTER);

        JPanel midWrapper = new JPanel(new BorderLayout());
        midWrapper.setBackground(BG_SIDEBAR);
        midWrapper.add(quickPanel, BorderLayout.NORTH);
        side.add(midWrapper,  BorderLayout.CENTER);
        side.add(bottomPanel, BorderLayout.SOUTH);

        return side;
    }

    private JButton createTopicButton(String text, Color accent) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                super.paintComponent(g);
            }
        };
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(new Color(
            Math.min(accent.getRed() / 5 + 25, 255),
            Math.min(accent.getGreen() / 5 + 25, 255),
            Math.min(accent.getBlue() / 5 + 45, 255)
        ));
        btn.setForeground(accent.brighter());
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(6, 12, 6, 12));
        btn.addActionListener(e -> sendMessage(text));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(Color.WHITE);
                btn.setContentAreaFilled(true);
                btn.setOpaque(true);
            }
            public void mouseExited(MouseEvent e) {
                btn.setForeground(accent.brighter());
                btn.setContentAreaFilled(false);
                btn.setOpaque(false);
            }
        });
        return btn;
    }
    // ---Chat Area------------------------------
    private JPanel buildChatArea() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_CHAT);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG_SIDEBAR);
        topBar.setBorder(new EmptyBorder(14, 20, 14, 20));
        topBar.setPreferredSize(new Dimension(0, 55));

        JLabel chatTitle = new JLabel("Chat with Nova");
        chatTitle.setFont(FONT_TITLE);
        chatTitle.setForeground(TEXT_WHITE);

        statusLabel = new JLabel("Nova is ready to chat!");
        statusLabel.setFont(FONT_SMALL);
        statusLabel.setForeground(ACCENT_TEAL);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BG_SIDEBAR);
        titlePanel.add(chatTitle,   BorderLayout.NORTH);
        titlePanel.add(statusLabel, BorderLayout.SOUTH);

        topBar.add(titlePanel, BorderLayout.WEST);

        // Chat messages panel
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(BG_CHAT);
        chatPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        chatScroll = new JScrollPane(chatPanel);
        chatScroll.setBorder(null);
        chatScroll.setBackground(BG_CHAT);
        chatScroll.getViewport().setBackground(BG_CHAT);
        chatScroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                thumbColor = new Color(80, 60, 130);
                trackColor = BG_CHAT;
            }
        });
        chatScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Input area
        JPanel inputArea = buildInputArea();

        wrapper.add(topBar,    BorderLayout.NORTH);
        wrapper.add(chatScroll,BorderLayout.CENTER);
        wrapper.add(inputArea, BorderLayout.SOUTH);

        return wrapper;
    }

    // ----Input Area---------------------------------
    private JPanel buildInputArea() {
        JPanel area = new JPanel(new BorderLayout(10, 0));
        area.setBackground(BG_INPUT_AREA);
        area.setBorder(new EmptyBorder(14, 16, 14, 16));

        inputField = new JTextField();
        inputField.setBackground(new Color(32, 37, 58));
        inputField.setForeground(TEXT_WHITE);
        inputField.setCaretColor(ACCENT_PURPLE);
        inputField.setFont(FONT_BODY);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 68, 105), 1),
            new EmptyBorder(10, 15, 10, 15)));
        inputField.putClientProperty("JTextField.placeholderText", "Type a message...");

        sendButton = new JButton("Send") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
            }
        };
        sendButton.setBackground(SEND_BTN);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(FONT_BOLD);
        sendButton.setPreferredSize(new Dimension(90, 44));
        sendButton.setBorderPainted(false);
        sendButton.setFocusPainted(false);
        sendButton.setContentAreaFilled(false);
        sendButton.setOpaque(false);
        sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { sendButton.setBackground(SEND_HOVER); }
            public void mouseExited(MouseEvent e)  { sendButton.setBackground(SEND_BTN); }
        });

        sendButton.addActionListener(e -> sendMessage(inputField.getText()));
        inputField.addActionListener(e -> sendMessage(inputField.getText()));

        area.add(inputField, BorderLayout.CENTER);
        area.add(sendButton, BorderLayout.EAST);
        return area;
    }

    // ---Send Message------------------------------------
    private void sendMessage(String text) {
        if (text == null || text.trim().isEmpty()) return;
        inputField.setText("");

        addBubble(text, true);

        // Simulate typing delay
        statusLabel.setText("Nova is typing...");
        statusLabel.setForeground(ACCENT_AMBER);

        Timer delay = new Timer(700, e -> {
            String response = engine.getResponse(text);
            addBubble(response, false);
            statusLabel.setText("Nova is ready to chat!");
            statusLabel.setForeground(ACCENT_TEAL);
        });
        delay.setRepeats(false);
        delay.start();
    }

    // ---Message Bubble----------------------------
    private void addBubble(String text, boolean isUser) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG_CHAT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel bubble = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isUser) {
                    GradientPaint gp = new GradientPaint(0, 0,
                        new Color(130, 70, 210), getWidth(), getHeight(),
                        new Color(90, 45, 160));
                    g2.setPaint(gp);
                } else {
                    g2.setColor(BUBBLE_BOT);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                super.paintComponent(g);
            }
        };
        bubble.setOpaque(false);
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBorder(new EmptyBorder(10, 14, 10, 14));

        // Wrap text
        JLabel msgLabel = new JLabel("<html><body style='width: 320px; font-family: Segoe UI; font-size: 13px;'>"
                + escapeHtml(text) + "</body></html>");
        msgLabel.setForeground(TEXT_WHITE);
        msgLabel.setFont(FONT_BODY);
        bubble.add(msgLabel);

        // Timestamp
        java.time.LocalTime now = java.time.LocalTime.now();
        String time = String.format("%02d:%02d", now.getHour(), now.getMinute());
        JLabel timeLabel = new JLabel(isUser ? "You  " + time : "Nova  " + time);
        timeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        timeLabel.setForeground(new Color(160, 170, 210));
        bubble.add(Box.createRigidArea(new Dimension(0, 4)));
        bubble.add(timeLabel);

        // Align user right, bot left
        JPanel wrapper2 = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        wrapper2.setBackground(BG_CHAT);
        wrapper2.add(bubble);

        row.add(wrapper2, BorderLayout.CENTER);

        chatPanel.add(row);
        chatPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        chatPanel.revalidate();
        chatPanel.repaint();

        // Auto-scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = chatScroll.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    private String escapeHtml(String text) {
        return text.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    private void sendWelcomeMessage() {
        Timer t = new Timer(300, e ->
            addBubble("Hello! I'm Nova, your AI assistant. Ask me anything - jokes, facts, advice, programming help, and much more!", false)
        );
        t.setRepeats(false);
        t.start();
    }

    // --Main--------------------------------------
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new ChatBotGUI().setVisible(true));
    }
}
