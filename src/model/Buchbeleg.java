
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Buchbeleg extends Literatur {
private Optional<String> buchverlag;
private Optional<Integer> auflage;
private Optional<String> erscheinungsort;
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
this.autor = new ArrayList<>();

if (autoren == null) return;

for (String autor : autoren) {
if (autor != null && !autor.isBlank()) {
    this.autor.add(autor.trim());
    }
}
}

public void setErscheinungsdatum(Optional<LocalDate> erscheinungsdatum) {
if (erscheinungsdatum.isPresent()&& erscheinungsdatum.get().isAfter(LocalDate.now())) {
throw new IllegalArgumentException("Das Veröffentlichungsdatum darf nicht in der Zukunft liegen."
    );
}

this.veroeffentlichungsdatum = erscheinungsdatum.orElse(null);
}

public void setTitel(Optional<String> titel) {
String wert = titel.orElse("");
this.titel = wert.isBlank() ? null : wert.trim();
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
return this.autor.isEmpty() ? "o.A." : formatiereAutoren();
}

private String formatiereAutoren() {
final int MAX_AUTORENANZAHL = 3;
String ergebnis = "";

for (int i = 0; i < this.autor.size(); i++) {
    if (i >= MAX_AUTORENANZAHL) {
        return ergebnis + ", et al.";
    }

    if (i > 0) {
        ergebnis += i == this.autor.size() - 1 ? " & " : ", ";
    }

    ergebnis += this.autor.get(i);
}

return ergebnis;
}

public String getErscheinungsjahr() {
return this.veroeffentlichungsdatum != null? String.valueOf(this.veroeffentlichungsdatum.getYear()): "o.J.";
}

@Override
public String getTitel() {
return this.titel == null || this.titel.isBlank() ? "o.T." : this.titel;
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
if (this.titel == null || this.titel.isBlank()|| this.buchverlag.isEmpty()) {throw new IllegalArgumentException("Für einen Langbeleg müssen Titel und Verlag angegeben sein.");
}

String titelMitAuflage = getTitel() + ".";

if (this.auflage.isPresent()) {
    titelMitAuflage += " " + getAuflage() + ". Aufl.";
}

return String.join(" ",getAutoren(),"(" + getErscheinungsjahr() + "): " + titelMitAuflage,getErscheinungsort() + ": " + getBuchverlag() + "."
);
}

@Override
public int getAnzahlAusgefuellterAttribute() {
int anzahl = 0;

if (autor != null && autor.stream().anyMatch(a -> a != null && !a.isBlank())) {
anzahl++;
}

if (veroeffentlichungsdatum != null) {
anzahl++;
}

if (titel != null && !titel.isBlank()) {
anzahl++;
}

if (buchverlag != null && buchverlag.isPresent()&& !buchverlag.get().isBlank()) {
    anzahl++;
}

if (erscheinungsort != null && erscheinungsort.isPresent()&& !erscheinungsort.get().isBlank()) {
    anzahl++;
}

return anzahl;
}

@Override
public int getAnzahlRelevanterAttribute() {
return 5;
}
}
