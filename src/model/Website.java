
package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Represents a web source with publication and retrieval dates.
 */
public class Website extends Literatur {
private static final DateTimeFormatter VEROEFFENTLICHUNGSFORMAT =
		DateTimeFormatter.ofPattern("yyyy, d. MMMM", Locale.GERMAN);
private static final DateTimeFormatter ABRUFFORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

private String websitename;
private String url;
private String textart;

/**
 * Creates a Website instance with the supplied metadata.
 *
 * @param autor author list
 * @param websitename website name
 * @param titel title
 * @param url URL
 * @param abrufdatum retrieval date
 * @param veroeffentlichungsdatum publication date
 * @param textart text category
 */
public Website(List<String> autor, String websitename, Optional<String> titel,String url, LocalDate abrufdatum,LocalDate veroeffentlichungsdatum, String textart) {

setAutoren(autor);
setWebsitename(websitename);
setTitel(titel);
setUrl(url);
this.abrufdatum = Optional.ofNullable(abrufdatum);
this.veroeffentlichungsdatum = Optional.ofNullable(veroeffentlichungsdatum);
this.textart = textart;
}

/**
 * Sets the author list.
 *
 * @param autoren author list
 */
public void setAutoren(List<String> autoren) {
this.autor = new ArrayList<>();

if (autoren == null) return;

for (String autor : autoren) {
	if (autor != null && !autor.isBlank()) {
		this.autor.add(autor.trim());
	}
}
}

/**
 * Sets the website name.
 *
 * @param websitename website name
 */
public void setWebsitename(String websitename) {
this.websitename = websitename == null ? "" : websitename.trim();
}

/**
 * Sets the URL.
 *
 * @param url URL
 */
public void setUrl(String url) {
this.url = url == null ? "" : url.trim();
}

/**
 * Sets the title.
 *
 * @param titel title
 */
public void setTitel(Optional<String> titel) {
this.titel = java.util.Objects.requireNonNull(titel).filter(v -> !v.isBlank()).map(String::trim);
}

/**
 * Returns the website name.
 *
 * @return the website name
 */
public String getWebsitename() {
return websitename;
}

/**
 * Returns the URL.
 *
 * @return the URL
 */
public String getUrl() {
return url;
}

/**
 * Returns the text category.
 *
 * @return the text category
 */
public String getTextart() {
return textart;
}

/**		ArrayList<String> autor = new ArrayList<String>(List.of("Mole, B."));
		String websitename = "Ars Technica";
		String titel = "mRNA vaccines are in RFK Jr’s crosshairs; funding in question";
		String url = "https://arstechnica.com/health/2025/03/federal-funding-formrna-vaccine-research-in-jeopardy-under-rfk-jr/";
		LocalDate aufrufdatum = LocalDate.of(2025, 3, 19);
		LocalDate veroeffentlichungsdatum = LocalDate.of(2025, 3, 17);
		String textart = "Report";
		Website w = new Website(autor, websitename, Optional.ofNullable(titel), url, aufrufdatum,
				veroeffentlichungsdatum, textart);

		assertEquals("Mole, B. (2025, 17. März)", w.generiereKurzbeleg());
 * Formats a full bibliographic citation for this source.
 *
 * @return the full citation
 */
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
/**
 * Formats a short citation for this source.
 *
 * @return the short citation
 */
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
/**
 * Counts populated attributes used to measure source completeness.
 *
 * @return the number of populated relevant attributes
 */
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
/**
 * Returns the number of attributes considered for source completeness.
 *
 * @return the total number of relevant attributes
 */
public int getAnzahlRelevanterAttribute() {
	return 6;
}

/**
 * Returns the publication date.
 *
 * @return the publication date
 */
public String getVeroeffentlichungsDatum() {
	return veroeffentlichungsdatum.map(VEROEFFENTLICHUNGSFORMAT::format).orElse("");
}

/**
 * Returns the retrieval date.
 *
 * @return the retrieval date
 */
public String getAbrufDatum() {
	return abrufdatum.map(ABRUFFORMAT::format).orElse("");
}
}
