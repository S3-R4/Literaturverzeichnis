package model;

import java.lang.foreign.Linker.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public abstract class Literatur {
    protected List<String> autor;
    protected Optional<String> titel;
    protected Optional<LocalDate> veroeffentlichungsdatum;
    protected Optional<LocalDate> abrufdatum;

    public List<String> getAutor() {
        return autor;
    }

    public void setAutor(List<String> autor) {
        this.autor = autor;
    }

    public Optional<String> getTitel() {
        return titel;
    }

    public void setTitel(Optional<String> titel) {
        this.titel = titel;
    }

    public Optional<LocalDate> getVeroeffentlichungsdatum() {
        return veroeffentlichungsdatum;
    }

    public void setVeroeffentlichungsdatum(Optional<LocalDate> veroeffentlichungsdatum) {
        this.veroeffentlichungsdatum = veroeffentlichungsdatum;
    }

    public Optional<LocalDate> getAbrufdatum() {
        return abrufdatum;
    }

    public void setAbrufdatum(Optional<LocalDate> abrufdatum) {
        this.abrufdatum = abrufdatum;
    }

    public abstract String generiereKurzbeleg();

    public abstract String generiereLangbeleg();

    public abstract int getAnzahlAusgefuellterAttribute();

    public abstract int getAnzahlRelevanterAttribute();

    public String getErfassungsstand() {
        return getAnzahlAusgefuellterAttribute() + "/" + getAnzahlRelevanterAttribute() + " Attribute eingetragen";
    }
}