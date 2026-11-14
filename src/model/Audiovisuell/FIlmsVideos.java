package model.Audiovisuell;

import java.time.LocalDate;

public class FIlmsVideos extends Audiovisuell {
	private String produktionsfirma;

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
		return ("(" + datum.getYear() + ")");
	}

}
