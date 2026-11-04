package view;

import javax.swing.*;

import model.Sammelbandbeleg;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SammelbandBeitragGUI extends JFrame {
	private JTextField txtAutoren;
	private JTextField txtErscheinungsjahr;
	private JTextField txtBeitragstitel;
	private JTextField txtHerausgeber;
	private JTextField txtSammelbandtitel;
	private JTextField txtAuflage;
	private JTextField txtVerlag;
	private JTextField txtOrt;
	private JTextField txtAnfang;
	private JTextField txtEnde;
	private JTextArea txtResultat;

	public SammelbandBeitragGUI() {

		setTitle("Sammelband-Beitrag Zitations-Generator");
		setSize(600, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		JPanel panelEingabe = new JPanel(new GridLayout(10, 2, 5, 5));
		panelEingabe.setBorder(BorderFactory.createTitledBorder("Eingabefelder"));

		txtAutoren = feld(panelEingabe, "Autor(en):");
		txtErscheinungsjahr = feld(panelEingabe, "Erscheinungsjahr:");
		txtBeitragstitel = feld(panelEingabe, "Beitragstitel:");
		txtHerausgeber = feld(panelEingabe, "Herausgeber:");
		txtSammelbandtitel = feld(panelEingabe, "Sammelbandtitel:");
		txtAuflage = feld(panelEingabe, "Auflage:");
		txtAnfang = feld(panelEingabe, "Anfangsseite:");
		txtEnde = feld(panelEingabe, "Endseite:");
		txtOrt = feld(panelEingabe, "Erscheinungsort:");
		txtVerlag = feld(panelEingabe, "Verlag:");
		add(panelEingabe, BorderLayout.NORTH);
		JButton btnGenerieren = new JButton("Beleg generieren");
		btnGenerieren.addActionListener(e -> generiereBeleg());
		add(btnGenerieren, BorderLayout.CENTER);
		JPanel panelAusgabe = new JPanel(new BorderLayout());
		panelAusgabe.setBorder(BorderFactory.createTitledBorder("Langbeleg"));
		txtResultat = new JTextArea(6, 40);
		txtResultat.setEditable(false);
		txtResultat.setLineWrap(true);
		txtResultat.setWrapStyleWord(true);
		panelAusgabe.add(new JScrollPane(txtResultat));
		add(panelAusgabe, BorderLayout.SOUTH);
	}

	private JTextField feld(JPanel panel, String beschriftung) {
		panel.add(new JLabel(beschriftung));
		JTextField textfeld = new JTextField();
		panel.add(textfeld);
		return textfeld;
	}

	private Optional<String> optionalText(String text) {
		return text.isBlank() ? Optional.empty() : Optional.of(text.trim());
	}

	private Optional<Integer> optionalZahl(String text) {
		return text.isBlank() ? Optional.empty() : Optional.of(Integer.parseInt(text.trim()));
	}

	private List<String> liste(String text) {
		return Arrays.asList(text.split(","));
	}

	private void generiereBeleg() {

		try {
			Sammelbandbeleg beitrag = new Sammelbandbeleg(liste(txtAutoren.getText()),
					optionalZahl(txtErscheinungsjahr.getText()), optionalText(txtBeitragstitel.getText()),
					liste(txtHerausgeber.getText()), optionalText(txtSammelbandtitel.getText()),
					optionalZahl(txtAuflage.getText()), optionalText(txtVerlag.getText()),
					optionalText(txtOrt.getText()), optionalZahl(txtAnfang.getText()), optionalZahl(txtEnde.getText()));

			String belegLang = beitrag.getBelegLang();

			txtResultat.setText(belegLang);

		} catch (NumberFormatException ex) {

			JOptionPane.showMessageDialog(this, "Jahr, Auflage und Seitenzahlen müssen Zahlen sein!", "Eingabefehler",
					JOptionPane.ERROR_MESSAGE);

		} catch (IllegalArgumentException ex) {

			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new SammelbandBeitragGUI().setVisible(true));
	}
}