
package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Stores book metadata and formats short and full book citations.
 */
public class Buchbeleg extends Literatur {
private Optional<String> buchverlag = Optional.empty();
@JsonProperty
private Optional<Integer> auflage = Optional.empty();
private Optional<String> erscheinungsort = Optional.empty();
@JsonProperty
private Seitenangabe seitenangabe;

/**
 * Creates a Buchbeleg instance with the supplied metadata.
 *
 * @param autor author list
 * @param veroeffentlichungsdatum publication date
 * @param titel title
 * @param buchverlag publisher
 * @param auflage edition number
 * @param erscheinungsort publication place
 * @param anfang first page, if known
 * @param ende last page, if known
 */
public Buchbeleg(List<String> autor, Optional<LocalDate> veroeffentlichungsdatum, Optional<String> titel, Optional<String> buchverlag,Optional<Integer> auflage, Optional<String> erscheinungsort,Optional<Integer> anfang, Optional<Integer> ende) {

setAutoren(autor);
setErscheinungsdatum(veroeffentlichungsdatum);
setTitel(titel);
setAuflage(auflage);
setBuchverlag(buchverlag);
setErscheinungsort(erscheinungsort);
setSeitenangabe(anfang, ende);
}

/**
 * Creates a Buchbeleg instance with the supplied metadata.
 *
 * @param autoren author list
 * @param erscheinungsjahr publication year
 * @param anfang first page, if known
 * @param ende last page, if known
 */
public Buchbeleg(List<String> autoren, Optional<LocalDate> erscheinungsjahr,Optional<Integer> anfang, Optional<Integer> ende) {setAutoren(autoren);
setErscheinungsdatum(erscheinungsjahr);
setSeitenangabe(anfang, ende);
}

/**
 * Sets the author list.
 *
 * @param autoren author list
 */
public void setAutoren(List<String> autoren) {
List<String> bereinigt = new ArrayList<>();

if (autoren == null) {
    setAutor(bereinigt);
    return;
}

for (String autor : autoren) {
if (autor != null && !autor.isBlank()) {
    bereinigt.add(autor.trim());
    }
}
setAutor(bereinigt);
}

/**
 * Sets the publication date.
 *
 * @param erscheinungsdatum publication date
 * @throws IllegalArgumentException if the publication date is in the future
 */
public void setErscheinungsdatum(Optional<LocalDate> erscheinungsdatum) {
if (erscheinungsdatum.isPresent()&& erscheinungsdatum.get().isAfter(LocalDate.now())) {
throw new IllegalArgumentException("Das Veröffentlichungsdatum darf nicht in der Zukunft liegen."
    );
}

setVeroeffentlichungsdatum(erscheinungsdatum);
}

/**
 * Sets the title.
 *
 * @param titel title
 */
public void setTitel(Optional<String> titel) {
super.setTitel(titel.map(String::trim).filter(wert -> !wert.isBlank()));
}

/**
 * Sets the edition number.
 *
 * @param auflage edition number
 * @throws IllegalArgumentException if the supplied edition is not positive
 */
public void setAuflage(Optional<Integer> auflage) {
if (auflage.isPresent() && auflage.get() <= 0) {
    throw new IllegalArgumentException("Die Auflage muss größer als 0 sein.");
}
this.auflage = auflage;
}

/**
 * Sets the publisher.
 *
 * @param buchverlag publisher
 */
public void setBuchverlag(Optional<String> buchverlag) {
String wert = buchverlag.orElse("");
this.buchverlag = wert.isBlank()? Optional.empty(): Optional.of(wert.trim());
}

/**
 * Sets the publication place.
 *
 * @param erscheinungsort publication place
 */
public void setErscheinungsort(Optional<String> erscheinungsort) {
String wert = erscheinungsort.orElse("");
this.erscheinungsort = wert.isBlank()? Optional.empty(): Optional.of(wert.trim());
}

/**
 * Sets the formatted page range.
 *
 * @param anfang first page, if known
 * @param ende last page, if known
 * @throws IllegalArgumentException if the end has no start, the start is below one, or the end precedes the start
 */
public void setSeitenangabe(Optional<Integer> anfang, Optional<Integer> ende) {
this.seitenangabe = new Seitenangabe(anfang, ende);
}

/**
 * Formats the author list for citation output.
 *
 * @return the formatted authors
 */
public String getAutoren() {
List<String> autoren = getAutor();
return autoren.isEmpty() ? "o.A." : formatiereAutoren(autoren);
}

/**
 * Formats up to three authors and abbreviates additional authors with et al.
 *
 * @param autoren author list
 * @return the formatted author list
 */
private String formatiereAutoren(List<String> autoren) {
final int MAX_AUTORENANZAHL = 3;
String ergebnis = "";

for (int i = 0; i < autoren.size(); i++) {
    if (i >= MAX_AUTORENANZAHL) {
        return ergebnis + ", et al.";
    }

    if (i > 0) {
        ergebnis += i == autoren.size() - 1 ? " & " : ", ";
    }

    ergebnis += autoren.get(i);
}

return ergebnis;
}

/**
 * Returns the publication year.
 *
 * @return the publication year
 */
public String getErscheinungsjahr() {
return getVeroeffentlichungsdatum().map(datum -> Integer.toString(datum.getYear())).orElse("o.J.");
}

/**
 * Returns the edition number.
 *
 * @return the edition number
 */
@JsonIgnore
public String getAuflage() {
return this.auflage.isPresent() ? this.auflage.get().toString() : "";
}

/**
 * Returns the publisher.
 *
 * @return the publisher
 */
public String getBuchverlag() {
return this.buchverlag.orElse("o.V.");
}

/**
 * Returns the publication place.
 *
 * @return the publication place
 */
public String getErscheinungsort() {
return this.erscheinungsort.orElse("o.O.");
}

/**
 * Returns the formatted page range.
 *
 * @return the formatted page range
 */
@JsonIgnore
public String getSeitenangabe() {
return this.seitenangabe.getSeitenangabe();
}

/**
 * Formats a short citation for this source.
 *
 * @return the short citation
 */
@Override
public String generiereKurzbeleg() {
String beleg = getAutoren() + ", " + getErscheinungsjahr();
String seiten = getSeitenangabe();

return seiten.isEmpty()? "(" + beleg + ")": "(" + beleg + ", S." + seiten + ")";
}

@Override
/**
 * Formats a full bibliographic citation for this source.
 *
 * @return the full citation
 * @throws IllegalArgumentException if required full-citation metadata is missing
 */
public String generiereLangbeleg() {
if (getTitel().isEmpty() || buchverlag.isEmpty()) {throw new IllegalArgumentException("Für einen Langbeleg müssen Titel und Verlag angegeben sein.");
}

String titelMitAuflage = getTitel().orElseThrow() + ".";

if (this.auflage.isPresent()) {
    titelMitAuflage += " " + getAuflage() + ". Aufl.";
}

return String.join(" ",getAutoren(),"(" + getErscheinungsjahr() + "): " + titelMitAuflage,getErscheinungsort() + ": " + getBuchverlag() + "."
);
}

@Override
/**
 * Counts populated attributes used to measure source completeness.
 *
 * @return the number of populated relevant attributes
 */
public int getAnzahlAusgefuellterAttribute() {
int anzahl = 0;

if (getAutor().stream().anyMatch(a -> a != null && !a.isBlank())) {
anzahl++;
}

if (getVeroeffentlichungsdatum().isPresent()) {
anzahl++;
}

if (getTitel().isPresent()) {
anzahl++;
}

if (buchverlag.isPresent()) {
    anzahl++;
}

if (erscheinungsort.isPresent()) {
    anzahl++;
}

return anzahl;
}

@Override
/**
 * Returns the number of attributes considered for source completeness.
 *
 * @return the total number of relevant attributes
 */
public int getAnzahlRelevanterAttribute() {
return 5;
}
}
