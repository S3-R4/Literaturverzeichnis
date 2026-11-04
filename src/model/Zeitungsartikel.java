package src.model;



import java.util.List;

public class Zeitungsartikel extends Literatur {
	
	String zeitung;
	String seitenzahl;
	String url;
	
	/**
	 * Erstellt neue Quelle f�r einen Zeitungsartikel
	 * @param autor
	 * @param datum
	 * @param titel
	 * @param zeitung
	 * @param seitenzahl
	 * @param url
	 */
	public Zeitungsartikel(java.time.LocalDate veroeffentlichungsdatum, String titel, String zeitung, String seitenzahl, String url,List<String>  autor) {
		setAutor(autor);
		setDatum(veroeffentlichungsdatum);
		setTitel(titel);
		setZeitung(zeitung);
		setSeitenzahl(seitenzahl);
		setUrl(url);
	}
	
	public List<String> getAutor() {
		return autor;
	}

	public void setAutor(List<String> autor) {
		this.autor = autor;
	}

	public java.time.LocalDate getDatum() {
		return veroeffentlichungsdatum;
	}

	public void setDatum(java.time.LocalDate datum) {
		this.veroeffentlichungsdatum = datum;
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


	@Override
	String generiereKurzbeleg() {
		String b = "(";
		
		b += getAutorAsStringKurz();
			
		b += ", " + getDatum();
		
		if (!getSeitenzahl().equals(""))
			b += ", " + getSeitenzahl();

				
		b += ")";
		return b;
	}

	@Override
	String generiereLangbeleg() {
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
}
