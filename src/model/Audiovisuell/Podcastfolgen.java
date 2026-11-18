package model.Audiovisuell;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Objects;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Podcastfolgen extends Audiovisuell {
	private Optional<String> herausgebendeOrganisation = Optional.empty();
	private Optional<Integer> podcastFolge = Optional.empty();
	private Optional<String> podcastFolgenName = Optional.empty();

	@Override
	public int getAnzahlAusgefuellterAttribute() {
		int anzahl = super.getAnzahlAusgefuellterAttribute();
		if (hatText(herausgebendeOrganisation)) anzahl++;
		if (podcastFolge.filter(folge -> folge > 0).isPresent()) anzahl++;
		if (hatText(podcastFolgenName)) anzahl++;
		return anzahl;
	}

	@Override
	public int getAnzahlRelevanterAttribute() {
		return super.getAnzahlRelevanterAttribute() + 3;
	}

	public Podcastfolgen() {
		setBeitragsArt(Optional.of("Podcast-Folge"));
	}

	public Optional<String> getHerausgebendeOrganisation() {
		return herausgebendeOrganisation;
	}

	public void setHerausgebendeOrganisation(Optional<String> herausgebendeOrganisation) {
		this.herausgebendeOrganisation = Objects.requireNonNull(herausgebendeOrganisation, "herausgebendeOrganisation");
	}

	public Optional<Integer> getPodcastFolge() {
		return podcastFolge;
	}

	public void setPodcastFolge(Optional<Integer> podcastFolge) {
		this.podcastFolge = Objects.requireNonNull(podcastFolge, "podcastFolge");
	}

	public void addProduzent(String produzent) {
		List<String> produzenten = new ArrayList<>(getAutor());
		produzenten.add(produzent);
		setAutor(produzenten);
	}

	public void removeProduzent(int index) {
		getAutor().remove(index);
	}

	@Override
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
	public String generiereKurzbeleg() {
		String autorenString = generiereAutorenString();
		return autorenString + (autorenString.endsWith(".") ? " " : ". ")
				+ datumFormatierung(veroeffentlichungsdatum) + " Folge #" + podcastFolge.map(String::valueOf).orElse("");
	}

	@Override
	public String datumFormatierung(Optional<LocalDate> datum) {
		return datum.map(d -> "(" + d.format(DateTimeFormatter.ofPattern("yyyy, d, MMMM", Locale.GERMAN)) + ")").orElse("");
	}

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

	public Optional<String> getPodcastFolgenName() {
		return podcastFolgenName;
	}

	public void setPodcastFolgenName(Optional<String> podcastFolgenName) {
		this.podcastFolgenName = Objects.requireNonNull(podcastFolgenName, "podcastFolgenName");
	}

}
