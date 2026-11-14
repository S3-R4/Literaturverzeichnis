package model.Audiovisuell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class Podcastfolgen extends Audiovisuell {
	private String herausgebendeOrganisation = "";
	private int podcastFolge = 0;
	private String podcastFolgenName = "";

	public Podcastfolgen() {
		setAutor(new ArrayList<>());
		setBeitragsArt("Podcast-Folge");
	}

	public String getHerausgebendeOrganisation() {
		return herausgebendeOrganisation;
	}

	public void setHerausgebendeOrganisation(String herausgebendeOrganisation) {
		this.herausgebendeOrganisation = herausgebendeOrganisation;
	}

	public int getPodcastFolge() {
		return podcastFolge;
	}

	public void setPodcastFolge(int podcastFolge) {
		this.podcastFolge = podcastFolge;
	}


	public void addProduzent(String produzent) {
		getAutor().add(produzent);
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
				+ "Folge #" + podcastFolge + " - " + getPodcastFolgenName() + ". "
				+ "[" + getBeitragsArt() + "]. "
				+ "In: " + getTitel();

		if (herausgebendeOrganisation != null && !herausgebendeOrganisation.isBlank()) {
			langbeleg += " " + herausgebendeOrganisation;
		}

		return langbeleg + ". " + getUrl() + " ("
				+ abrufdatum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN))
				+ ").";
	}

	@Override
	public String generiereKurzbeleg() {
		String autorenString = generiereAutorenString();
		return autorenString + (autorenString.endsWith(".") ? " " : ". ")
				+ datumFormatierung(veroeffentlichungsdatum) + " Folge #" + podcastFolge;
	}

	@Override
	public String datumFormatierung(LocalDate datum) {
		return "(" + datum.format(DateTimeFormatter.ofPattern("yyyy, d, MMMM", Locale.GERMAN)) + ")";
	}

	private String generiereAutorenString() {
		if (autor == null || autor.isEmpty()) {
			return "";
		}
		if (autor.size() == 1) {
			return autor.get(0);
		}

		return String.join(", ", autor.subList(0, autor.size() - 1))
				+ " & " + autor.get(autor.size() - 1);
	}


	public String getPodcastFolgenName() {
		return podcastFolgenName;
	}


	public void setPodcastFolgenName(String podcastFolgenName) {
		this.podcastFolgenName = podcastFolgenName;
	}
}
