package model;

import java.time.LocalDate;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Stores newspaper article metadata and formats article citations.
 */
public class Zeitungsartikel extends Literatur {

	String zeitung;
	String seitenzahl;
	String url;

	/**
	 * Creates a Zeitungsartikel instance with the supplied metadata.
	 *
	 * @param veroeffentlichungsdatum publication date
	 * @param titel title
	 * @param zeitung newspaper name
	 * @param seitenzahl page information
	 * @param url URL
	 * @param autor author list
	 */
	public Zeitungsartikel(LocalDate veroeffentlichungsdatum, String titel, String zeitung, String seitenzahl,
			String url, List<String> autor) {
		setAutor(autor == null ? List.of() : autor);
		setDatum(veroeffentlichungsdatum);
		setTitel(Optional.ofNullable(titel));
		setZeitung(zeitung);
		setSeitenzahl(seitenzahl);
		setUrl(url);
	}

	/**
	 * Returns the date.
	 *
	 * @return the date
	 */
	public String getDatum() {
		// Definition des gewünschten Formats und der deutschen Sprache
        DateTimeFormatter datumstransformator = DateTimeFormatter.ofPattern("yyyy, d.MMMM", Locale.GERMAN);
        
        // Formatierung anwenden
        String transformiertesDatum = veroeffentlichungsdatum.map(d -> d.format(datumstransformator)).orElse("");
		return transformiertesDatum;
	}

	/**
	 * Sets the date.
	 *
	 * @param datum date
	 */
	public void setDatum(java.time.LocalDate datum) {
		this.veroeffentlichungsdatum = Optional.ofNullable(datum);
	}

	/**
	 * Returns the title.
	 *
	 * @return the title
	 */
	public Optional<String> getTitel() {
		return titel;
	}

	/**
	 * Sets the title.
	 *
	 * @param titel title
	 */
	public void setTitel(Optional<String> titel) {
		this.titel = java.util.Objects.requireNonNull(titel);
	}

	/**
	 * Returns the newspaper name.
	 *
	 * @return the newspaper name
	 */
	public String getZeitung() {
		return zeitung;
	}

	/**
	 * Sets the newspaper name.
	 *
	 * @param zeitung newspaper name
	 */
	public void setZeitung(String zeitung) {
		this.zeitung = zeitung;
	}

	/**
	 * Returns the page information.
	 *
	 * @return the page information
	 */
	public String getSeitenzahl() {
		return seitenzahl;
	}

	/**
	 * Sets the page information.
	 *
	 * @param seitenzahl page information
	 */
	public void setSeitenzahl(String seitenzahl) {
		this.seitenzahl = seitenzahl;
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
	 * Sets the URL.
	 *
	 * @param url URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Abbreviates authors for short citations, falling back to the newspaper or title.
	 *
	 * @return the short author label
	 */
	private String getAutorAsStringKurz() {
		String s = "";

		// Autor Konventionen
		switch (getAutor().size()) {
			case 0:
				break;
			case 1:
				s += getAutor().get(0);
				break;
			case 2:
				s += getAutor().get(0) + "&" + getAutor().get(1);
				break;
			default:
				s += getAutor().get(0) + " et al.";
		}

		if (s.equals(""))
			if (getZeitung().equals(""))
				s += getTitel().orElse("");
			else
				s += getZeitung();

		return s;
	}

	/**
	 * Formats up to three authors, falling back to the newspaper or title.
	 *
	 * @return the full author label
	 */
	private String getAutorAsStringLang() {
		String s = "";
		switch (getAutor().size()) {
			case 0:
				break;
			case 1:
				s += getAutor().get(0);
				break;
			case 2:
				s += getAutor().get(0) + ", " + getAutor().get(1);
				break;
			case 3:
				s += getAutor().get(0) + ", " + getAutor().get(1) + ", " + getAutor().get(2);
				break;
			default:
				s += getAutor().get(0) + ", " + getAutor().get(1) + ", " + getAutor().get(2) + ", et al.";
		}

		if (s.equals(""))
			if (getZeitung().equals(""))
				s += getTitel().orElse("");
			else
				s += getZeitung();

		return s;
	}

	/**
	 * Formats a short citation for this source.
	 *
	 * @return the short citation
	 */
	@Override
	public String generiereKurzbeleg() {
		String b = "(";

		b += getAutorAsStringKurz();

		b += ", " + getDatum();

		if (!getSeitenzahl().equals(""))
			b += ", " + getSeitenzahl();

		b += ")";
		return b;
	}

	@Override
	/**
	 * Formats a full bibliographic citation for this source.
	 *
	 * @return the full citation
	 */
	public String generiereLangbeleg() {
		String b = "";

		b += getAutorAsStringLang();

		b += " (" + getDatum() + "). ";

		if (!titel.orElse("").equals(""))
			b += titel.orElse("") + ". ";
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
@Override
/**
 * Counts populated attributes used to measure source completeness.
 *
 * @return the number of populated relevant attributes
 */
public int getAnzahlAusgefuellterAttribute() {
int anzahl = 0;

if (autor.stream().anyMatch(a -> a != null && !a.isBlank())) anzahl++;
if (veroeffentlichungsdatum.isPresent()) anzahl++;
if (titel.filter(v -> !v.isBlank()).isPresent()) anzahl++;
if (zeitung != null && !zeitung.isBlank()) anzahl++;
if (seitenzahl != null && !seitenzahl.isBlank()) anzahl++;
if (url != null && !url.isBlank()) anzahl++;

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
}
