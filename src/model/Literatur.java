package model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

public abstract class Literatur {
protected Optional<List<String>> autor = Optional.empty();
protected Optional<String> titel = Optional.empty();
protected Optional<LocalDate> veroeffentlichungsdatum = Optional.empty();
protected Optional<LocalDate> abrufdatum = Optional.empty();

public Optional<List<String>> getAutor() {
    return autor;
}

public void setAutor(Optional<List<String>> autor) {
this.autor = Objects.requireNonNull(autor, "autor");
}

public Optional<String> getTitel() {
return titel;
}

public void setTitel(Optional<String> titel) {
this.titel = Objects.requireNonNull(titel, "titel");
}

public Optional<LocalDate> getVeroeffentlichungsdatum() {
return veroeffentlichungsdatum;
}

public void setVeroeffentlichungsdatum(Optional<LocalDate> veroeffentlichungsdatum) {
this.veroeffentlichungsdatum = Objects.requireNonNull(veroeffentlichungsdatum, "veroeffentlichungsdatum");
}

public Optional<LocalDate> getAbrufdatum() {
return abrufdatum;
}

public void setAbrufdatum(Optional<LocalDate> abrufdatum) {
this.abrufdatum = Objects.requireNonNull(abrufdatum, "abrufdatum");
}

public abstract String generiereKurzbeleg();

public abstract String generiereLangbeleg();

public abstract int getAnzahlAusgefuellterAttribute();

public abstract int getAnzahlRelevanterAttribute();

public String getErfassungsstand() {
	return getAnzahlAusgefuellterAttribute() + "/"+ getAnzahlRelevanterAttribute() + " Attribute eingetragen";
    }
}