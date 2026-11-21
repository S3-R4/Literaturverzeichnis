package model.Audiovisuell;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Objects;

/**
 * Represents a film or video citation, including its production company.
 */
public class FIlmsVideos extends Audiovisuell {
	private Optional<String> produktionsfirma = Optional.empty();


	/**
	 * Counts populated attributes used to measure source completeness.
	 *
	 * @return the number of populated relevant attributes
	 */
	@Override
	public int getAnzahlAusgefuellterAttribute() {
		return super.getAnzahlAusgefuellterAttribute() + (hatText(produktionsfirma) ? 1 : 0);
	}

	@Override
	/**
	 * Returns the number of attributes considered for source completeness.
	 *
	 * @return the total number of relevant attributes
	 */
	public int getAnzahlRelevanterAttribute() {
		return super.getAnzahlRelevanterAttribute() + 1;
	}

	@Override
	/**
	 * Formats a full bibliographic citation for this source.
	 *
	 * @return the full citation
	 */
	public String generiereLangbeleg() {
		if (getUrl().isPresent()) {
			return getAutorsString(autor) + ". " +
					datumFormatierung(veroeffentlichungsdatum) + ". " +
					getTitel().orElse("") + " [" +
					getBeitragsArt().orElse("") + "]. " +
					getProduktionsfirma().orElse("") + ". " +
					getUrl().orElse("") + ".";
		} else {
			return getAutorsString(autor) + ". " +
					datumFormatierung(veroeffentlichungsdatum) + ". " +
					getTitel().orElse("") + " [" +
					getBeitragsArt().orElse("") + "]. " +
					getProduktionsfirma().orElse("") + ".";
		}
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
				getTitel().orElse("") + " [";
	}

	/**
	 * Returns the production company.
	 *
	 * @return the production company
	 */
	public Optional<String> getProduktionsfirma() {
		return this.produktionsfirma;
	}

	/**
	 * Sets the production company.
	 *
	 * @param produktionsfirma production company
	 */
	public void setProduktionsfirma(Optional<String> produktionsfirma) {
		this.produktionsfirma = Objects.requireNonNull(produktionsfirma, "produktionsfirma");
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
