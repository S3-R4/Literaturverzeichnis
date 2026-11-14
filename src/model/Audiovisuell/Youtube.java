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
		if (abrufdatum == null)
			return "";
		return "(" + datum.getYear() + ")";
	}

	@Override
	public int getAnzahlAusgefuellterAttribute() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAnzahlRelevanterAttribute() {
		// TODO Auto-generated method stub
		return 0;
	}
}
