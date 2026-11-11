package model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public abstract class Literatur {
	public List<String> autor;
	public Optional<String> titel;
	public Optional<LocalDate> veroeffentlichungsdatum;
	public LocalDate abrufdatum;

	abstract String generiereKurzbeleg();

	abstract String generiereLangbeleg();
}
