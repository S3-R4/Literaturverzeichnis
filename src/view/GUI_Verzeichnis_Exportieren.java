package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.data.DataManager;

/**
 * Saves a "snapshot" / copy of the current Verzeichnis on a new filepath, new changes will continue on the original filepath.
 */
public class GUI_Verzeichnis_Exportieren extends JDialog{
	
	final String titel = "Verzeichnis exportieren"; 
	private Path chosenFile;
	private final JTextField tfdDateipfad = new JTextField("<Dateipfad>");
	private final DataManager manager;
	
	/**
	 * Creates the GUI_Verzeichnis_Exportieren view and its controls.
	 *
	 * @param parent owner window
	 * @param manager data manager for the active bibliography
	 */
	public GUI_Verzeichnis_Exportieren(JFrame parent, DataManager manager) {
		super(parent, "Verzeichnis exportieren", true);
		this.manager = Objects.requireNonNull(manager, "manager");

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 12, 8, 12);
		gbc.anchor = GridBagConstraints.WEST;
		setLayout(layout);

		// --- Zeile 1: Speicherort ---
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Speicherort:"), gbc);

		gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
		JPanel pathPanel = new JPanel(new BorderLayout(5, 0));
		tfdDateipfad.setEditable(false);
		pathPanel.add(tfdDateipfad, BorderLayout.CENTER);

		JButton btnSpreicherort = new JButton("Durchsuchen...");
		btnSpreicherort.addActionListener(e -> openFileChooser());
		pathPanel.add(btnSpreicherort, BorderLayout.EAST);
		add(pathPanel, gbc);

		// --- Zeile 2: Buttons ---
		gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(e -> close());
		btnPanel.add(btnAbbrechen);

		JButton btnBestaetigen = new JButton("Bestätigen");
		btnBestaetigen.addActionListener(e -> confirmSelection());
		btnPanel.add(btnBestaetigen);

		add(btnPanel, gbc);
		
		// Fenstergröße und Position anpassen
        setSize(400, 140);
        setResizable(false); // Optional: Verhindert Verziehen des Fensters
        setLocationRelativeTo(parent); // Zentriert über dem Hauptfenster
	}
	
	/**
	 * Closes the export dialog.
	 */
	private void close() {
		dispose();
	}
	
	/**
	 * Processes the selected file and reports validation or file-operation errors.
	 */
	private void confirmSelection() {
		if (chosenFile == null) {
			JOptionPane.showMessageDialog(this, "Bitte zuerst einen Speicherort auswählen.");
			return;
		}
		if (Files.exists(chosenFile) && JOptionPane.showConfirmDialog(this,
				"Die Datei existiert bereits. Überschreiben?", "Export",
				JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			this.manager.exportJson(chosenFile);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Fehler beim Erstellen der Kopie:\n" + e.getMessage(),
					"Export fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		close();
	}
	
	/**
	 * Opens a JSON file chooser and displays the selected path.
	 */
	private void openFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter("JSON-Dateien (*.json)", "json"));

		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			chosenFile = fileChooser.getSelectedFile().toPath().toAbsolutePath();
			if (!chosenFile.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".json")) {
				chosenFile = chosenFile.resolveSibling(chosenFile.getFileName() + ".json");
			}
			tfdDateipfad.setText(chosenFile.toString());
			tfdDateipfad.setToolTipText(chosenFile.toString());
		}
	}
}
