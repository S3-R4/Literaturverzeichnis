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

import model.data.data_manager;

public class GUI_Verzeichnis_Importieren extends JFrame {
	final String titel = "Verzeichnis importieren"; 
	
	public GUI_Verzeichnis_Importieren() {
		setTitle(titel);
        setSize(400, 150);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
        JPanel oberesPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        JLabel lblVerzeichnis = new JLabel(" <Verzeichnisname> ", SwingConstants.CENTER);
        lblVerzeichnis.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        lblVerzeichnis.setOpaque(true);
        lblVerzeichnis.setBackground(new Color(224,224,224));
        oberesPanel.add(lblVerzeichnis);
        
        JButton btnDateiAuswaehlen = new JButton("Datei wählen");
        btnDateiAuswaehlen.addActionListener(e -> openFileChooser());
        oberesPanel.add(btnDateiAuswaehlen);
        
        this.add(oberesPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        JButton btnAbbrechen = new JButton("Abbrechen");
        btnAbbrechen.addActionListener(e -> returnToStart());
        
        JButton btnBestaetigen = new JButton("Bestätigen");
        btnBestaetigen.addActionListener(e -> confirmSelection());
        
        buttonPanel.add(btnAbbrechen);
        buttonPanel.add(btnBestaetigen);
        
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null); 
        setVisible(true);
	}
	
	private void openFileChooser() { //TODO: implement Action later
		JFileChooser filechooser = new JFileChooser();
		
		int result = filechooser.showOpenDialog(null);
		
		if(result == JFileChooser.APPROVE_OPTION) {
			//TODO: filechooser.getAbsolutePath(); to data_manager
		} else {
			new JOptionPane("Unexpected Error");
		}
	}
	
	private void returnToStart() {
		new GUI();
		dispose();
	}
	
	private void confirmSelection() { //TODO: implement Action later
		new GUI_Literaturverwaltung();
		dispose();
		; // should load the exisiting data in the filepath or what ever
	}
}
