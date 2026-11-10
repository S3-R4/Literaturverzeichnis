package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

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
        setSize(600, 280);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0,2,10,10));
        
        JLabel lblVerzeichnis = new JLabel("Verzeichnisname");
        lblVerzeichnis.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lblVerzeichnis);
        
        JTextField tfdVerzeichnisname = new JTextField(" <Vereichnisname> ");
        this.add(tfdVerzeichnisname);
        
        JButton btnSpreicherort = new JButton("Speicherort wählen"); //TODO: implement Action later
        this.add(btnSpreicherort);
        
        JLabel lblDateipfad = new JLabel(" <Dateipfad> ");
        this.add(lblDateipfad);
        
        // Gap
        this.add(new JLabel());
        
        JPanel pnlHolder = new JPanel();
        pnlHolder.setLayout(new GridLayout(0,2));
        
        JButton btnAbbrechen = new JButton("Abbrechen"); //TODO: implement Action later
        pnlHolder.add(btnAbbrechen);
        
        JButton btnBestätigen = new JButton("Bestätigen"); //TODO: implement Action later
        pnlHolder.add(btnBestätigen);

        this.add(pnlHolder);
        
        setVisible(true);
	}
}
