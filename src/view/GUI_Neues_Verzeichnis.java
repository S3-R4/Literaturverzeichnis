package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.data.DataManager;

public class GUI_Neues_Verzeichnis extends JFrame {
	final String titel = "Neues Verzeichnis anlegen"; 
	private Path chosenFile;
	private final JTextField tfdVerzeichnisname = new JTextField(25);
	private final JTextField tfdDateipfad = new JTextField("<Dateipfad>");

	
	public GUI_Neues_Verzeichnis() {
		setTitle(titel);
		setSize(550, 200);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 12, 8, 12);
		gbc.anchor = GridBagConstraints.WEST;
		setLayout(layout);

		// --- Zeile 1: Verzeichnisname ---
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Verzeichnisname:"), gbc);

		gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
		add(tfdVerzeichnisname, gbc);

		// --- Zeile 2: Speicherort ---
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

		// --- Zeile 3: Buttons ---
		gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(e -> returnToStart());
		btnPanel.add(btnAbbrechen);

		JButton btnBestaetigen = new JButton("Bestätigen");
		btnBestaetigen.addActionListener(e -> confirmSelection());
		btnPanel.add(btnBestaetigen);

		add(btnPanel, gbc);

		setLocationRelativeTo(null);
		setVisible(true);

	}
	
	/*
	 * Open the start window
	 */
	private void returnToStart() {
		new GUI_Startfenster();
		dispose();
	}
	
	/*
	 * if user chose a location to save the new file -> open the Literaturverwaltung
	 */
	private void confirmSelection() {
		if (chosenFile == null) {
			JOptionPane.showMessageDialog(this, "Bitte zuerst einen Speicherort auswählen.");
			return;
		}

		try {
			DataManager manager = new DataManager();
			chosenFile = chosenFile.resolveSibling(jsonFileName());
			showSelectedPath();
			if (!manager.createJson(chosenFile)) {
				JOptionPane.showMessageDialog(this, "Die Datei existiert bereits. Bitte einen anderen Namen wählen.");
				return;
			}
			new GUI_Literaturverwaltung(manager);
			dispose();
		} catch (IOException | IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, "Die JSON-Datei konnte nicht erstellt werden:\n" + e.getMessage(),
					"Erstellen fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
		}
	}

	/*
	 * craet a fileChooser to select a file to save the Quellenverzeichnis
	 */
	private void openFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter("JSON-Dateien (*.json)", "json"));

		try {
			if (!tfdVerzeichnisname.getText().isBlank()) {
				Path name = jsonFileName();
				fileChooser.setSelectedFile((chosenFile == null ? name : chosenFile.resolveSibling(name)).toFile());
			} else if (chosenFile != null) {
				fileChooser.setSelectedFile(chosenFile.toFile());
			}
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Ungültiger Dateiname", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			chosenFile = fileChooser.getSelectedFile().toPath().toAbsolutePath();
			String name = chosenFile.getFileName().toString();
			if (!name.toLowerCase(Locale.ROOT).endsWith(".json")) {
				chosenFile = chosenFile.resolveSibling(name + ".json");
			}
			tfdVerzeichnisname.setText(chosenFile.getFileName().toString());
			showSelectedPath();
		}
	}

	private Path jsonFileName() {
		String name = tfdVerzeichnisname.getText().trim();
		if (name.isEmpty() || name.equals(".") || name.equals("..")
				|| name.contains("/") || name.contains("\\")) {
			throw new IllegalArgumentException("Bitte einen gültigen Dateinamen ohne Ordnerpfad eingeben.");
		}
		if (!name.toLowerCase(Locale.ROOT).endsWith(".json")) {
			name += ".json";
		}
		return Path.of(name);
	}

	private void showSelectedPath() {
		tfdDateipfad.setText(chosenFile.toString());
		tfdDateipfad.setToolTipText(chosenFile.toString());
	}
}
