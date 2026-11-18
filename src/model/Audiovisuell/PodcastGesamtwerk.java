package model.Audiovisuell;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

public class PodcastGesamtwerk extends Audiovisuell {

	private Optional<String> herausgebendeOrganisation = Optional.empty();

	@Override
	public int getAnzahlAusgefuellterAttribute() {
		return super.getAnzahlAusgefuellterAttribute() + (hatText(herausgebendeOrganisation) ? 1 : 0);
	}

	@Override
	public int getAnzahlRelevanterAttribute() {
		return super.getAnzahlRelevanterAttribute() + 1;
	}

	@Override
	public String generiereLangbeleg() {
		return getAutorsString(autor) + ". " + datumFormatierung(veroeffentlichungsdatum) + ". [" + getTitel().orElse("") + "]. "
				+ getHerausgebendeOrganisation().orElse("") + ". " + getUrl().orElse("") + " "
				+ getAbruffDatum().map(d -> d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).orElse("");
	}

	@Override
	public String generiereKurzbeleg() {
		return getAutorsString(autor) + ". " + datumFormatierung(veroeffentlichungsdatum) + ". [" + getTitel().orElse("") + "]";
	}

	@Override
	public String datumFormatierung(Optional<LocalDate> datum) {
		return datum.map(d -> "(" + d.getYear() + ")").orElse("");
	}

	public Optional<String> getHerausgebendeOrganisation() {
		return herausgebendeOrganisation;
	}

	public void setHerausgebendeOrganisation(Optional<String> herausgebendeOrganisation) {
		this.herausgebendeOrganisation = Objects.requireNonNull(herausgebendeOrganisation, "herausgebendeOrganisation");
	}

}
