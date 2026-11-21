package model.Audiovisuell;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

/**
 * Represents a social media post and its citation metadata.
 */
public class SocialMediaBeitraege extends Audiovisuell {
	private Optional<String> text = Optional.empty();
	private Optional<String> beschreibung = Optional.empty();

	/**
	 * Counts populated attributes used to measure source completeness.
	 *
	 * @return the number of populated relevant attributes
	 */
	@Override
	public int getAnzahlAusgefuellterAttribute() {
		int anzahl = super.getAnzahlAusgefuellterAttribute();
		if (hatText(text)) anzahl++;
		if (hatText(beschreibung)) anzahl++;
		return anzahl;
	}

	@Override
	/**
	 * Returns the number of attributes considered for source completeness.
	 *
	 * @return the total number of relevant attributes
	 */
	public int getAnzahlRelevanterAttribute() {
		return super.getAnzahlRelevanterAttribute() + 2;
	}

	@Override
	/**
	 * Formats a full bibliographic citation for this source.
	 *
	 * @return the full citation
	 */
	public String generiereLangbeleg() {
		return getAutorsString(autor) + ". " +
				datumFormatierung(veroeffentlichungsdatum) + ". " +
				getTitel().orElse("") + ". [" +
				getBeitragsArt().orElse("") + "]. " +
				getMedium().orElse("") +
				getUrl().orElse("") + " " +
				getAbruffDatum().map(d -> d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).orElse("");
	}

	@Override
	/**
	 * Formats a short citation for this source.
	 *
	 * @return the short citation
	 */
	public String generiereKurzbeleg() {
		return getAutorsString(autor) + ". " +
				datumFormatierung(veroeffentlichungsdatum) + ". " +
				getTitel().orElse("");
	}

	@Override
	/**
	 * Formats a publication date according to this source type.
	 *
	 * @param datum date
	 * @return the formatted date, or an empty string when absent
	 */
	public String datumFormatierung(Optional<LocalDate> datum) {
		return datum.map(d -> "(" + d.getYear() + ")").orElse("");
	}

	/**
	 * Returns the text.
	 *
	 * @return the text
	 */
	public Optional<String> getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text text
	 */
	public void setText(Optional<String> text) {
		this.text = Objects.requireNonNull(text, "text");
	}

	/**
	 * Returns the description.
	 *
	 * @return the description
	 */
	public Optional<String> getBeschreibung() {
		return beschreibung;
	}

	/**
	 * Sets the description.
	 *
	 * @param beschreibung description
	 */
	public void setBeschreibung(Optional<String> beschreibung) {
		this.beschreibung = Objects.requireNonNull(beschreibung, "beschreibung");
	}

}
