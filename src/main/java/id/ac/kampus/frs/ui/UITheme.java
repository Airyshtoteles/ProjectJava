package id.ac.kampus.frs.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * UITheme
 * - Menyediakan palet warna, font, dan helper komponen bertema modern.
 * - Murni Swing/AWT (tanpa library eksternal).
 */
public final class UITheme {
    private UITheme() {}

    // Palet warna (pastel modern)
    public static final Color PRIMARY = Color.decode("#3F51B5");      // indigo
    public static final Color SECONDARY = Color.decode("#E8EAF6");   // abu lembut
    public static final Color ACCENT = Color.decode("#5C6BC0");      // ungu lembut
    public static final Color TEXT = Color.decode("#1E1E2F");        // teks utama
    public static final Color BACKGROUND = Color.decode("#F5F7FB");  // latar global

    public static Font uiFont(int style, int size) {
        String[] candidates = {"Segoe UI", "Poppins", "Roboto", "SansSerif"};
        for (String name : candidates) {
            Font f = new Font(name, style, size);
            if (f.getFamily() != null && !"Dialog".equalsIgnoreCase(f.getFamily())) return f;
        }
        return new Font(Font.SANS_SERIF, style, size);
    }

    public static void enableAA(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    /** RoundedPanel dengan soft shadow buatan. */
    public static class CardPanel extends JPanel {
        private final int arc;
        private final Color fill;
        public CardPanel(int arc) { this(arc, Color.WHITE); }
        public CardPanel(int arc, Color fill) {
            this.arc = arc; this.fill = new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), 230);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        }
        @Override protected void paintComponent(Graphics g) {
            int w = getWidth(), h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            UITheme.enableAA(g2);
            for (int i = 8; i >= 1; i--) {
                float alpha = 0.03f * i;
                g2.setColor(new Color(0f, 0f, 0f, alpha));
                g2.fill(new RoundRectangle2D.Float(6 + (8 - i), 8 + (8 - i), w - 12, h - 12, arc + i, arc + i));
            }
            g2.setColor(fill);
            g2.fill(new RoundRectangle2D.Float(6, 8, w - 12, h - 12, arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Tombol dengan animasi hover glow + press scale. */
    public static class AnimatedButton extends JButton {
        private float glow = 0f, scale = 1f; boolean hovering;
        private final Timer hoverTimer, pressTimer;
        public AnimatedButton(String text) {
            super(text);
            setContentAreaFilled(false); setBorderPainted(false); setFocusPainted(false);
            setForeground(Color.WHITE); setFont(uiFont(Font.BOLD, 14));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            hoverTimer = new Timer(15, e -> { float target = hovering ? 1f : 0f; glow += (target - glow) * 0.1f; repaint(); });
            hoverTimer.start();
            pressTimer = new Timer(15, e -> { scale += (1f - scale) * 0.2f; repaint(); });
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovering = true; }
                @Override public void mouseExited(MouseEvent e) { hovering = false; }
                @Override public void mousePressed(MouseEvent e) { scale = 0.97f; pressTimer.restart(); }
            });
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create(); UITheme.enableAA(g2);
            int w = getWidth(), h = getHeight(), arc = 18;
            g2.translate(w/2.0, h/2.0); g2.scale(scale, scale); g2.translate(-w/2.0, -h/2.0);
            g2.setPaint(new GradientPaint(0,0, ACCENT, w,h, PRIMARY));
            g2.fillRoundRect(0, 0, w, h, arc, arc);
            if (glow > 0) { int gw = 3 + (int)(7*glow); g2.setColor(new Color(92,107,192,(int)(120*glow))); g2.setStroke(new BasicStroke(gw)); g2.drawRoundRect(gw/2, gw/2, w-gw, h-gw, arc, arc); }
            super.setForeground(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics(getFont()); String t = getText();
            g2.setColor(Color.WHITE); g2.drawString(t, (w-fm.stringWidth(t))/2, (h+fm.getAscent())/2-3);
            g2.dispose();
        }
    }

    /** Sidebar item dengan hover dan highlight garis accent. */
    public static class SidebarItem extends JPanel {
        private boolean hover, active; private final JLabel label = new JLabel();
        private int badge = 0;
        public SidebarItem(String text) {
            setLayout(new BorderLayout()); setOpaque(false);
            label.setText(text); label.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 12));
            label.setFont(uiFont(Font.PLAIN, 14)); label.setForeground(TEXT);
            add(label, BorderLayout.CENTER);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter(){
                @Override public void mouseEntered(MouseEvent e){ hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e){ hover = false; repaint(); }
            });
        }
        public void setActive(boolean b){ active = b; repaint(); }
        public void setBadge(int count){ badge = Math.max(0, count); repaint(); }
        @Override protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g.create(); UITheme.enableAA(g2);
            int w=getWidth(), h=getHeight();
            if (hover || active){ g2.setColor(new Color(92,107,192, 40)); g2.fillRect(0,0,w,h); }
            if (active){ g2.setColor(ACCENT); g2.fillRect(0,0,4,h); }
            if (badge > 0){
                String txt = String.valueOf(badge);
                Font f = UITheme.uiFont(Font.BOLD, 11);
                g2.setFont(f);
                FontMetrics fm = g2.getFontMetrics();
                int bw = Math.max(18, fm.stringWidth(txt) + 10);
                int bh = 16;
                int x = w - bw - 10;
                int y = (h - bh) / 2;
                g2.setColor(new Color(0x5C,0x6B,0xC0));
                g2.fillRoundRect(x, y, bw, bh, 12, 12);
                g2.setColor(Color.WHITE);
                g2.drawString(txt, x + (bw - fm.stringWidth(txt))/2, y + (bh + fm.getAscent())/2 - 3);
            }
            g2.dispose(); super.paintComponent(g);
        }
    }

    /** Panel gradient global lembut. */
    public static class GradientBackground extends JPanel {
        public GradientBackground(){ setOpaque(true); }
        @Override protected void paintComponent(Graphics g){
            super.paintComponent(g); Graphics2D g2=(Graphics2D) g.create(); UITheme.enableAA(g2);
            int w=getWidth(),h=getHeight();
            g2.setPaint(new GradientPaint(0,0,new Color(0xF5,0xF7,0xFB), w,h,new Color(0xE8,0xEA,0xF6)));
            g2.fillRect(0,0,w,h);
            g2.dispose();
        }
    }

    /** Fading helper untuk transisi halus. */
    public static class FadingPanel extends JPanel {
        private float alpha = 0f; private Timer timer;
        public FadingPanel(LayoutManager lm){ super(lm); setOpaque(false); }
        public void fadeIn(int ms){ start(ms, true, null); }
        public void fadeOut(int ms, Runnable after){ start(ms, false, after); }
        private void start(int ms, boolean in, Runnable after){ if (timer!=null&&timer.isRunning()) timer.stop(); int steps=Math.max(1, ms/15); alpha = in?0f:1f; timer = new Timer(15, e->{ alpha += (in?1f:-1f)/steps; if ((in&&alpha>=1f)||(!in&&alpha<=0f)){ alpha = in?1f:0f; ((Timer)e.getSource()).stop(); if (after!=null) after.run(); } repaint(); }); timer.start(); }
        @Override protected void paintComponent(Graphics g){ Graphics2D g2=(Graphics2D) g.create(); g2.setComposite(AlphaComposite.SrcOver.derive(alpha)); super.paintComponent(g2); g2.dispose(); }
    }
}
