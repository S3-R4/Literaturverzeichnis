package model.Audiovisuell;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Objects;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Represents a podcast episode with its number, title, and producers.
 */
public class Podcastfolgen extends Audiovisuell {
	private Optional<String> herausgebendeOrganisation = Optional.empty();
	private Optional<Integer> podcastFolge = Optional.empty();
	private Optional<String> podcastFolgenName = Optional.empty();

	/**
	 * Counts populated attributes used to measure source completeness.
	 *
	 * @return the number of populated relevant attributes
	 */
	@Override
	public int getAnzahlAusgefuellterAttribute() {
		int anzahl = super.getAnzahlAusgefuellterAttribute();
		if (hatText(herausgebendeOrganisation)) anzahl++;
		if (podcastFolge.filter(folge -> folge > 0).isPresent()) anzahl++;
		if (hatText(podcastFolgenName)) anzahl++;
		return anzahl;
	}

	@Override
	/**
	 * Returns the number of attributes considered for source completeness.
	 *
	 * @return the total number of relevant attributes
	 */
	public int getAnzahlRelevanterAttribute() {
		return super.getAnzahlRelevanterAttribute() + 3;
	}

	/**
	 * Creates a Podcastfolgen instance.
	 */
	public Podcastfolgen() {
		setBeitragsArt(Optional.of("Podcast-Folge"));
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

	/**
	 * Returns the episode number.
	 *
	 * @return the episode number
	 */
	public Optional<Integer> getPodcastFolge() {
		return podcastFolge;
	}

	/**
	 * Sets the episode number.
	 *
	 * @param podcastFolge episode number
	 */
	public void setPodcastFolge(Optional<Integer> podcastFolge) {
		this.podcastFolge = Objects.requireNonNull(podcastFolge, "podcastFolge");
	}

	/**
	 * Appends a producer to the author list and normalizes the resulting names.
	 *
	 * @param produzent producer name
	 */
	public void addProduzent(String produzent) {
		List<String> produzenten = new ArrayList<>(getAutor());
		produzenten.add(produzent);
		setAutor(produzenten);
	}

	/**
	 * Removes the producer at the specified author-list index.
	 *
	 * @param index zero-based index
	 */
	public void removeProduzent(int index) {
		getAutor().remove(index);
	}

	@Override
	/**
	 * Formats a full bibliographic citation for this source.
	 *
	 * @return the full citation
	 */
	public String generiereLangbeleg() {
		String autorenString = generiereAutorenString();
		String langbeleg = autorenString
				+ (autorenString.endsWith(".") ? " " : ". ")
				+ datumFormatierung(veroeffentlichungsdatum) + ". "
				+ "Folge #" + podcastFolge.map(String::valueOf).orElse("") + " - " + getPodcastFolgenName().orElse("") + ". "
				+ "[" + getBeitragsArt().orElse("") + "]. "
				+ "In: " + getTitel().orElse("");

		if (herausgebendeOrganisation.filter(wert -> !wert.isBlank()).isPresent()) {
			langbeleg += " " + herausgebendeOrganisation.orElse("");
		}

		return langbeleg + ". " + getUrl().orElse("") + " ("
				+ abrufdatum.map(d -> d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN))).orElse("")
				+ ").";
	}

	@Override
	/**
	 * Formats a short citation for this source.
	 *
	 * @return the short citation
	 */
	public String generiereKurzbeleg() {
		String autorenString = generiereAutorenString();
		return autorenString + (autorenString.endsWith(".") ? " " : ". ")
				+ datumFormatierung(veroeffentlichungsdatum) + " Folge #" + podcastFolge.map(String::valueOf).orElse("");
	}

	@Override
	/**
	 * Formats a publication date according to this source type.
	 *
	 * @param datum date
	 * @return the formatted date, or an empty string when absent
	 */
	public String datumFormatierung(Optional<LocalDate> datum) {
		return datum.map(d -> "(" + d.format(DateTimeFormatter.ofPattern("yyyy, d, MMMM", Locale.GERMAN)) + ")").orElse("");
	}

	/**
	 * Joins episode producers with commas and an ampersand before the last name.
	 *
	 * @return the formatted producers, or an empty string
	 */
	private String generiereAutorenString() {
		List<String> autoren = getAutor();
		if (autoren.isEmpty()) {
			return "";
		}
		if (autoren.size() == 1) {
			return autoren.get(0);
		}

		return String.join(", ", autoren.subList(0, autoren.size() - 1))
				+ " & " + autoren.get(autoren.size() - 1);
	}

	/**
	 * Returns the episode title.
	 *
	 * @return the episode title
	 */
	public Optional<String> getPodcastFolgenName() {
		return podcastFolgenName;
	}

	/**
	 * Sets the episode title.
	 *
	 * @param podcastFolgenName episode title
	 */
	public void setPodcastFolgenName(Optional<String> podcastFolgenName) {
		this.podcastFolgenName = Objects.requireNonNull(podcastFolgenName, "podcastFolgenName");
	}

}
