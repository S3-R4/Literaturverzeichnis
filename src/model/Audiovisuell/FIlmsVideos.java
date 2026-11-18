package model.Audiovisuell;

import java.time.LocalDate;

public class FIlmsVideos extends Audiovisuell {
	private String produktionsfirma;

	@Override
	public int getAnzahlAusgefuellterAttribute() {
		return super.getAnzahlAusgefuellterAttribute() + (hatText(produktionsfirma) ? 1 : 0);
	}

	@Override
	public int getAnzahlRelevanterAttribute() {
		return super.getAnzahlRelevanterAttribute() + 1;
	}

	// protected String beitragsArt;
	// protected String medium;
	// protected String url;
	// protected String titel
	// public List<String> autor;
	// public LocalDate veroeffentlichungsdatum;
	// public LocalDate abrufdatum;

	@Override
	public String generiereLangbeleg() {
		if (getUrl() != null) {
			return getAutorsString(autor) + ". " +
					datumFormatierung(veroeffentlichungsdatum) + ". " +
					getTitel() + " [" +
					getBeitragsArt() + "]. " +
					getProduktionsfirma() + ". " +
					getUrl() + ".";
		} else {
			return getAutorsString(autor) + ". " +
					datumFormatierung(veroeffentlichungsdatum) + ". " +
					getTitel() + " [" +
					getBeitragsArt() + "]. " +
					getProduktionsfirma() + ".";
		}
	}

	@Override
	public String generiereKurzbeleg() {
		return getAutorsString(autor) + ". " +
				datumFormatierung(veroeffentlichungsdatum) + ". " +
				getTitel() + " [";
	}

	public String getProduktionsfirma() {
		return this.produktionsfirma;
	}

	public void setProduktionsfirma(String produktionsfirma) {
		this.produktionsfirma = produktionsfirma;
	}

	@Override
	public String datumFormatierung(LocalDate datum) {
		if (abrufdatum == null)
			return "";
		return ("(" + datum.getYear() + ")");
	}

}
