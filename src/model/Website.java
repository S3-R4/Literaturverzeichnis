
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Website extends Literatur {
private String websitename;
private String url;
private String textart;

public Website(List<String> autor, String websitename, Optional<String> titel,String url, LocalDate abrufdatum,LocalDate veroeffentlichungsdatum, String textart) {

setAutoren(autor);
setWebsitename(websitename);
setTitel(titel.orElse(""));
setUrl(url);
this.abrufdatum = abrufdatum;
this.veroeffentlichungsdatum = veroeffentlichungsdatum;
this.textart = textart;
}

public void setAutoren(List<String> autoren) {
this.autor = new ArrayList<>();

if (autoren == null) return;

for (String autor : autoren) {
	if (autor != null && !autor.isBlank()) {
		this.autor.add(autor.trim());
	}
}
}

public void setWebsitename(String websitename) {
this.websitename = websitename == null ? "" : websitename.trim();
}

public void setUrl(String url) {
this.url = url == null ? "" : url.trim();
}

public void setTitel(String titel) {
this.titel = titel == null || titel.isBlank() ? null : titel.trim();
}

public String getWebsitename() {
return websitename;
}

public String getUrl() {
return url;
}

public String getTextart() {
return textart;
}

@Override
public String generiereLangbeleg() {
String textBaustein;

// Autor
if (autor == null || autor.isEmpty()) {
	textBaustein = "u. Autor ";
} else {
	textBaustein = String.join(", ", autor) + " ";
}

// Veröffentlichung
if (veroeffentlichungsdatum != null) {
	textBaustein += "(" + veroeffentlichungsdatum + "). ";
}

// Textart
if (textart != null && !textart.isBlank()) {
	textBaustein += textart + ": ";
}

// Titel
if (titel != null && !titel.isBlank()) {
	textBaustein += titel + ". ";
}

// Websitename
textBaustein += websitename + ". ";

// URL
textBaustein += url + " ";

// Aufrufdatum
if (abrufdatum != null) {
	textBaustein += "(" + abrufdatum + ").";
}

return textBaustein;
}

@Override
public String generiereKurzbeleg() {
	String textbaustein;

	// Autor
	if (autor == null || autor.isEmpty()) {
		textbaustein = websitename + " ";
	} else {
		textbaustein = String.join(", ", autor) + " ";
	}

	// Veröffentlichung
	if (veroeffentlichungsdatum != null) {
		textbaustein += "(" + veroeffentlichungsdatum + ")";
	}

	return textbaustein;
}

@Override
public int getAnzahlAusgefuellterAttribute() {
	int anzahl = 0;

	if (autor != null && autor.stream().anyMatch(a -> a != null && !a.isBlank())) {
		anzahl++;
	}

	if (titel != null && !titel.isBlank()) {
		anzahl++;
	}

	if (websitename != null && !websitename.isBlank()) {
		anzahl++;
	}

	if (url != null && !url.isBlank()) {
		anzahl++;
	}

	if (veroeffentlichungsdatum != null) {
		anzahl++;
	}

	if (abrufdatum != null) {
		anzahl++;
	}

	return anzahl;
}

@Override
public int getAnzahlRelevanterAttribute() {
	return 6;
}
}
