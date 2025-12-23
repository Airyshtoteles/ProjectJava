package id.ac.kampus.frs.ui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * BaseDashboardFrame
 * - Kerangka utama dashboard bertema modern: header, sidebar, content, footer.
 * - Menyediakan API untuk menambah halaman (CardLayout) dan berpindah dengan fade.
 */
public class BaseDashboardFrame extends JFrame {
    protected final JPanel sidebar = new JPanel();
    protected final UITheme.FadingPanel content = new UITheme.FadingPanel(new CardLayout());
    protected final Map<String, UITheme.SidebarItem> navItems = new LinkedHashMap<>();
    protected final JLabel lblPageTitle = new JLabel();
    protected final JLabel lblUser = new JLabel();

    public BaseDashboardFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);

        // Root dengan gradient lembut
        UITheme.GradientBackground root = new UITheme.GradientBackground();
        root.setLayout(new BorderLayout());
        setContentPane(root);

        // Header bar
        root.add(buildHeader(), BorderLayout.NORTH);

        // Sidebar kiri
        root.add(buildSidebar(), BorderLayout.WEST);

        // Content area (cards) + footer
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        content.setLayout(new CardLayout());
        center.add(content, BorderLayout.CENTER);
        center.add(buildFooter(), BorderLayout.SOUTH);
        root.add(center, BorderLayout.CENTER);

        // Efek fade saat tampil
        SwingUtilities.invokeLater(() -> content.fadeIn(450));
    }

    protected final JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout()){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g); Graphics2D g2=(Graphics2D) g.create(); UITheme.enableAA(g2);
                int w=getWidth(), h=getHeight();
                g2.setPaint(new GradientPaint(0,0, UITheme.PRIMARY, w,0, UITheme.ACCENT));
                g2.fillRect(0,0,w,h); g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(100, 56));
        header.setOpaque(false);

        // Kiri: logo + page title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10)); left.setOpaque(false);
        JComponent logo = makeLogoDot(); left.add(logo);
        lblPageTitle.setText("Dashboard"); lblPageTitle.setForeground(Color.WHITE); lblPageTitle.setFont(UITheme.uiFont(Font.BOLD, 18));
        left.add(lblPageTitle);

        // Kanan: user + logout
        headerActions.setOpaque(false);
        lblUser.setForeground(Color.WHITE); lblUser.setFont(UITheme.uiFont(Font.PLAIN, 14));
        UITheme.AnimatedButton btnLogout = new UITheme.AnimatedButton("Logout"); btnLogout.addActionListener(e -> onLogout());
        btnLogout.setPreferredSize(new Dimension(100, 32));
        headerActions.add(lblUser); headerActions.add(btnLogout);

        header.add(left, BorderLayout.WEST); header.add(headerActions, BorderLayout.EAST);
        return header;
    }

    private JComponent buildSidebar() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setPreferredSize(new Dimension(220, 100));
        wrap.setOpaque(true);
        wrap.setBackground(UITheme.SECONDARY);

        sidebarItems.setOpaque(false);
        sidebarItems.setLayout(new BoxLayout(sidebarItems, BoxLayout.Y_AXIS));
        wrap.add(sidebarItems, BorderLayout.NORTH);
        return wrap;
    }

    private JComponent buildFooter() {
        JPanel foot = new JPanel(new BorderLayout()); foot.setOpaque(false);
        JLabel lbl = new JLabel("\u00A9 2025 FRS System"); lbl.setFont(UITheme.uiFont(Font.PLAIN, 12)); lbl.setForeground(new Color(0,0,0,120));
        foot.add(lbl, BorderLayout.EAST);
        foot.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 16));
        return foot;
    }

    private JComponent makeLogoDot(){
        return new JComponent(){
            @Override public Dimension getPreferredSize(){ return new Dimension(28,28);} 
            @Override protected void paintComponent(Graphics g){ Graphics2D g2=(Graphics2D) g.create(); UITheme.enableAA(g2); int d=24; g2.setColor(Color.WHITE); g2.fillOval(2,2,d,d); g2.setColor(UITheme.ACCENT); g2.fillOval(6,6,d-8,d-8); g2.dispose(); }
        };
    }

    protected void onLogout(){
        dispose();
        // kembali ke login
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    // API untuk nav + konten
    public void setUserInfo(String text){ lblUser.setText(text); }
    public void setPageTitle(String text){ lblPageTitle.setText(text); }

    

    // Simpler: Keep explicit container references
    private final JPanel sidebarItems = new JPanel();
    {
        sidebarItems.setOpaque(false);
        sidebarItems.setLayout(new BoxLayout(sidebarItems, BoxLayout.Y_AXIS));
    }
    {
        // Attach items panel to sidebar wrapper
        // Rebuild sidebar with items
    }

    // sidebarItems sudah ditempel di buildSidebar

    public void registerPage(String key, String title, JComponent panel) {
        UITheme.SidebarItem item = new UITheme.SidebarItem(title);
        item.addMouseListener(new java.awt.event.MouseAdapter(){ @Override public void mouseClicked(java.awt.event.MouseEvent e){ showPage(key); }});
        navItems.put(key, item);
        sidebarItems.add(item);
        content.add(panel, key);
    }

    public void showPage(String key) {
        // Fade-out, switch, fade-in for smoother transition
        content.fadeOut(120, () -> {
            for (Map.Entry<String, UITheme.SidebarItem> e : navItems.entrySet()) e.getValue().setActive(e.getKey().equals(key));
            ((CardLayout) content.getLayout()).show(content, key);
            setPageTitle(key);
            content.fadeIn(180);
        });
    }

    public void setSidebarBadge(String key, int count) {
        UITheme.SidebarItem item = navItems.get(key);
        if (item != null) item.setBadge(count);
    }
}
