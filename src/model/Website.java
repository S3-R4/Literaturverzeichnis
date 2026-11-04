package model;

import java.util.List;

public class Website extends Literatur {
	String websitename, url, textart;
	
	public Website(List<String> autor, String websitename, String titel, String url, java.time.LocalDate abrufdatum, java.time.LocalDate veroeffentlichungsdatum, String textart) {
		this.autor = autor;
		this.websitename = websitename;
		this.titel = titel;
		this.url = url;
		this.abrufdatum = abrufdatum;
		this.veroeffentlichungsdatum = veroeffentlichungsdatum;
		this.textart = textart;
	}
	
	@Override
	public String generiereLangbeleg() {
		String textBaustein;
		//Autor
		if (this.autor.toString().equals("")) {
			textBaustein = "u. Autor ";
		} else {
			textBaustein = this.autor.toString() + " ";
		}
		//Veroeffentlichung
		textBaustein += "("+this.veroeffentlichungsdatum.toString()+"). ";
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
		textBaustein += "("+this.abrufdatum.toString()+").";
		
		return textBaustein;
	}
	

	@Override
	public String generiereKurzbeleg() {
		//Autor
				String textbaustein;
				if (this.autor.toString().equals("")) {
					textbaustein = this.websitename + " ";
				} else {
					textbaustein = this.autor.toString() + " ";
				}
				//Veroeffentlichung
				textbaustein += "("+this.veroeffentlichungsdatum.toString()+")";
				return textbaustein;
	}
}
