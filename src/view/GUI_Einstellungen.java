package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.*;

/**
 * Displays the settings dialog; applying a theme is not yet implemented.
 */
public class GUI_Einstellungen extends JDialog{

    private JComboBox<String> designerComboBox;
    private JButton abbrechenButton;
    private JButton bestaetigenButton;

    /**
     * Creates the GUI_Einstellungen view and its controls.
     *
     * @param parent owner window
     */
    public GUI_Einstellungen(JFrame parent) {
        super(parent, "Einstellungen", true);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- ZEILE 1: Label (1/4) und ComboBox (3/4) ---
        
        JLabel designerLabel = new JLabel("Designer:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25; 
        add(designerLabel, gbc);

        designerComboBox = new JComboBox<>();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.75;
        add(designerComboBox, gbc);

        // --- ZEILE 2: Buttons in der rechten Haelfte ---
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        abbrechenButton = new JButton("Abbrechen");
        bestaetigenButton = new JButton("Bestätigen");
        
        buttonPanel.add(abbrechenButton);
        buttonPanel.add(bestaetigenButton);

        JPanel untereZeilePanel = new JPanel(new GridBagLayout());
        GridBagConstraints subGbc = new GridBagConstraints();
        subGbc.fill = GridBagConstraints.HORIZONTAL;
        
        subGbc.gridx = 0;
        subGbc.weightx = 0.5;
        untereZeilePanel.add(Box.createHorizontalGlue(), subGbc);
        
        subGbc.gridx = 1;
        subGbc.weightx = 0.5;
        untereZeilePanel.add(buttonPanel, subGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 10, 10, 10);
        add(untereZeilePanel, gbc);

        // --- BUTTON-AKTIONEN ---
        abbrechenButton.addActionListener(e -> abbrechenAction());

        bestaetigenButton.addActionListener(e -> bestaetigenAction());

        // Fenstergröße und Position anpassen
        setSize(400, 140);
        setResizable(false); // Optional: Verhindert Verziehen des Fensters
        setLocationRelativeTo(parent); // Zentriert über dem Hauptfenster
    }
    
    /**
     * Closes the settings dialog.
     */
    private void abbrechenAction() {
    	dispose();
    }
    
    /**
     * Closes the dialog; applying the selected theme is not yet implemented.
     */
    private void bestaetigenAction() { //TODO: implement code
    	dispose();
    }
}
