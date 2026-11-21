package view;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.List;
import java.nio.file.Path;

import model.data.DataManager;
import model.Literatur;

public class GUI_Verzeichnis_Importieren extends JFrame {
	final String titel = "Verzeichnis importieren";
	private Path chosenFile;
	private final JLabel lblVerzeichnis = new JLabel(" <Verzeichnisname> ", SwingConstants.CENTER);

	public GUI_Verzeichnis_Importieren() {
		setTitle(titel);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));
		setSize(550, 200);
		((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

		JPanel oberesPanel = new JPanel(new BorderLayout(10, 0));

		lblVerzeichnis.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lblVerzeichnis.setOpaque(true);
		lblVerzeichnis.setBackground(new Color(224, 224, 224));
		lblVerzeichnis.setPreferredSize(new Dimension(400, 30));
		oberesPanel.add(lblVerzeichnis, BorderLayout.CENTER);
		
		JButton btnDateiAuswaehlen = new JButton("Datei wählen");
		btnDateiAuswaehlen.addActionListener(e -> openFileChooser());
		oberesPanel.add(btnDateiAuswaehlen, BorderLayout.EAST);

		add(oberesPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

		JButton btnAbbrechen = new JButton("Abbrechen");
		btnAbbrechen.addActionListener(e -> returnToStart());
		buttonPanel.add(btnAbbrechen);

		JButton btnBestaetigen = new JButton("Bestätigen");
		btnBestaetigen.addActionListener(e -> confirmSelection());
		buttonPanel.add(btnBestaetigen);

		add(buttonPanel, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/*
	 * craet a fileChooser to select a file to save the Quellenverzeichnis
	 */
	private void openFileChooser() {
		JFileChooser fileChooser = new JFileChooser("/home/s3r4/Documents/Svault/Okul/IT/Projekt/Literaturverzeichnis/data/quellenData.json"); //NOTE: REMOVE AFTER TESTING!!!
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileNameExtensionFilter("JSON-Dateien (*.json)", "json"));

		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			chosenFile = fileChooser.getSelectedFile().toPath();
			lblVerzeichnis.setText(chosenFile.getFileName().toString());
			lblVerzeichnis.setToolTipText(chosenFile.toString());
		}
	}

	/*
	 * Open the start window
	 */
	private void returnToStart() {
		new GUI_Startfenster();
		dispose();
	}

	/*
	 * if user chose Literaturveruzeichnis to import -> open the Literaturverwaltung
	 */
	private void confirmSelection() {
    if (chosenFile == null) {JOptionPane.showMessageDialog(this, "Bitte zuerst eine JSON-Datei auswählen.");
        return;
    }

    try {
    	DataManager manager = new DataManager();
        manager.importJson(chosenFile);
        List<Literatur> sources = manager.readAllSources();
        GUI_Literaturverwaltung window = new GUI_Literaturverwaltung(manager);
        window.addTableRows(sources.toArray(Literatur[]::new));
        dispose();
    } catch (IOException e) { JOptionPane.showMessageDialog(this,"Die JSON-Datei konnte nicht importiert werden:\n" + e.getMessage(),"Import fehlgeschlagen",JOptionPane.ERROR_MESSAGE);
    }
}
}
