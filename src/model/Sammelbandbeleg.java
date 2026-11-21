package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a contribution to an edited volume and formats its citations.
 */
public class Sammelbandbeleg extends Literatur {


	private List<String> herausgeber;
	private Optional<String> sammelbandtitel;
	private Optional<String> verlag;
	private Optional<Integer> auflage;
	private Optional<String> erscheinungsort;
	private Seitenangabe seitenangabe;

	// Konstruktor für einen vollständigen Beleg
	/**
	 * Creates a Sammelbandbeleg instance with the supplied metadata.
	 *
	 * @param autoren author list
	 * @param veroeffentlichungsdatum publication date
	 * @param beitragstitel contribution title
	 * @param herausgeber editor information
	 * @param sammelbandtitel edited-volume title
	 * @param auflage edition number
	 * @param verlag publisher
	 * @param erscheinungsort publication place
	 * @param anfang first page, if known
	 * @param ende last page, if known
	 */
	public Sammelbandbeleg(List<String> autoren, Optional<LocalDate> veroeffentlichungsdatum, Optional<String> beitragstitel,
			List<String> herausgeber, Optional<String> sammelbandtitel, Optional<Integer> auflage,
			Optional<String> verlag, Optional<String> erscheinungsort, Optional<Integer> anfang,
			Optional<Integer> ende) {

		setAutoren(autoren);
		setHerausgeber(herausgeber);
		setVeroeffentlichungsdatum(veroeffentlichungsdatum);
		setBeitragstitel(beitragstitel);
		setSammelbandtitel(sammelbandtitel);
		setAuflage(auflage);
		setVerlag(verlag);
		setErscheinungsort(erscheinungsort);
		setSeitenangabe(anfang, ende);
	}


	/**
	 * Trims names and removes null or blank entries; accepts a null list.
	 *
	 * @param personen names to normalize
	 * @return a new list of nonblank names
	 */
	private List<String> bereinigePersonen(List<String> personen) {

		List<String> ergebnis = new ArrayList<>();

		if (personen != null) {
			for (String person : personen) {

				if (person != null && !person.isBlank()) {
					ergebnis.add(person.trim());
				}
			}
		}

		return ergebnis;
	}

	/**
	 * Sets the author list.
	 *
	 * @param autoren author list
	 */
	public void setAutoren(List<String> autoren) {
		setAutor(bereinigePersonen(autoren));
	}

	/**
	 * Sets the editor information.
	 *
	 * @param herausgeber editor information
	 */
	public void setHerausgeber(List<String> herausgeber) {
		this.herausgeber = bereinigePersonen(herausgeber);
	}


	/**
	 * Sets the contribution title.
	 *
	 * @param beitragstitel contribution title
	 */
	public void setBeitragstitel(Optional<String> beitragstitel) {
		setTitel(bereinigeText(beitragstitel));
	}

	/**
	 * Sets the edited-volume title.
	 *
	 * @param sammelbandtitel edited-volume title
	 */
	public void setSammelbandtitel(Optional<String> sammelbandtitel) {
		this.sammelbandtitel = bereinigeText(sammelbandtitel);
	}

	/**
	 * Sets the publisher.
	 *
	 * @param verlag publisher
	 */
	public void setVerlag(Optional<String> verlag) {
		this.verlag = bereinigeText(verlag);
	}

	/**
	 * Sets the edition number.
	 *
	 * @param auflage edition number
	 */
	public void setAuflage(Optional<Integer> auflage) {
	    this.auflage = auflage;
	}

	/**
	 * Sets the publication place.
	 *
	 * @param erscheinungsort publication place
	 */
	public void setErscheinungsort(Optional<String> erscheinungsort) {
		this.erscheinungsort = bereinigeText(erscheinungsort);
	}

	/**
	 * Trims text and treats blank values as absent.
	 *
	 * @param text text
	 * @return the normalized optional text
	 */
	private Optional<String> bereinigeText(Optional<String> text) {

		String wert = text.orElse("");

		if (wert.isBlank()) {
			return Optional.empty();
		}

		return Optional.of(wert.trim());
	}

	/**
	 * Sets the formatted page range.
	 *
	 * @param anfang first page, if known
	 * @param ende last page, if known
	 * @throws IllegalArgumentException if the end has no start, the start is below one, or the end precedes the start
	 */
	public void setSeitenangabe(Optional<Integer> anfang, Optional<Integer> ende) {

		this.seitenangabe = new Seitenangabe(anfang, ende);
	}



	/**
	 * Returns the editor information.
	 *
	 * @return the editor information
	 */
	public String getHerausgeber() {

		if (herausgeber.isEmpty()) {
			return "";
		}

		return String.join(", ", herausgeber);
	}

	/**
	 * Returns the publication year.
	 *
	 * @return the publication year
	 */
	public String getErscheinungsjahr() {

		if (veroeffentlichungsdatum.isPresent()) {
			return String.valueOf(veroeffentlichungsdatum.orElseThrow().getYear());
		}

		return "";
	}

	/**
	 * Returns the contribution title.
	 *
	 * @return the contribution title
	 */
	public String getBeitragstitel() {
		return getTitel().orElse("");
	}

	/**
	 * Returns the edited-volume title.
	 *
	 * @return the edited-volume title
	 */
	public String getSammelbandtitel() {
		return sammelbandtitel.orElse("");
	}

	/**
	 * Returns the publisher.
	 *
	 * @return the publisher
	 */
	public String getBuchverlag() {
		return verlag.orElse("");
	}

	/**
	 * Returns the edition number.
	 *
	 * @return the edition number
	 */
	public String getAuflage() {

		if (auflage.isPresent()) {
			return auflage.get().toString();
		}

		return "";
	}

	/**
	 * Returns the publication place.
	 *
	 * @return the publication place
	 */
	public String getErscheinungsort() {
		return erscheinungsort.orElse("");
	}

	/**
	 * Returns the formatted page range.
	 *
	 * @return the formatted page range
	 */
	public String getSeitenangabe() {
		return seitenangabe.getSeitenangabe();
	}




	/**
	 * Formats a short citation for this source.
	 *
	 * @return the short citation
	 */
	@Override
	public String generiereKurzbeleg() {
		String beleg = belegAutor() + ", " + belegJahr();
		String seiten = getSeitenangabe();

		if (seiten.isEmpty()) {
			return "(" + beleg + ")";
		}

		return "(" + beleg + ", S. " + seiten + ")";
	}

	@Override
	/**
	 * Formats a full bibliographic citation for this source.
	 *
	 * @return the full citation
	 * @throws IllegalArgumentException if required full-citation metadata is missing
	 */
	public String generiereLangbeleg() {

		if (getTitel().isEmpty() || sammelbandtitel.isEmpty() || verlag.isEmpty() || herausgeber.isEmpty()) {

			throw new IllegalArgumentException(
					"Es müssen Beitragstitel, " + "Sammelbandtitel, Herausgeber und Verlag angegeben sein.");
		}

		List<String> banddetails = new ArrayList<>();
		if (auflage.isPresent()) {
			banddetails.add(getAuflage() + ". Aufl.");
		}
		String seiten = getSeitenangabe();
		if (!seiten.isEmpty()) {
			banddetails.add("S. " + seiten);
		}
		String bandtitel = getSammelbandtitel();
		if (!banddetails.isEmpty()) {
			bandtitel += " (" + String.join(", ", banddetails) + ")";
		}

		String ort = getErscheinungsort();
		String veroeffentlichungsort = ort.isEmpty() ? "" : ort + ": ";
		return belegAutor() + " (" + belegJahr() + "). " + getBeitragstitel() + ". In " + getHerausgeber()
				+ " (Hrsg.), " + bandtitel + ". " + veroeffentlichungsort + getBuchverlag() + ".";
	}

	/**
	 * Formats the author list for citation output.
	 *
	 * @return the formatted authors
	 */
	private String getAutoren() {
		return String.join(", ", getAutor());
	}

	@Override
	/**
	 * Returns the current placeholder count; completeness calculation is not implemented.
	 *
	 * @return zero
	 */
	public int getAnzahlAusgefuellterAttribute() {
		// TODO Auto-generated method stub
				return 0;
	}

	@Override
	/**
	 * Returns the current placeholder count; completeness calculation is not implemented.
	 *
	 * @return zero
	 */
	public int getAnzahlRelevanterAttribute() {
		// TODO Auto-generated method stub
		return 0;
	}
	/**
	 * Formats authors with o.A. as the fallback for an empty list.
	 *
	 * @return the citation author text
	 */
	private String belegAutor() {
		String autoren = getAutoren();
		return autoren.isEmpty() ? "o.A." : autoren;
	}

	/**
	 * Formats the publication year with o.J. as the fallback.
	 *
	 * @return the citation year text
	 */
	private String belegJahr() {
		return veroeffentlichungsdatum.isEmpty() ? "o.J." : getErscheinungsjahr();
	}

}