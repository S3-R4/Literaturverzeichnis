package model;

import java.time.LocalDate;
import java.util.List;

public abstract class Literatur {
	protected List<String> autor;
	protected String titel;
	protected LocalDate veroeffentlichungsdatum;
	protected LocalDate abrufdatum;

	public abstract String generiereKurzbeleg();

	public abstract String generiereLangbeleg();
}
