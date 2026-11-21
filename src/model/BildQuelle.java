
package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Stores image credits and publication metadata for image citations.
 */
public class BildQuelle extends Literatur {

private Optional<String> Medienart = Optional.empty();
private Optional<String> Herausgeber = Optional.empty();
private Optional<String> Anbieter = Optional.empty();
private boolean MehrUrheber;

/**
 * Creates a BildQuelle instance.
 */
public BildQuelle() {
autor = new ArrayList<>();

for (int i = 0; i < 4; i++) {
autor.add("");
}

titel = Optional.empty();
veroeffentlichungsdatum = Optional.empty();
abrufdatum = Optional.of(LocalDate.now());
}

/**
 * Creates a BildQuelle instance with the supplied metadata.
 *
 * @param urheber1 creator 1 name
 * @param urheber2 creator 2 name
 * @param urheber3 creator 3 name
 * @param urheber4 creator 4 name
 * @param mehrurheber whether additional creators should be indicated
 * @param titel title
 * @param medienart media type
 * @param veroeffentlichungsdatum publication date
 * @param herausgeber editor information
 * @param anbieter provider
 */
public BildQuelle(String urheber1, String urheber2,String urheber3, String urheber4,boolean mehrurheber, Optional<String> titel,Optional<String> medienart,Optional<LocalDate> veroeffentlichungsdatum,Optional<String> herausgeber,Optional<String> anbieter) {
this();

setUrheber1(urheber1);
setUrheber2(urheber2);
setUrheber3(urheber3);
setUrheber4(urheber4);

setMedienart(medienart);
setHerausgeber(herausgeber);
setAnbieter(anbieter);

this.MehrUrheber = mehrurheber;
setTitel(titel);
setVeroeffentlichungsdatum(veroeffentlichungsdatum);
}

/**
 * Checks the image title and collects validation errors.
 *
 * @return validation errors, or an empty list when valid
 */
public List<String> validiere() {
List<String> fehler = new ArrayList<>();
Optional<String> fehlerText = Validierung.pruefeText(titel);
fehlerText.ifPresent(fehler::add);

return fehler;
}

/**
 * Reads a creator name, using an empty string for missing entries.
 *
 * @param index zero-based index
 * @return the creator name or an empty string
 */
private String urheber(int index) {
List<String> urheber = autor;

return index < urheber.size() && urheber.get(index) != null
? urheber.get(index) : "";
}

/**
 * Stores a trimmed creator name, expanding the list as needed.
 *
 * @param index zero-based index
 * @param name name
 */
private void setUrheber(int index, String name) {
while (autor.size() <= index) {
autor.add("");
    }

    autor.set(index, name == null ? "" : name.trim());
}

/**
 * Returns the creator 1 name.
 *
 * @return the creator 1 name
 */
public String getUrheber1() {
String name = urheber(0);
return name.isBlank() ? "Unbekannter Autor" : name;
}

/**
 * Sets the creator 1 name.
 *
 * @param name name
 */
public void setUrheber1(String name) {
setUrheber(0, name);
}

/**
 * Returns the creator 2 name.
 *
 * @return the creator 2 name
 */
public String getUrheber2() {
String name = urheber(1);
return name.isBlank() ? "" : ", " + name;
}

/**
 * Sets the creator 2 name.
 *
 * @param name name
 */
public void setUrheber2(String name) {
setUrheber(1, name);
}

/**
 * Returns the creator 3 name.
 *
 * @return the creator 3 name
 */
public String getUrheber3() {
String name = urheber(2);
return name.isBlank() ? "" : ", " + name;
}

/**
 * Sets the creator 3 name.
 *
 * @param name name
 */
public void setUrheber3(String name) {
setUrheber(2, name);
}

/**
 * Returns the creator 4 name.
 *
 * @return the creator 4 name
 */
public String getUrheber4() {
String name = urheber(3);
return name.isBlank() ? "" : ", " + name;
}

/**
 * Sets the creator 4 name.
 *
 * @param name name
 */
public void setUrheber4(String name) {
setUrheber(3, name);
}

/**
 * Formats the marker for additional creators.
 *
 * @param mehrurheber whether additional creators should be indicated
 * @return et al. with a leading space, or an empty string
 */
public String getMehrUrheber(boolean mehrurheber) {
return mehrurheber ? " et al." : "";
}

/**
 * Sets the whether additional creators should be indicated.
 *
 * @param mehrurheber whether additional creators should be indicated
 */
public void setMehrUrheber(boolean mehrurheber) {
MehrUrheber = mehrurheber;
}

/**
 * Sets the title.
 *
 * @param titel title
 */
@Override
public void setTitel(Optional<String> titel) {
this.titel = titel == null
? Optional.empty()
: titel.map(String::trim)
        .filter(wert -> !wert.isBlank());
}

/**
 * Returns the media type.
 *
 * @return the media type
 */
public String getMedienart() {
return Medienart.map(wert -> "[" + wert + "]. ").orElse("");
}

/**
 * Sets the media type.
 *
 * @param medienart media type
 */
public void setMedienart(Optional<String> medienart) {
Medienart = medienart == null
? Optional.empty()
: medienart.map(String::trim)
            .filter(wert -> !wert.isBlank());
}

/**
 * Returns the publication year.
 *
 * @return the publication year
 */
public String getErscheinungsjahr() {
return veroeffentlichungsdatum
.map(datum -> " (" + datum.getYear() + "). ")
.orElse("");
}

/**
 * Validates and stores the publication year, clearing it when input is absent.
 *
 * @param erscheinungsjahr publication year
 * @return an error message, or an empty optional on success
 */
public Optional<String> setErscheinungsjahr(Optional<String> erscheinungsjahr) {
if (erscheinungsjahr == null ||
erscheinungsjahr.map(String::trim).isEmpty()) {

veroeffentlichungsdatum = Optional.empty();
return Optional.empty();
}

erscheinungsjahr = erscheinungsjahr.map(String::trim);

Optional<String> fehler = Validierung.pruefeJahr(erscheinungsjahr);

if (fehler.isPresent()) {
return fehler;
}

veroeffentlichungsdatum = Optional.of(
LocalDate.of(Integer.parseInt(erscheinungsjahr.get()), 1, 1));

return Optional.empty();
}

/**
 * Sets the publication date.
 *
 * @param veroeffentlichungsdatum publication date
 */
public void setVeroeffentlichungsdatum(
Optional<LocalDate> veroeffentlichungsdatum) {

this.veroeffentlichungsdatum = veroeffentlichungsdatum == null? Optional.empty() : veroeffentlichungsdatum;
}

/**
 * Returns the editor information.
 *
 * @return the editor information
 */
public String getHerausgeber() {
return Herausgeber.map(wert -> wert + ". ").orElse("");
}

/**
 * Sets the editor information.
 *
 * @param herausgeber editor information
 */
public void setHerausgeber(Optional<String> herausgeber) {
Herausgeber = herausgeber == null? Optional.empty(): herausgeber.map(String::trim).filter(wert -> !wert.isBlank());
}

/**
 * Returns the provider.
 *
 * @return the provider
 */
public Optional<String> getAnbieter() {
return Anbieter;
}

/**
 * Sets the provider.
 *
 * @param anbieter provider
 */
public void setAnbieter(Optional<String> anbieter) {
Anbieter = anbieter == null? Optional.empty(): anbieter.map(String::trim).filter(wert -> !wert.isBlank());
}

/**
 * Formats the retrieval date in brackets using d.M.yyyy.
 *
 * @return the formatted retrieval date, or an empty string
 */
public String DatumFormatierung() {
return abrufdatum.map(datum ->"[" + datum.format(DateTimeFormatter.ofPattern("d.M.yyyy")) + "].").orElse("");
}

/**
 * Validates and stores the retrieval date, clearing it when input is absent.
 *
 * @param abrufdatumText retrieval date in day.month.year format
 * @return an error message, or an empty optional on success
 */
public Optional<String> setAbrufdatumText(Optional<String> abrufdatumText) {

Optional<String> datum = abrufdatumText == null? Optional.empty(): abrufdatumText.map(String::trim).filter(wert -> !wert.isBlank());
if (datum.isEmpty()) {
abrufdatum = Optional.empty();
return Optional.empty();
}

Optional<String> fehler = Validierung.pruefeDatum(datum);

if (fehler.isPresent()) {
return fehler;
}

abrufdatum = Optional.of(
LocalDate.parse(datum.get(),DateTimeFormatter.ofPattern("d.M.uuuu")));
return Optional.empty();
}

/**
 * Combines image credits and metadata into a full citation.
 *
 * @return the complete image citation
 */
public String getbeleg() {
return getUrheber1()
+ getUrheber2()
+ getUrheber3()
+ getUrheber4()
+ getMehrUrheber(MehrUrheber)
+ getErscheinungsjahr()
+ getTitel().orElse("")
+ getMedienart()
+ getHerausgeber()
+ getAnbieter().orElse("")
+ DatumFormatierung();
}

@Override
/**
 * Formats a short citation for this source.
 *
 * @return the short citation
 */
public String generiereKurzbeleg() {
return "(" + getUrheber1()+ veroeffentlichungsdatum.map(datum -> ", " + datum.getYear()).orElse("")+ ")";
}

@Override
/**
 * Formats a full bibliographic citation for this source.
 *
 * @return the full citation
 */
public String generiereLangbeleg() {
return getbeleg();
}

// US05: Erfassungsstand berechnen

@Override
/**
 * Counts populated attributes used to measure source completeness.
 *
 * @return the number of populated relevant attributes
 */
public int getAnzahlAusgefuellterAttribute() {
int anzahl = 0;

// Urheber
if (autor.stream().anyMatch(a -> a != null && !a.isBlank())) {
anzahl++;
}

// Titel
if (titel.isPresent()) {
anzahl++;
}

// Medienart
if (Medienart.isPresent()) {
anzahl++;
}

// Erscheinungsjahr
if (veroeffentlichungsdatum.isPresent()) {
anzahl++;
}

// Herausgeber oder Anbieter
if (Herausgeber.isPresent() || Anbieter.isPresent()) {
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
