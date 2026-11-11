package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GUI_Neues_Verzeichnis extends JFrame {
	final String titel = "Neues Verzeichnis anlegen"; 
	
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
		JTextField tfdVerzeichnisname = new JTextField(25);
		tfdVerzeichnisname.setText("<Verzeichnisname>");
		add(tfdVerzeichnisname, gbc);

		// --- Zeile 2: Speicherort ---
		gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
		add(new JLabel("Speicherort:"), gbc);

		gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
		JPanel pathPanel = new JPanel(new BorderLayout(5, 0));
		JTextField tfdDateipfad = new JTextField("<Dateipfad>");
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
	
	private void returnToStart() {
		new GUI();
		dispose();
	}
	
	private void confirmSelection() { //TODO: implement Action later
		;
	}
	
	private void openFileChooser() { //TODO: implement Action later
		;
	}
}
