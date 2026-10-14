package work;

public class Website {
	String autor, websitename, titel, url, aufrufdatum, veroeffentlichungsdatum, textart;
	
	public Website(String autor, String websitename, String titel, String url, String aufrufdatum, String veroeffentlichungsdatum, String textart) {
		this.autor = autor;
		this.websitename = websitename;
		this.titel = titel;
		this.url = url;
		this.aufrufdatum = aufrufdatum;
		this.veroeffentlichungsdatum = veroeffentlichungsdatum;
		this.textart = textart;
	}
	
	public String generiereLangbeleg() {
		String textBaustein;
		//Autor
		if (this.autor.equals("")) {
			textBaustein = "u. Autor ";
		} else {
			textBaustein = this.autor + " ";
		}
		//Veroeffentlichung
		textBaustein += "("+this.veroeffentlichungsdatum+"). ";
		//Textart
		
		if (!textart.equals("")) {
			textBaustein += this.textart+": ";
		}
		//Titel
		textBaustein += this.titel+". ";
		//Websitename
		textBaustein += this.websitename+". ";
		//URL
		textBaustein += this.url+" ";
		//Aufrufdatum
		textBaustein += "("+this.aufrufdatum+").";
		
		return textBaustein;
	}
	
	public String generiereKurzerBeleg() {
		//Autor
		String textbaustein;
		if (this.autor.equals("")) {
			textbaustein = this.websitename + " ";
		} else {
			textbaustein = this.autor + " ";
		}
		//Veroeffentlichung
		textbaustein += "("+this.veroeffentlichungsdatum+")";
		return textbaustein;
	}
}
