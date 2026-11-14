package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.nio.file.Path;

import model.data.data_manager;

public class GUI_Verzeichnis_Importieren extends JFrame {
	final String titel = "Verzeichnis importieren";
	private Path chosenFile;
	private final JLabel lblVerzeichnis = new JLabel(" <Verzeichnisname> ", SwingConstants.CENTER);

	public GUI_Verzeichnis_Importieren() {
		setTitle("Verzeichnis importieren");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));
		setSize(800, 800);
		((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

		JPanel oberesPanel = new JPanel(new GridLayout(1, 2, 10, 0));

		lblVerzeichnis.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lblVerzeichnis.setOpaque(true);
		lblVerzeichnis.setBackground(new Color(224, 224, 224));
		oberesPanel.add(lblVerzeichnis);

		JButton btnDateiAuswaehlen = new JButton("Datei wählen");
		btnDateiAuswaehlen.addActionListener(e -> openFileChooser());
		oberesPanel.add(btnDateiAuswaehlen);

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

	private void openFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(
				new FileNameExtensionFilter("JSON-Dateien (*.json)", "json"));

		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			chosenFile = fileChooser.getSelectedFile().toPath();
			lblVerzeichnis.setText(chosenFile.getFileName().toString());
			lblVerzeichnis.setToolTipText(chosenFile.toString());
		}
	}

	private void returnToStart() {
		new GUI();
		dispose();
	}

	private void confirmSelection() {
		if (chosenFile == null) {
			JOptionPane.showMessageDialog(this, "Bitte zuerst eine JSON-Datei auswählen.");
			return;
		}

		data_manager manager = new data_manager();
		manager.importJson(chosenFile);
		new GUI_Literaturverwaltung(manager);
		dispose();
	}
}
