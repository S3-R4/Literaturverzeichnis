package view;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import model.data.DataManager;
import model.data.LastBibliography;

/**
 * Displays the start window for creating or importing a bibliography.
 */
public class GUI_Startfenster extends JFrame {
    final String maintitle = "Literaturverzeichnis";
    private final JButton btnContinue = new JButton("Weiterarbeiten");
    private final JLabel lblLastFile = new JLabel();

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

        JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 20, 12));
        pnlButtons.setBorder(BorderFactory.createEmptyBorder(0, 35, 25, 35));
        pnlButtons.setPreferredSize(new Dimension(600, 133));

        JButton btnVerzeichnisAnlegen = new JButton("Neues Verzeichnis anlegen");
        btnVerzeichnisAnlegen.addActionListener(e -> openVerzeichnisAnlegen());
        pnlButtons.add(btnVerzeichnisAnlegen);

        JButton btnVerzeichnisImportieren = new JButton("Verzeichnis importieren");
        btnVerzeichnisImportieren.addActionListener(e -> openVerzeichnisImportieren());
        pnlButtons.add(btnVerzeichnisImportieren);

        btnContinue.addActionListener(e -> openLastFile());
        pnlButtons.add(btnContinue);
        lblLastFile.putClientProperty("html.disable", true);
        lblLastFile.setLabelFor(btnContinue);
        pnlButtons.add(lblLastFile);
        try {
            Path path = lastFile();
            lblLastFile.setText(path.getFileName().toString());
            lblLastFile.setToolTipText(path.toString());
        } catch (IOException e) {
            showLastFileError(e.getMessage());
            btnContinue.setEnabled(false);
        }
        add(pnlButtons, BorderLayout.SOUTH);

        setLocationRelativeTo(null); 
        setVisible(true);
    }

    private Path lastFile() throws IOException {
        Path path = LastBibliography.read();
        if (path.getFileName() == null || (!Files.isRegularFile(path)
                && !Files.isRegularFile(DataManager.workingPathFor(path)))) {
            throw new IOException("Letzte Datei nicht gefunden.");
        }
        return path;
    }

    private void openLastFile() {
        try {
            BibliographySession.openExisting(this, lastFile());
        } catch (IOException | RuntimeException e) {
            showLastFileError("Öffnen fehlgeschlagen: " + e.getMessage());
        }
    }

    private void showLastFileError(String message) {
        lblLastFile.setText(message);
        lblLastFile.setToolTipText(message);
        lblLastFile.setForeground(Color.RED);
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
