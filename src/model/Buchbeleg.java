
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Buchbeleg extends Literatur {
private Optional<String> buchverlag = Optional.empty();
private Optional<Integer> auflage = Optional.empty();
private Optional<String> erscheinungsort = Optional.empty();
private Seitenangabe seitenangabe;

public Buchbeleg(List<String> autor, Optional<LocalDate> veroeffentlichungsdatum,Optional<String> titel, Optional<String> buchverlag,Optional<Integer> auflage, Optional<String> erscheinungsort,Optional<Integer> anfang, Optional<Integer> ende) {

setAutoren(autor);
setErscheinungsdatum(veroeffentlichungsdatum);
setTitel(titel);
setAuflage(auflage);
setBuchverlag(buchverlag);
setErscheinungsort(erscheinungsort);
setSeitenangabe(anfang, ende);
}

public Buchbeleg(List<String> autoren, Optional<LocalDate> erscheinungsjahr,Optional<Integer> anfang, Optional<Integer> ende) {setAutoren(autoren);
setErscheinungsdatum(erscheinungsjahr);
setSeitenangabe(anfang, ende);
}

public void setAutoren(List<String> autoren) {
List<String> bereinigt = new ArrayList<>();

if (autoren == null) {
    setAutor(Optional.empty());
    return;
}

for (String autor : autoren) {
if (autor != null && !autor.isBlank()) {
    bereinigt.add(autor.trim());
    }
}
setAutor(bereinigt.isEmpty() ? Optional.empty() : Optional.of(bereinigt));
}

public void setErscheinungsdatum(Optional<LocalDate> erscheinungsdatum) {
if (erscheinungsdatum.isPresent()&& erscheinungsdatum.get().isAfter(LocalDate.now())) {
throw new IllegalArgumentException("Das Veröffentlichungsdatum darf nicht in der Zukunft liegen."
    );
}

setVeroeffentlichungsdatum(erscheinungsdatum);
}

public void setTitel(Optional<String> titel) {
super.setTitel(titel.map(String::trim).filter(wert -> !wert.isBlank()));
}

public void setAuflage(Optional<Integer> auflage) {
if (auflage.isPresent() && auflage.get() <= 0) {
    throw new IllegalArgumentException("Die Auflage muss größer als 0 sein.");
}
this.auflage = auflage;
}

public void setBuchverlag(Optional<String> buchverlag) {
String wert = buchverlag.orElse("");
this.buchverlag = wert.isBlank()? Optional.empty(): Optional.of(wert.trim());
}

public void setErscheinungsort(Optional<String> erscheinungsort) {
String wert = erscheinungsort.orElse("");
this.erscheinungsort = wert.isBlank()? Optional.empty(): Optional.of(wert.trim());
}

public void setSeitenangabe(Optional<Integer> anfang, Optional<Integer> ende) {
this.seitenangabe = new Seitenangabe(anfang, ende);
}

public String getAutoren() {
List<String> autoren = getAutor().orElse(List.of());
return autoren.isEmpty() ? "o.A." : formatiereAutoren(autoren);
}

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

public String getErscheinungsjahr() {
return getVeroeffentlichungsdatum().map(datum -> Integer.toString(datum.getYear())).orElse("o.J.");
}

public String getAuflage() {
return this.auflage.isPresent() ? this.auflage.get().toString() : "";
}

public String getBuchverlag() {
return this.buchverlag.orElse("o.V.");
}

public String getErscheinungsort() {
return this.erscheinungsort.orElse("o.O.");
}

public String getSeitenangabe() {
return this.seitenangabe.getSeitenangabe();
}

@Override
public String generiereKurzbeleg() {
String beleg = getAutoren() + ", " + getErscheinungsjahr();
String seiten = getSeitenangabe();

return seiten.isEmpty()? "(" + beleg + ")": "(" + beleg + ", S." + seiten + ")";
}

@Override
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
public int getAnzahlAusgefuellterAttribute() {
int anzahl = 0;

if (getAutor().orElse(List.of()).stream().anyMatch(a -> a != null && !a.isBlank())) {
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
public int getAnzahlRelevanterAttribute() {
return 5;
}
}
