package model.Audiovisuell;

import java.time.LocalDate;
import java.util.Optional;
import java.time.format.DateTimeFormatter;

/**
 * Formats short and full citations for YouTube sources.
 */
public class Youtube extends Audiovisuell {
	/**
	 * Formats a full bibliographic citation for this source.
	 *
	 * @return the full citation
	 */
	@Override
	public String generiereLangbeleg() {
		return getAutorsString(autor) + ". " +
				datumFormatierung(veroeffentlichungsdatum) + ". " +
				getTitel().orElse("") + ". [" +
				getBeitragsArt().orElse("") + "]. " +
				getMedium().orElse("") + ". " +
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

}
