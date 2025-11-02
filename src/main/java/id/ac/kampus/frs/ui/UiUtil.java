package id.ac.kampus.frs.ui;

import javax.swing.*;
import java.awt.*;

public class UiUtil {
    public static JPanel header(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, lblTitle.getFont().getSize2D() + 2f));
        JLabel lblSub = new JLabel(subtitle == null ? "" : subtitle);
        lblSub.setForeground(new Color(0,0,0,120));
        JPanel inner = new JPanel(new GridLayout(0,1));
        inner.setOpaque(false);
        inner.add(lblTitle);
        if (subtitle != null && !subtitle.isBlank()) inner.add(lblSub);
        p.add(inner, BorderLayout.CENTER);
        JSeparator sep = new JSeparator();
        p.add(sep, BorderLayout.SOUTH);
        return p;
    }
}
