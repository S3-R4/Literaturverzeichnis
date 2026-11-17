
package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BildQuelle extends Literatur {

private Optional<String> Medienart = Optional.empty();
private Optional<String> Herausgeber = Optional.empty();
private Optional<String> Anbieter = Optional.empty();
private boolean MehrUrheber;

public BildQuelle() {
autor = new ArrayList<>();

for (int i = 0; i < 4; i++) {
autor.add("");
}

titel = Optional.empty();
veroeffentlichungsdatum = Optional.empty();
abrufdatum = Optional.of(LocalDate.now());
}

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

public List<String> validiere() {
List<String> fehler = new ArrayList<>();
Optional<String> fehlerText = Validierung.pruefeText(titel);
fehlerText.ifPresent(fehler::add);

return fehler;
}

private String urheber(int index) {
List<String> urheber = autor;

return index < urheber.size() && urheber.get(index) != null
? urheber.get(index) : "";
}

private void setUrheber(int index, String name) {
while (autor.size() <= index) {
autor.add("");
    }

    autor.set(index, name == null ? "" : name.trim());
}

public String getUrheber1() {
String name = urheber(0);
return name.isBlank() ? "Unbekannter Autor" : name;
}

public void setUrheber1(String name) {
setUrheber(0, name);
}

public String getUrheber2() {
String name = urheber(1);
return name.isBlank() ? "" : ", " + name;
}

public void setUrheber2(String name) {
setUrheber(1, name);
}

public String getUrheber3() {
String name = urheber(2);
return name.isBlank() ? "" : ", " + name;
}

public void setUrheber3(String name) {
setUrheber(2, name);
}

public String getUrheber4() {
String name = urheber(3);
return name.isBlank() ? "" : ", " + name;
}

public void setUrheber4(String name) {
setUrheber(3, name);
}

public String getMehrUrheber(boolean mehrurheber) {
return mehrurheber ? " et al." : "";
}

public void setMehrUrheber(boolean mehrurheber) {
MehrUrheber = mehrurheber;
}

@Override
public void setTitel(Optional<String> titel) {
this.titel = titel == null
? Optional.empty()
: titel.map(String::trim)
        .filter(wert -> !wert.isBlank());
}

public String getMedienart() {
return Medienart.map(wert -> "[" + wert + "]. ").orElse("");
}

public void setMedienart(Optional<String> medienart) {
Medienart = medienart == null
? Optional.empty()
: medienart.map(String::trim)
            .filter(wert -> !wert.isBlank());
}

public String getErscheinungsjahr() {
return veroeffentlichungsdatum
.map(datum -> " (" + datum.getYear() + "). ")
.orElse("");
}

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

public void setVeroeffentlichungsdatum(
Optional<LocalDate> veroeffentlichungsdatum) {

this.veroeffentlichungsdatum = veroeffentlichungsdatum == null? Optional.empty() : veroeffentlichungsdatum;
}

public String getHerausgeber() {
return Herausgeber.map(wert -> wert + ". ").orElse("");
}

public void setHerausgeber(Optional<String> herausgeber) {
Herausgeber = herausgeber == null? Optional.empty(): herausgeber.map(String::trim).filter(wert -> !wert.isBlank());
}

public Optional<String> getAnbieter() {
return Anbieter;
}

public void setAnbieter(Optional<String> anbieter) {
Anbieter = anbieter == null? Optional.empty(): anbieter.map(String::trim).filter(wert -> !wert.isBlank());
}

public String DatumFormatierung() {
return abrufdatum.map(datum ->"[" + datum.format(DateTimeFormatter.ofPattern("d.M.yyyy")) + "].").orElse("");
}

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
public String generiereKurzbeleg() {
return "(" + getUrheber1()+ veroeffentlichungsdatum.map(datum -> ", " + datum.getYear()).orElse("")+ ")";
}

@Override
public String generiereLangbeleg() {
return getbeleg();
}

// US05: Erfassungsstand berechnen

@Override
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
public int getAnzahlRelevanterAttribute() {
return 5;
}
}
