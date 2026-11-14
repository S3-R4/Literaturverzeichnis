package model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public abstract class Literatur {
	protected List<String> autor;
	protected String titel;
	protected LocalDate veroeffentlichungsdatum;
	protected LocalDate abrufdatum;

	public List<String> getAutor() {
		return autor;
	}

	public void setAutor(List<String> autor) {
		this.autor = autor;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public LocalDate getVeroeffentlichungsdatum() {
		return veroeffentlichungsdatum;
	}

	public void setVeroeffentlichungsdatum(LocalDate veroeffentlichungsdatum) {
		this.veroeffentlichungsdatum = veroeffentlichungsdatum;
	}

	public LocalDate getAbrufdatum() {
		return abrufdatum;
	}

	public void setAbrufdatum(LocalDate abrufdatum) {
		this.abrufdatum = abrufdatum;
	}

	public abstract String generiereKurzbeleg();

	public abstract String generiereLangbeleg();
}
