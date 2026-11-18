package model.Audiovisuell;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

public class SocialMediaBeitraege extends Audiovisuell {
	private Optional<String> text = Optional.empty();
	private Optional<String> beschreibung = Optional.empty();

	@Override
	public int getAnzahlAusgefuellterAttribute() {
		int anzahl = super.getAnzahlAusgefuellterAttribute();
		if (hatText(text)) anzahl++;
		if (hatText(beschreibung)) anzahl++;
		return anzahl;
	}

	@Override
	public int getAnzahlRelevanterAttribute() {
		return super.getAnzahlRelevanterAttribute() + 2;
	}

	@Override
	public String generiereLangbeleg() {
		return getAutorsString(autor) + ". " +
				datumFormatierung(veroeffentlichungsdatum) + ". " +
				getTitel().orElse("") + ". [" +
				getBeitragsArt().orElse("") + "]. " +
				getMedium().orElse("") +
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

	public Optional<String> getText() {
		return text;
	}

	public void setText(Optional<String> text) {
		this.text = Objects.requireNonNull(text, "text");
	}

	public Optional<String> getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(Optional<String> beschreibung) {
		this.beschreibung = Objects.requireNonNull(beschreibung, "beschreibung");
	}

}
