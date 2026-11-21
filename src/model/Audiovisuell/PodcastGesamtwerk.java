package model.Audiovisuell;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

/**
 * Represents a complete podcast series and its publishing organization.
 */
public class PodcastGesamtwerk extends Audiovisuell {

	private Optional<String> herausgebendeOrganisation = Optional.empty();

	/**
	 * Counts populated attributes used to measure source completeness.
	 *
	 * @return the number of populated relevant attributes
	 */
	@Override
	public int getAnzahlAusgefuellterAttribute() {
		return super.getAnzahlAusgefuellterAttribute() + (hatText(herausgebendeOrganisation) ? 1 : 0);
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
		return getAutorsString(autor) + ". " + datumFormatierung(veroeffentlichungsdatum) + ". [" + getTitel().orElse("") + "]. "
				+ getHerausgebendeOrganisation().orElse("") + ". " + getUrl().orElse("") + " "
				+ getAbruffDatum().map(d -> d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).orElse("");
	}

	@Override
	/**
	 * Formats a short citation for this source.
	 *
	 * @return the short citation
	 */
	public String generiereKurzbeleg() {
		return getAutorsString(autor) + ". " + datumFormatierung(veroeffentlichungsdatum) + ". [" + getTitel().orElse("") + "]";
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
	 * Returns the publishing organization.
	 *
	 * @return the publishing organization
	 */
	public Optional<String> getHerausgebendeOrganisation() {
		return herausgebendeOrganisation;
	}

	/**
	 * Sets the publishing organization.
	 *
	 * @param herausgebendeOrganisation publishing organization
	 */
	public void setHerausgebendeOrganisation(Optional<String> herausgebendeOrganisation) {
		this.herausgebendeOrganisation = Objects.requireNonNull(herausgebendeOrganisation, "herausgebendeOrganisation");
	}

}
