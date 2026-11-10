package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

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
        oberesPanel.add(lblVerzeichnis);
        
        JButton btnDateiAuswaehlen = new JButton("Datei wählen"); //TODO: implement Action later
        oberesPanel.add(btnDateiAuswaehlen);
        
        this.add(oberesPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        
        JButton btnAbbrechen = new JButton("Abbrechen"); //TODO: implement Action later
        JButton btnBestätigen = new JButton("Bestätigen"); //TODO: implement Action later
        
        buttonPanel.add(btnAbbrechen);
        buttonPanel.add(btnBestätigen);
        
        this.add(buttonPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null); 
        setVisible(true);
	}
}
