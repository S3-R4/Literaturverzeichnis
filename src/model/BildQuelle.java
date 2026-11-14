package model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BildQuelle extends Literatur {

private String Medienart = "";
private String Herausgeber = "";
private String Anbieter = "";
private boolean MehrUrheber;

public BildQuelle() {
autor = new ArrayList<>();
for (int i = 0; i < 4; i++) {
autor.add("");}
}

public BildQuelle(String urheber1, String urheber2,String urheber3, String urheber4, boolean mehrurheber,Optional<String> titel, String medienart, Optional<LocalDate> veroeffentlichungsdatum,String herausgeber, String anbieter, String abrufdatum) {
this();

setUrheber1(urheber1);
setUrheber2(urheber2);
setUrheber3(urheber3);
setUrheber4(urheber4);

setMedienart(medienart);
setHerausgeber(herausgeber);
setAnbieter(anbieter);
this.MehrUrheber = mehrurheber;

this.titel = titel == null ? Optional.empty() : titel;
this.veroeffentlichungsdatum = veroeffentlichungsdatum == null? Optional.empty(): veroeffentlichungsdatum;


if (abrufdatum != null && !abrufdatum.trim().isEmpty()) {
setAbrufdatum(abrufdatum);
}
}

public List<String> validiere() {
List<String> fehler = new ArrayList<>();

if (titel == null) {
fehler.add(Fehlermeldungen.getAllgemeinerFehler());

    } else if (titel.isPresent()) {
    String fehlerText = Validierung.pruefeText(titel.get());

    if (fehlerText != null) {
    fehler.add(fehlerText);
        }
    }

    return fehler;
}

public String getUrheber1() {
if (autor.get(0).equals("")) {
return "Unbekannter Autor";
        }
    return autor.get(0);
    }

public void setUrheber1(String urheber1) {
autor.set(0, urheber1 == null ? "" : urheber1);
}

public String getUrheber2() {
if (autor.get(1).equals("")) {
return "";
    }
return ", " + autor.get(1);
}

public void setUrheber2(String urheber2) {
autor.set(1, urheber2 == null ? "" : urheber2);
    }

public String getUrheber3() {
if (autor.get(2).equals("")) {
return "";
    }
return ", " + autor.get(2);
}

public void setUrheber3(String urheber3) {
autor.set(2, urheber3 == null ? "" : urheber3);
    }

public String getUrheber4() {
if (autor.get(3).equals("")) {
    return "";
        }
    return ", " + autor.get(3);
    }

public void setUrheber4(String urheber4) {
autor.set(3, urheber4 == null ? "" : urheber4);
    }

public String getMehrUrheber(boolean mehrurheber) {
if (mehrurheber) {
return " et al.";
    }
    return "";
}

public void setMehrUrheber(boolean mehrurheber) {
MehrUrheber = mehrurheber;
}

public String getTitel() {
return titel == null ? "" : titel.orElse("");
}

public void setTitel(String titel) {
this.titel = titel == null? Optional.empty(): Optional.of(titel.trim());
}

public String getMedienart() {
if (Medienart == null || Medienart.isEmpty()) {
return "";
    }
return "[" + Medienart + "]. ";
}

public void setMedienart(String medienart) {
Medienart = medienart == null ? "" : medienart;
}

public String getErscheinungsjahr() {
if (veroeffentlichungsdatum == null || veroeffentlichungsdatum.isEmpty()) {
return "";
}
return " (" + veroeffentlichungsdatum.get().getYear() + "). ";
}

public String setErscheinungsjahr(String erscheinungsjahr) {
if (erscheinungsjahr == null || erscheinungsjahr.trim().isEmpty()) {
veroeffentlichungsdatum = Optional.empty();
    return null;
    }

erscheinungsjahr = erscheinungsjahr.trim();
String fehler = Validierung.pruefeJahr(erscheinungsjahr);

    if (fehler != null) {
    return fehler;
    }
    veroeffentlichungsdatum = Optional.of(LocalDate.of(Integer.parseInt(erscheinungsjahr), 1, 1));
    return null;
}

public String getHerausgeber() {
if (Herausgeber.equals("")) {
    return "";
    }
    return Herausgeber + ". ";
}

public void setHerausgeber(String herausgeber) {
Herausgeber = herausgeber == null ? "" : herausgeber;
}

public String getAnbieter() {
return Anbieter;
}

public void setAnbieter(String anbieter) {
Anbieter = anbieter == null ? "" : anbieter;
}

public String getAbrufdatum() {
    if (abrufdatum == null) {
        return "";
    }

    return "[" + abrufdatum.format(java.time.format.DateTimeFormatter.ofPattern("d.M.yyyy")) + "].";
}

public String setAbrufdatum(String abrufdatum) {
if (abrufdatum == null || abrufdatum.trim().isEmpty()) {
    this.abrufdatum = null;
    return null;
    }
abrufdatum = abrufdatum.trim();
String fehler = Validierung.pruefeDatum(abrufdatum);

if (fehler != null) {
return fehler;
    }

this.abrufdatum = LocalDate.parse(abrufdatum,java.time.format.DateTimeFormatter.ofPattern("d.M.uuuu"));

    return null;
}

public String getbeleg() {
return getUrheber1() + getUrheber2() + getUrheber3() + getUrheber4() + getMehrUrheber(MehrUrheber) + getErscheinungsjahr() + getTitel() + getMedienart() + getHerausgeber() + getAnbieter() + getAbrufdatum();
}

@Override
public String generiereKurzbeleg() {
    if (veroeffentlichungsdatum == null || veroeffentlichungsdatum.isEmpty()) { 
    return "(" + getUrheber1() + ")";
    }

    return "(" + getUrheber1() + ", "+ veroeffentlichungsdatum.get().getYear() + ")";
}

@Override
public String generiereLangbeleg() { 
return getbeleg();
}
}