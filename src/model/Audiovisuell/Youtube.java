package model.Audiovisuell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Youtube extends Audiovisuell {
	@Override
	public String generiereLangbeleg() {
		return getAutorsString(autor) + ". " +
				datumFormatierung(veroeffentlichungsdatum) + ". " +
				getTitel() + ". [" +
				getBeitragsArt() + "]. " +
				getMedium() + ". " +
				getUrl() + " " +
				getAbruffDatum().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}

	@Override
	public String generiereKurzbeleg() {
		return getAutorsString(autor) + ". " +
				datumFormatierung(veroeffentlichungsdatum) + ". " +
				getTitel();
	}

	@Override
	public String datumFormatierung(LocalDate datum) {
		return "(" + datum.getYear() + ")";
	}
}
