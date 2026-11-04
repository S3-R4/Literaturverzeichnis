package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Sammelbandbeleg {

	private List<String> autoren;
	private List<String> herausgeber;
	private Optional<Integer> erscheinungsjahr;
	private Optional<String> beitragstitel;
	private Optional<String> sammelbandtitel;
	private Optional<String> verlag;
	private Optional<Integer> auflage;
	private Optional<String> erscheinungsort;
	private Seitenangabe seitenangabe;

	// Konstruktor für einen vollständigen Beleg
	public Sammelbandbeleg(List<String> autoren, Optional<Integer> erscheinungsjahr, Optional<String> beitragstitel,
			List<String> herausgeber, Optional<String> sammelbandtitel, Optional<Integer> auflage,
			Optional<String> verlag, Optional<String> erscheinungsort, Optional<Integer> anfang,
			Optional<Integer> ende) {

		setAutoren(autoren);
		setHerausgeber(herausgeber);
		setErscheinungsjahr(erscheinungsjahr);
		setBeitragstitel(beitragstitel);
		setSammelbandtitel(sammelbandtitel);
		setAuflage(auflage);
		setVerlag(verlag);
		setErscheinungsort(erscheinungsort);
		setSeitenangabe(anfang, ende);
	}

	// Konstruktor für einen Kurzbeleg
	public Sammelbandbeleg(List<String> autoren, Optional<Integer> erscheinungsjahr, Optional<Integer> anfang,
			Optional<Integer> ende) {

		setAutoren(autoren);
		setHerausgeber(new ArrayList<>());
		setErscheinungsjahr(erscheinungsjahr);
		setBeitragstitel(Optional.empty());
		setSammelbandtitel(Optional.empty());
		setAuflage(Optional.empty());
		setVerlag(Optional.empty());
		setErscheinungsort(Optional.empty());
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
		this.autoren = bereinigePersonen(autoren);
	}

	public void setHerausgeber(List<String> herausgeber) {
		this.herausgeber = bereinigePersonen(herausgeber);
	}

	public void setErscheinungsjahr(Optional<Integer> erscheinungsjahr) {
		this.erscheinungsjahr = erscheinungsjahr;
	}

	public void setBeitragstitel(Optional<String> beitragstitel) {
		this.beitragstitel = bereinigeText(beitragstitel);
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

	public String getAutoren() {

		if (autoren.isEmpty()) {
			return "";
		}

		return String.join(", ", autoren);
	}

	public String getHerausgeber() {

		if (herausgeber.isEmpty()) {
			return "";
		}

		return String.join(", ", herausgeber);
	}

	public String getErscheinungsjahr() {

		if (erscheinungsjahr.isPresent()) {
			return erscheinungsjahr.get().toString();
		}

		return "";
	}

	public String getBeitragstitel() {
		return beitragstitel.orElse("");
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

	// Erzeugt einen Langbeleg
	public String getBelegLang() {

		if (beitragstitel.isEmpty() || sammelbandtitel.isEmpty() || verlag.isEmpty() || herausgeber.isEmpty()) {

			throw new IllegalArgumentException(
					"Es müssen Beitragstitel, " + "Sammelbandtitel, Herausgeber und Verlag angegeben sein.");
		}

		String bandtitelMitAuflage = getSammelbandtitel() + ".";

		if (auflage.isPresent()) {
			bandtitelMitAuflage += " " + getAuflage() + ". Aufl.";
		}

		String seiten = getSeitenangabe();

		String seitenTeil = "";

		if (!seiten.isEmpty()) {
			seitenTeil = ", S. " + seiten;
		}

		return getAutoren() + " (" + getErscheinungsjahr() + "): " + getBeitragstitel() + ". In: " + getHerausgeber()
				+ " (Hrsg.): " + bandtitelMitAuflage + " " + getErscheinungsort() + ": " + getBuchverlag() + seitenTeil
				+ ".";
	}

	// Erzeugt einen Kurzbeleg
	public String getBelegKurz() {

		String beleg = getAutoren() + ", " + getErscheinungsjahr();
		String seiten = getSeitenangabe();

		if (seiten.isEmpty()) {
			return "(" + beleg + ")";
		}

		return "(" + beleg + ", S. " + seiten + ")";
	}
}