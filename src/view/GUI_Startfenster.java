package view;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Displays the start window for creating or importing a bibliography.
 */
public class GUI_Startfenster extends JFrame {
    final String maintitle = "Literaturverzeichnis";

    /**
     * Creates the GUI_Startfenster view and its controls.
     */
    public GUI_Startfenster() {
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

        JButton btnVerzeichnisAnlegen = new JButton("Neues Verzeichnis anlegen");
        btnVerzeichnisAnlegen.setPreferredSize(new Dimension(245, 48));
        btnVerzeichnisAnlegen.addActionListener(e -> openVerzeichnisAnlegen());
        pnlButtons.add(btnVerzeichnisAnlegen);

        JButton btnVerzeichnisImportieren = new JButton("Verzeichnis importieren");
        btnVerzeichnisImportieren.setPreferredSize(new Dimension(245, 48));
        btnVerzeichnisImportieren.addActionListener(e -> openVerzeichnisImportieren());
        pnlButtons.add(btnVerzeichnisImportieren);

        add(pnlButtons, BorderLayout.SOUTH);

        setLocationRelativeTo(null); 
        setVisible(true);
    }
    
    /**
     * Opens the bibliography creation window and closes the start window.
     */
    private void openVerzeichnisAnlegen() {
    	new GUI_Neues_Verzeichnis();
    	dispose();
    }
    
    /**
     * Opens the bibliography import window and closes the start window.
     */
    private void openVerzeichnisImportieren() {
    	new GUI_Verzeichnis_Importieren();
    	dispose();
    }
}
