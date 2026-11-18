package model.Audiovisuell;

import java.time.LocalDate;
import java.util.Optional;
import java.time.format.DateTimeFormatter;

public class Youtube extends Audiovisuell {
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
	public String generiereKurzbeleg() {
		return getAutorsString(autor) + ". " +
				datumFormatierung(veroeffentlichungsdatum) + ". " +
				getTitel().orElse("");
	}

	@Override
	public String datumFormatierung(Optional<LocalDate> datum) {
		return datum.map(d -> "(" + d.getYear() + ")").orElse("");
	}

}
