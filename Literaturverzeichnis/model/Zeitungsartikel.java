package zeitung;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Zeitungsartikel {
	
	ArrayList<String> autor;
	String datum;
	String titel;
	String zeitung;
	String seitenzahl;
	String url;
	
	/**
	 * Erstellt neue Quelle für einen Zeitungsartikel
	 * @param autor
	 * @param datum
	 * @param titel
	 * @param zeitung
	 * @param seitenzahl
	 * @param url
	 */
	public Zeitungsartikel(String datum, String titel, String zeitung, String seitenzahl, String url, String... autor) {
		setAutor(new ArrayList<String>(Arrays.asList(autor)));
		setDatum(datum);
		setTitel(titel);
		setZeitung(zeitung);
		setSeitenzahl(seitenzahl);
		setUrl(url);
	}
	
	public ArrayList<String> getAutor() {
		return autor;
	}

	public void setAutor(ArrayList<String> autor) {
		this.autor = autor;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getZeitung() {
		return zeitung;
	}

	public void setZeitung(String zeitung) {
		this.zeitung = zeitung;
	}

	public String getSeitenzahl() {
		return seitenzahl;
	}

	public void setSeitenzahl(String seitenzahl) {
		this.seitenzahl = seitenzahl;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	private String getAutorAsStringKurz() {
		String s = "";
	
		// Autor Konventionen
		switch (getAutor().size()) {
		case 0: break;
		case 1: s += getAutor().get(0); break;
		case 2: s += getAutor().get(0) + "&" + getAutor().get(1); break;
		default: s += getAutor().get(0) + " et al.";
		}
		
		if (s.equals("")) 
			if (getZeitung().equals(""))
				s += getTitel();
			else
				s += getZeitung();
		
		return s;
	}
	
	private String getAutorAsStringLang() {
		String s = "";
		switch (getAutor().size()) {
		case 0: break;
		case 1: s += getAutor().get(0); break;
		case 2: s += getAutor().get(0) + ", " + getAutor().get(1); break;
		case 3: s += getAutor().get(0) + ", " + getAutor().get(1) + ", " + getAutor().get(2); break;
		default: s += getAutor().get(0) + ", " + getAutor().get(1) + ", " + getAutor().get(2) + ", et al.";
		}
		
		if (s.equals("")) 
			if (getZeitung().equals(""))
				s += getTitel();
			else
				s += getZeitung();
		
		return s;
	}

	public String getLangerBeleg() {
		String b = "";
	
		b += getAutorAsStringLang();
			
		b += " (" + getDatum() + "). ";
		
		if (!titel.equals(""))
			b += titel + ". ";
		else 
			b += "[o. T.]. ";
		
		if (!zeitung.equals(""))
			b += zeitung;
		else
			b += "[o. Z.]";
		
		if (!seitenzahl.equals(""))
			b += ". " + seitenzahl;
		else if (!url.equals(""))
			b += ". " + url;
		return b;
	}
	
	public String getKurzerBeleg() {
		String b = "(";
		
		b += getAutorAsStringKurz();
			
		b += ", " + getDatum();
		
		if (!getSeitenzahl().equals(""))
			b += ", " + getSeitenzahl();

				
		b += ")";
		return b;
	}
}
