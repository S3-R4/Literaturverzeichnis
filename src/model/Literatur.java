package model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

/**
 * Defines shared bibliographic metadata and the citation and completeness contracts.
 */
public abstract class Literatur {

protected List<String> autor = new java.util.ArrayList<>();
protected Optional<String> titel = Optional.empty();
protected Optional<LocalDate> veroeffentlichungsdatum = Optional.empty();
protected Optional<LocalDate> abrufdatum = Optional.empty();

/**
 * Returns the author list.
 *
 * @return the author list
 */
public List<String> getAutor() {
    return autor;
}

/**
 * Sets the author list.
 *
 * @param autor author list
 */
public void setAutor(List<String> autor) {
    this.autor = Objects.requireNonNull(autor, "autor");
}

/**
 * Returns the title.
 *
 * @return the title
 */
public Optional<String> getTitel() {
    return titel;
}

/**
 * Sets the title.
 *
 * @param titel title
 */
public void setTitel(Optional<String> titel) {
    this.titel = Objects.requireNonNull(titel, "titel");
}

/**
 * Returns the publication date.
 *
 * @return the publication date
 */
public Optional<LocalDate> getVeroeffentlichungsdatum() {
    return veroeffentlichungsdatum;
}

/**
 * Sets the publication date.
 *
 * @param veroeffentlichungsdatum publication date
 */
public void setVeroeffentlichungsdatum(Optional<LocalDate> veroeffentlichungsdatum) {
    this.veroeffentlichungsdatum = Objects.requireNonNull(veroeffentlichungsdatum, "veroeffentlichungsdatum");
}

/**
 * Returns the retrieval date.
 *
 * @return the retrieval date
 */
public Optional<LocalDate> getAbrufdatum() {
    return abrufdatum;
}

/**
 * Sets the retrieval date.
 *
 * @param abrufdatum retrieval date
 */
public void setAbrufdatum(Optional<LocalDate> abrufdatum) {
    this.abrufdatum = Objects.requireNonNull(abrufdatum, "abrufdatum");
}

/**
 * Formats a short citation for this source.
 *
 * @return the short citation
 */
public abstract String generiereKurzbeleg();

/**
 * Formats a full bibliographic citation for this source.
 *
 * @return the full citation
 */
public abstract String generiereLangbeleg();

/**
 * Counts populated attributes used to measure source completeness.
 *
 * @return the number of populated relevant attributes
 */
public abstract int getAnzahlAusgefuellterAttribute();

/**
 * Returns the number of attributes considered for source completeness.
 *
 * @return the total number of relevant attributes
 */
public abstract int getAnzahlRelevanterAttribute();

/**
 * Formats the ratio of populated to relevant attributes for display.
 *
 * @return the German completeness status
 */
public String getErfassungsstand() {
return getAnzahlAusgefuellterAttribute() + "/"+ getAnzahlRelevanterAttribute() + " Attribute eingetragen";
}
}
