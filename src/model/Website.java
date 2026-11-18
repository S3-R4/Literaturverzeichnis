
package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Website extends Literatur {
private static final DateTimeFormatter VEROEFFENTLICHUNGSFORMAT =
		DateTimeFormatter.ofPattern("yyyy, d. MMMM", Locale.GERMAN);
private static final DateTimeFormatter ABRUFFORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

private String websitename;
private String url;
private String textart;

public Website(List<String> autor, String websitename, Optional<String> titel,String url, LocalDate abrufdatum,LocalDate veroeffentlichungsdatum, String textart) {

setAutoren(autor);
setWebsitename(websitename);
setTitel(titel);
setUrl(url);
this.abrufdatum = Optional.ofNullable(abrufdatum);
this.veroeffentlichungsdatum = Optional.ofNullable(veroeffentlichungsdatum);
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

public void setTitel(Optional<String> titel) {
this.titel = java.util.Objects.requireNonNull(titel).filter(v -> !v.isBlank()).map(String::trim);
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
if (autor.isEmpty()) {
	textBaustein = "u. Autor ";
} else {
	textBaustein = String.join(", ", autor) + " ";
}

// Veröffentlichung
if (veroeffentlichungsdatum.isPresent()) {
	textBaustein += "(" + getVeroeffentlichungsDatum() + "). ";
}

// Textart
if (textart != null && !textart.isBlank()) {
	textBaustein += textart + ": ";
}

// Titel
if (titel.filter(v -> !v.isBlank()).isPresent()) {
	textBaustein += titel.orElse("") + ". ";
}

// Websitename
textBaustein += websitename + ". ";

// URL
textBaustein += url + " ";

// Aufrufdatum
if (abrufdatum.isPresent()) {
	textBaustein += "(" + getAbrufDatum() + ").";
}

return textBaustein;
}

@Override
public String generiereKurzbeleg() {
	String textbaustein;

	// Autor
	if (autor.isEmpty()) {
		textbaustein = websitename + " ";
	} else {
		textbaustein = String.join(", ", autor) + " ";
	}

	// Veröffentlichung
	if (veroeffentlichungsdatum.isPresent()) {
		textbaustein += "(" + getVeroeffentlichungsDatum() + ")";
	}

	return textbaustein;
}

@Override
public int getAnzahlAusgefuellterAttribute() {
	int anzahl = 0;

	if (autor.stream().anyMatch(a -> a != null && !a.isBlank())) {
		anzahl++;
	}

	if (titel.filter(v -> !v.isBlank()).isPresent()) {
		anzahl++;
	}

	if (websitename != null && !websitename.isBlank()) {
		anzahl++;
	}

	if (url != null && !url.isBlank()) {
		anzahl++;
	}

	if (veroeffentlichungsdatum.isPresent()) {
		anzahl++;
	}

	if (abrufdatum.isPresent()) {
		anzahl++;
	}

	return anzahl;
}

@Override
public int getAnzahlRelevanterAttribute() {
	return 6;
}

public String getVeroeffentlichungsDatum() {
	return veroeffentlichungsdatum.map(VEROEFFENTLICHUNGSFORMAT::format).orElse("");
}

public String getAbrufDatum() {
	return abrufdatum.map(ABRUFFORMAT::format).orElse("");
}
}
