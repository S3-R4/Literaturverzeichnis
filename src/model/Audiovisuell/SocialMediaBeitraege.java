package model.Audiovisuell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SocialMediaBeitraege extends Audiovisuell {
	private String text;
	private String beschreibung;

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
				getTitel() + ". [" +
				getBeitragsArt() + "]. " +
				getMedium() +
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
}
