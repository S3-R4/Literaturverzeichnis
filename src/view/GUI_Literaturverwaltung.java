package view;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GUI_Literaturverwaltung extends JFrame {
	final String titel = "Literaturverwaltung - Literaturverzeichnis"; 
	
	public GUI_Literaturverwaltung() {
		setTitle(titel);
		setSize(600, 600);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;

		// ---- Zeile 1 ----
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		
		JPanel pnlFirstLine = new JPanel();
		add(pnlFirstLine, gbc);
		
		JButton btnDateiExportieren = new JButton("Datei exportieren");
		btnDateiExportieren.addActionListener(e -> dateiExportieren());
		pnlFirstLine.add(btnDateiExportieren);
		
		JButton btnEinstellungen = new JButton("Einstellungen");
		btnEinstellungen.addActionListener(e -> einstellungen());
		pnlFirstLine.add(btnEinstellungen);
		
		// Zeile 2
		gbc.gridy = 1;
		add(new JLabel("Literaturverzeichnis: " + fetchVerzeichnisName()), gbc);

		
		// unten links
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 3;
		gbc.weighty = 1;
		
		JPanel pnlVerwaltung = new JPanel(new GridBagLayout());
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.fill = GridBagConstraints.BOTH;
		add(pnlVerwaltung, gbc);
		
		// obere Zeile (75/25)

		gbc2.gridx = 0;
		gbc2.gridy = 0;
		gbc2.weightx = 3;
		gbc2.weighty = 0;

		pnlVerwaltung.add(new JTextField("Suchen..."), gbc2);

		gbc2.gridx = 1;
		gbc2.weightx = 1;

		pnlVerwaltung.add(new JLabel("<Platzhalter>"), gbc2); //TODO: implement JCombobox for selection of wanted quellen types

		// JTable über volle Breite

		gbc2.gridx = 0;
		gbc2.gridy = 1;
		gbc2.gridwidth = 2;
		gbc2.weightx = 1;
		gbc2.weighty = 1;
		
		String[] header = {"Nr.", "Titel", "Quellentyp", "Autor", "Jahr"};
				
		DefaultTableModel dtm = new DefaultTableModel(header, 0);
		JTable jtb = new JTable(dtm);
		JScrollPane scpView = new JScrollPane(jtb);
		
		pnlVerwaltung.add(scpView, gbc2);

		// Button über volle Breite

		gbc2.gridy = 2;
		gbc2.weighty = 0;

		JButton btnNeuerVerweis = new JButton("+ Neuen Verweis erstellen");
		btnNeuerVerweis.addActionListener(e -> neuerVerweis());
		pnlVerwaltung.add(btnNeuerVerweis, gbc);
				
		// unten rechts
		gbc.gridx = 1;
		gbc.weightx = 1;
		
		JPanel pnlQuellendetails = new JPanel();
		pnlQuellendetails.setLayout(new BoxLayout(pnlQuellendetails, BoxLayout.Y_AXIS));
		add(pnlQuellendetails, gbc);
		
		JLabel lblQuellendetials = new JLabel("Quellendetails");
		pnlQuellendetails.add(lblQuellendetials);
		
		JLabel lblQuellentyp = new JLabel("Quellentyp"); //TODO: combobox?
		pnlQuellendetails.add(lblQuellentyp);
		
		JLabel lblHerrausgeber = new JLabel("Autor / Herausgeber");
		pnlQuellendetails.add(lblHerrausgeber);
		
		JTextField tfdHerrausgeber = new JTextField("Autorenliste");
		pnlQuellendetails.add(tfdHerrausgeber);
		
		JLabel lblErscheinungsjahr = new JLabel("Erscheinungsjahr");
		pnlQuellendetails.add(lblErscheinungsjahr);
		
		JTextField tfdErscheinungsjahr = new JTextField("2000");
		pnlQuellendetails.add(tfdErscheinungsjahr);
		
		JLabel lblVerlag = new JLabel("Verlag / Fundstelle");
		pnlQuellendetails.add(lblVerlag);
		
		JTextField tfdVerlag = new JTextField("Berliner");
		pnlQuellendetails.add(tfdVerlag);
		
		JTextArea txaVorschau = new JTextArea("Autor: 2000, Berliner");
		pnlQuellendetails.add(txaVorschau);
		
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void dateiExportieren() { //TODO: implement code
		;
	}
	
	private void einstellungen() { //TODO: implement code
		;
	}
	
	private String fetchVerzeichnisName() { //TODO: implement code
		return "";
	}
	
	private void neuerVerweis() { //TODO: implement code
		;
	}
}
