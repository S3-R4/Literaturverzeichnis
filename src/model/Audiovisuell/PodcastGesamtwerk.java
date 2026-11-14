package model.Audiovisuell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PodcastGesamtwerk extends Audiovisuell {

	private String herausgebendeOrganisation;

	@Override
	public String generiereLangbeleg() {
		return getAutorsString(autor) + ". " + datumFormatierung(veroeffentlichungsdatum) + ". [" + getTitel() + "]. "
				+ getHerausgebendeOrganisation() + ". " + getUrl() + " "
				+ getAbruffDatum().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}

	@Override
	public String generiereKurzbeleg() {
		return getAutorsString(autor) + ". " + datumFormatierung(veroeffentlichungsdatum) + ". [" + getTitel() + "]";
	}

	@Override
	public String datumFormatierung(LocalDate datum) {
		return "(" + datum.getYear() + ")";
	}

	public String getHerausgebendeOrganisation() {
		return herausgebendeOrganisation;
	}

	public void setHerausgebendeOrganisation(String herausgebendeOrganisation) {
		this.herausgebendeOrganisation = herausgebendeOrganisation;
	}
}
