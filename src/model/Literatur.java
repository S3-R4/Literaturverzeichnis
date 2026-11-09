package model;

import java.time.LocalDate;
import java.util.List;

public abstract class Literatur {
	public List<String> autor;
	public String titel;
	public LocalDate veroeffentlichungsdatum;
	public LocalDate abrufdatum;

	abstract String generiereKurzbeleg();

	abstract String generiereLangbeleg();
}
