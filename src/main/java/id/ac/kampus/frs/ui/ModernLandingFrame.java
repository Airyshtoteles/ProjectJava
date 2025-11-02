package id.ac.kampus.frs.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * ModernLandingFrame
 * Contoh tampilan Swing modern dengan:
 * - Background gradient (biru -> ungu)
 * - Card transparan dengan soft shadow
 * - Tombol animasi (hover glow + klik scale)
 * - Layout responsif (BorderLayout + GridBagLayout)
 * - Tanpa library eksternal (murni Swing/AWT)
 */
public class ModernLandingFrame extends JFrame {
    public ModernLandingFrame() {
        setTitle("Modern Landing • Aplikasi FRS Online");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(880, 560));
        setLocationRelativeTo(null);

        // Panel utama dengan gradient penuh layar
        GradientPanel root = new GradientPanel();
        root.setLayout(new BorderLayout());

        // Panel pusat menggunakan GridBagLayout agar responsif saat resize
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false); // transparan, biar gradient terlihat
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0; gc.insets = new Insets(24,24,24,24);

        // Card berisi judul, deskripsi, dan tombol Start
        SoftShadowCard card = new SoftShadowCard();
        card.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Aplikasi FRS Online");
        title.setFont(selectFont("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(0x1b1e2b));

        JLabel subtitle = new JLabel("Kelola KRS/FRS Anda dengan antarmuka futuristik dan nyaman.");
        subtitle.setFont(selectFont("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(new Color(0x3a4155));

        AnimatedButton start = new AnimatedButton("Start Now \uD83D\uDE80");
        start.setPreferredSize(new Dimension(220, 48));
        start.addActionListener(e -> JOptionPane.showMessageDialog(this, "Let's go!"));

        GridBagConstraints cg = new GridBagConstraints();
        cg.gridx = 0; cg.gridy = 0; cg.insets = new Insets(10, 10, 4, 10);
        card.add(title, cg);
        cg.gridy++;
        cg.insets = new Insets(4, 10, 16, 10);
        card.add(subtitle, cg);
        cg.gridy++;
        card.add(start, cg);

        center.add(card, gc);
        root.add(center, BorderLayout.CENTER);
        setContentPane(root);
    }

    /**
     * Memilih font dengan fallback ke SansSerif jika tidak tersedia.
     */
    private static Font selectFont(String preferred, int style, int size) {
        // Coba prefered -> Roboto -> Poppins -> Segoe UI -> SansSerif
        String[] candidates = new String[]{preferred, "Roboto", "Poppins", "Segoe UI", "SansSerif"};
        for (String name : candidates) {
            Font f = new Font(name, style, size);
            if (f.getFamily() != null && !"Dialog".equalsIgnoreCase(f.getFamily())) {
                return f;
            }
        }
        return new Font(Font.SANS_SERIF, style, size);
    }

    /**
     * Panel gradient lembut (biru -> ungu) yang merespons ukuran jendela.
     * Menggunakan 2 layer gradient agar hasilnya lebih kaya.
     */
    static class GradientPanel extends JPanel {
        private final Color c1 = Color.decode("#3490dc"); // biru
        private final Color c2 = Color.decode("#586ff9"); // ungu
        private final Color light = Color.decode("#f0f4ff"); // putih lembut

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            int w = getWidth();
            int h = getHeight();
            // Smoothing
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Layer 1: linear gradient
            GradientPaint gp = new GradientPaint(0, 0, c1, w, h, c2);
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);

            // Layer 2: radial highlight (soft) di kanan atas
            RadialGradientPaint rg = new RadialGradientPaint(
                    new Point((int) (w * 0.75), (int) (h * 0.25)), Math.max(w, h) / 2f,
                    new float[]{0f, 1f}, new Color[]{new Color(light.getRed(), light.getGreen(), light.getBlue(), 80), new Color(255, 255, 255, 0)}
            );
            g2.setPaint(rg);
            g2.fillRect(0, 0, w, h);
            g2.dispose();
        }
    }

    /**
     * Card dengan soft shadow buatan (tanpa blur filter). Shadow dibuat
     * dari beberapa lapisan rounded rectangle semi-transparan.
     */
    static class SoftShadowCard extends JPanel {
        private final int arc = 24;
        private final Color bg = new Color(255, 255, 255, 220); // sedikit transparan

        public SoftShadowCard() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        }

        @Override
        protected void paintComponent(Graphics g) {
            int w = getWidth();
            int h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Soft shadow berlapis (offset kecil + alpha menurun)
            for (int i = 8; i >= 1; i--) {
                float alpha = 0.03f * i; // total ~0.24
                g2.setColor(new Color(0f, 0f, 0f, alpha));
                g2.fill(new RoundRectangle2D.Float(8 + (8 - i), 10 + (8 - i), w - 16, h - 16, arc + i, arc + i));
            }

            // Panel utama (putih lembut)
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(8, 10, w - 16, h - 16, arc, arc));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Tombol animasi dengan efek:
     * - Hover: glow biru-ungu yang fade-in/out
     * - Klik: scale sedikit mengecil lalu kembali
     */
    static class AnimatedButton extends JButton {
        private float glow = 0f; // 0..1
        private float scale = 1f; // 0.97..1.0
        private Timer hoverTimer;
        private Timer pressTimer;
        private boolean hovering = false;

        private final Color base = Color.decode("#586ff9");
        private final Color base2 = Color.decode("#3490dc");

        public AnimatedButton(String text) {
            super(text);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(selectFont("Segoe UI", Font.BOLD, 16));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Hover animation timer
            hoverTimer = new Timer(15, e -> {
                float target = hovering ? 1f : 0f;
                float speed = 0.08f; // smoothing
                glow += (target - glow) * speed;
                if (Math.abs(target - glow) < 0.01f) glow = target;
                repaint();
            });
            hoverTimer.start();

            // Press animation timer (springy to 0.97 then back)
            pressTimer = new Timer(15, e -> {
                float target = 1f;
                float speed = 0.2f;
                scale += (target - scale) * speed;
                if (Math.abs(target - scale) < 0.005f) scale = target;
                repaint();
            });

            // Mouse interactions
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovering = true; }
                @Override public void mouseExited(MouseEvent e) { hovering = false; }
                @Override public void mousePressed(MouseEvent e) {
                    scale = 0.97f; // shrink a bit
                    pressTimer.restart();
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arc = 20;

            // Transform untuk efek scale sekitar pusat tombol
            g2.translate(w / 2.0, h / 2.0);
            g2.scale(scale, scale);
            g2.translate(-w / 2.0, -h / 2.0);

            // Latar tombol: gradient biru -> ungu
            GradientPaint gp = new GradientPaint(0, 0, base2, w, h, base);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, w, h, arc, arc);

            // Glow tepi (berdasarkan nilai glow 0..1)
            if (glow > 0f) {
                int glowWidth = 3 + (int) (8 * glow);
                Color glowColor = new Color(88, 111, 249, (int) (120 * glow));
                g2.setStroke(new BasicStroke(glowWidth));
                g2.setColor(glowColor);
                g2.drawRoundRect(glowWidth / 2, glowWidth / 2, w - glowWidth, h - glowWidth, arc, arc);
            }

            // Teks tombol
            FontMetrics fm = g2.getFontMetrics(getFont());
            String text = getText();
            int tw = fm.stringWidth(text);
            int th = fm.getAscent();
            g2.setColor(Color.WHITE);
            g2.drawString(text, (w - tw) / 2, (h + th) / 2 - 3);

            g2.dispose();
        }
    }

    /**
     * Optional: contoh transisi fade antar panel tanpa JavaFX.
     * Dipakai jika ingin dikembangkan lebih lanjut (tidak terpakai default).
     */
    static class FadingPanel extends JPanel {
        private float alpha = 1f;
        private Timer timer;
        public FadingPanel(LayoutManager lm) {
            super(lm);
            setOpaque(false);
        }
        public void fadeIn(int ms) {
            if (timer != null && timer.isRunning()) timer.stop();
            alpha = 0f;
            int steps = Math.max(1, ms / 15);
            timer = new Timer(15, e -> {
                alpha += 1f / steps;
                if (alpha >= 1f) { alpha = 1f; timer.stop(); }
                repaint();
            });
            timer.start();
        }
        public void fadeOut(int ms, Runnable onDone) {
            if (timer != null && timer.isRunning()) timer.stop();
            alpha = 1f;
            int steps = Math.max(1, ms / 15);
            timer = new Timer(15, e -> {
                alpha -= 1f / steps;
                if (alpha <= 0f) { alpha = 0f; timer.stop(); if (onDone != null) onDone.run(); }
                repaint();
            });
            timer.start();
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    // Demo mandiri
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ModernLandingFrame f = new ModernLandingFrame();
            f.setVisible(true);
        });
    }
}
