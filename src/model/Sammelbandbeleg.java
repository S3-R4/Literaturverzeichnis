package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Sammelbandbeleg extends Literatur {


	private List<String> herausgeber;
	private Optional<String> sammelbandtitel;
	private Optional<String> verlag;
	private Optional<Integer> auflage;
	private Optional<String> erscheinungsort;
	private Seitenangabe seitenangabe;

	// Konstruktor für einen vollständigen Beleg
	public Sammelbandbeleg(List<String> autoren, Optional<LocalDate> veroeffentlichungsdatum, Optional<String> beitragstitel,
			List<String> herausgeber, Optional<String> sammelbandtitel, Optional<Integer> auflage,
			Optional<String> verlag, Optional<String> erscheinungsort, Optional<Integer> anfang,
			Optional<Integer> ende) {

		setAutoren(autoren);
		setHerausgeber(herausgeber);
		setVeroeffentlichungsdatum(veroeffentlichungsdatum);
		setBeitragstitel(beitragstitel);
		setSammelbandtitel(sammelbandtitel);
		setAuflage(auflage);
		setVerlag(verlag);
		setErscheinungsort(erscheinungsort);
		setSeitenangabe(anfang, ende);
	}


	private List<String> bereinigePersonen(List<String> personen) {

		List<String> ergebnis = new ArrayList<>();

		if (personen != null) {
			for (String person : personen) {

				if (person != null && !person.isBlank()) {
					ergebnis.add(person.trim());
				}
			}
		}

		return ergebnis;
	}

	public void setAutoren(List<String> autoren) {
		this.autor = Optional.of(bereinigePersonen(autoren));
	}

	public void setHerausgeber(List<String> herausgeber) {
		this.herausgeber = bereinigePersonen(herausgeber);
	}


	public void setBeitragstitel(Optional<String> beitragstitel) {
		setTitel(bereinigeText(beitragstitel));
	}

	public void setSammelbandtitel(Optional<String> sammelbandtitel) {
		this.sammelbandtitel = bereinigeText(sammelbandtitel);
	}

	public void setVerlag(Optional<String> verlag) {
		this.verlag = bereinigeText(verlag);
	}

	public void setAuflage(Optional<Integer> auflage) {
	    this.auflage = auflage;
	}

	public void setErscheinungsort(Optional<String> erscheinungsort) {
		this.erscheinungsort = bereinigeText(erscheinungsort);
	}

	private Optional<String> bereinigeText(Optional<String> text) {

		String wert = text.orElse("");

		if (wert.isBlank()) {
			return Optional.empty();
		}

		return Optional.of(wert.trim());
	}

	public void setSeitenangabe(Optional<Integer> anfang, Optional<Integer> ende) {

		this.seitenangabe = new Seitenangabe(anfang, ende);
	}



	public String getHerausgeber() {

		if (herausgeber.isEmpty()) {
			return "";
		}

		return String.join(", ", herausgeber);
	}

	public String getErscheinungsjahr() {

		if (veroeffentlichungsdatum.isPresent()) {
			return String.valueOf(veroeffentlichungsdatum.orElseThrow().getYear());
		}

		return "";
	}

	public String getBeitragstitel() {
		return getTitel().orElse("");
	}

	public String getSammelbandtitel() {
		return sammelbandtitel.orElse("");
	}

	public String getBuchverlag() {
		return verlag.orElse("");
	}

	public String getAuflage() {

		if (auflage.isPresent()) {
			return auflage.get().toString();
		}

		return "";
	}

	public String getErscheinungsort() {
		return erscheinungsort.orElse("");
	}

	public String getSeitenangabe() {
		return seitenangabe.getSeitenangabe();
	}




	@Override
	public String generiereKurzbeleg() {
		String beleg;
		if (getErscheinungsjahr().isEmpty()) {
			beleg = getAutoren() + ", o.J.";
		} else {
			beleg = getAutoren() + ", " + getErscheinungsjahr();
		}
		
		String seiten = getSeitenangabe();

		if (seiten.isEmpty()) {
			return "(" + beleg + ")";
		}

		return "(" + beleg + ", S. " + seiten + ")";
	}

	@Override
	public String generiereLangbeleg() {

		if (getTitel().isEmpty() || sammelbandtitel.isEmpty() || verlag.isEmpty() || herausgeber.isEmpty()) {

			throw new IllegalArgumentException(
					"Es müssen Beitragstitel, " + "Sammelbandtitel, Herausgeber und Verlag angegeben sein.");
		}

		String bandtitelMitAuflage = getSammelbandtitel() + " (";

		if (auflage.isPresent()) {
			bandtitelMitAuflage += getAuflage() + ". Aufl.";
		}

		String seiten = getSeitenangabe();

		String seitenTeil = "";

		if (!seiten.isEmpty()) {
			seitenTeil = ", S. " + seiten;
		}
		
		if (getAutoren().equals("")) {
			return "o.A. (" + getErscheinungsjahr() + "). " + getBeitragstitel() + ". In " + getHerausgeber()
			+ " (Hrsg.), " + bandtitelMitAuflage + seitenTeil + "). " + getErscheinungsort() + ": " + getBuchverlag()
			+ ".";
		}
		
		return getAutoren() + " (" + getErscheinungsjahr() + "). " + getBeitragstitel() + ". In " + getHerausgeber()
				+ " (Hrsg.), " + bandtitelMitAuflage + seitenTeil + "). " + getErscheinungsort() + ": " + getBuchverlag()
				+ ".";
	}

	private String getAutoren() {

		if (autor.orElseGet(List::of).isEmpty()) {
			return "";
		}

		return String.join(", ", autor.orElseGet(List::of));
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