package model.Audiovisuell;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Objects;

public class FIlmsVideos extends Audiovisuell {
	private Optional<String> produktionsfirma = Optional.empty();


	@Override
	public String generiereLangbeleg() {
		if (getUrl().isPresent()) {
			return getAutorsString(autor) + ". " +
					datumFormatierung(veroeffentlichungsdatum) + ". " +
					getTitel().orElse("") + " [" +
					getBeitragsArt().orElse("") + "]. " +
					getProduktionsfirma().orElse("") + ". " +
					getUrl().orElse("") + ".";
		} else {
			return getAutorsString(autor) + ". " +
					datumFormatierung(veroeffentlichungsdatum) + ". " +
					getTitel().orElse("") + " [" +
					getBeitragsArt().orElse("") + "]. " +
					getProduktionsfirma().orElse("") + ".";
		}
	}

	@Override
	public String generiereKurzbeleg() {
		return getAutorsString(autor) + ". " +
				datumFormatierung(veroeffentlichungsdatum) + ". " +
				getTitel().orElse("") + " [";
	}

	public Optional<String> getProduktionsfirma() {
		return this.produktionsfirma;
	}

	public void setProduktionsfirma(Optional<String> produktionsfirma) {
		this.produktionsfirma = Objects.requireNonNull(produktionsfirma, "produktionsfirma");
	}

	@Override
	public String datumFormatierung(Optional<LocalDate> datum) {
		return datum.map(d -> "(" + d.getYear() + ")").orElse("");
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
