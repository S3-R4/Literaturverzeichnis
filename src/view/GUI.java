package view;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

public class GUI extends JFrame {
    final String maintitle = "Literaturverzeichnis";

    public GUI() {
        setTitle(maintitle);
        setSize(600, 280);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lblWindowLabel = new JLabel(maintitle);
        lblWindowLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblWindowLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        add(lblWindowLabel, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 40));

        JButton btnVerzeichnisAnlegen = new JButton("Neues Verzeichnis anlegen"); //TODO: implement Action later
        btnVerzeichnisAnlegen.setPreferredSize(new Dimension(245, 48));
        pnlButtons.add(btnVerzeichnisAnlegen);

        JButton btnVerzeichnisImportieren = new JButton("Verzeichnis importieren"); //TODO: implement Action later
        btnVerzeichnisImportieren.setPreferredSize(new Dimension(245, 48));
        pnlButtons.add(btnVerzeichnisImportieren);

        add(pnlButtons, BorderLayout.SOUTH);

        setVisible(true);
    }
}
