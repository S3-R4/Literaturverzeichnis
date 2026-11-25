package controller;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;

import javax.swing.JOptionPane;

public class LiteraturRichtlinien {
	
	private static final File html = new File("assets/Literaturverzeichnis.html");
	
	/**
	 * Every slide on the html data "Richtlinien zur Zitation"
	 */
	public enum Seite {
		START,
		KURZBELEG,
		LANGBELEG,
		SAMMELBAND,
		ZEITSCHRIFT,
		INTERNET,
		AUDIOVISUEL	
	}
	
	/**
	 * decide on which slide the thml will be opened
	 * @param seitenname
	 */
	public static void openDoc(Seite seitenname) {
		String s = "";
		switch(seitenname) {
		case Seite.START: s = "start"; break; 
		case Seite.KURZBELEG: s = "kurzbeleg"; break; 
		case Seite.LANGBELEG: s = "langbeleg"; break; 
		case Seite.SAMMELBAND: s = "sammelband"; break; 
		case Seite.ZEITSCHRIFT: s = "zeitschrift"; break; 
		case Seite.INTERNET: s = "internet"; break; 
		case Seite.AUDIOVISUEL: s = "audiovisuel"; break; 
		}
		
		try {
			open(s);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Dokument wurde nicht gefunden!");
		}
	}
	
	/**
	 * open the html
	 * @throws Exception
	 */
	private static void openDoc() throws Exception {
		Desktop.getDesktop().browse(html.toURI());
	}
	
	/**
	 * !! does not work !!
	 * open the html on the given slide
	 * @param s
	 * @throws Exception
	 */
	private static void open(String s) throws Exception {
		String url = html.toURI().toString() + "#" + s;
		
		URI uri = new URI(url);

		Desktop.getDesktop().browse(uri);
	}
}
