import java.util.ArrayList;

public class Bildbelegclass extends Literatur {

private String Medienart = "";
private String Herausgeber = "";
private String Anbieter = "";
private boolean MehrUrheber;

public Bildbelegclass() {
autor = new ArrayList<>();
for (int i = 0; i < 4; i++) {
autor.add("");}
}

public Bildbelegclass(String urheber1, String urheber2,String urheber3, String urheber4, boolean mehrurheber,String titel, String medienart, String erscheinungsjahr,String herausgeber, String anbieter, String abrufdatum) {
this();

setUrheber1(urheber1);
setUrheber2(urheber2);
setUrheber3(urheber3);
setUrheber4(urheber4);

MehrUrheber = mehrurheber;
this.titel = titel;
Medienart = medienart;
Herausgeber = herausgeber;
Anbieter = anbieter;

if (erscheinungsjahr != null && !erscheinungsjahr.isEmpty()) {
this.veroeffentlichungsdatum =
java.time.LocalDate.of(
Integer.parseInt(erscheinungsjahr), 1, 1);
}

if (abrufdatum != null && !abrufdatum.isEmpty()) {
this.abrufdatum = java.time.LocalDate.parse(abrufdatum,java.time.format.DateTimeFormatter.ofPattern("d.M.yyyy"));
    }
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
return titel;
}

public void setTitel(String titel) {
this.titel = titel;
}

public String getMedienart() {
if (Medienart.equals("")) {
return "";
    }
return "[" + Medienart + "]. ";
}

public void setMedienart(String medienart) {
Medienart = medienart;
}

public String getErscheinungsjahr() {
if (veroeffentlichungsdatum == null) {
    return "";
        }
    return " (" + veroeffentlichungsdatum.getYear() + "). ";
    }

public void setErscheinungsjahr(String erscheinungsjahr) {
if (erscheinungsjahr == null || erscheinungsjahr.isEmpty()) {
veroeffentlichungsdatum = null;
    } else {veroeffentlichungsdatum = java.time.LocalDate.of(Integer.parseInt(erscheinungsjahr), 1, 1);
        }
    }

public String getHerausgeber() {
if (Herausgeber.equals("")) {
    return "";
    }
        return Herausgeber + ". ";
}

public void setHerausgeber(String herausgeber) {
Herausgeber = herausgeber;
}

public String getAnbieter() {
return Anbieter;
}

public void setAnbieter(String anbieter) {
Anbieter = anbieter;
}

public String getAbrufdatum() {
if (abrufdatum == null) {
return "";
        }
return "[" + abrufdatum.format(java.time.format.DateTimeFormatter.ofPattern("d.M.yyyy")) + "].";
    }

public void setAbrufdatum(String abrufdatum) {
if (abrufdatum == null || abrufdatum.isEmpty()) {
    this.abrufdatum = null;
        } else {
        this.abrufdatum = java.time.LocalDate.parse(
        abrufdatum,
        java.time.format.DateTimeFormatter.ofPattern("d.M.yyyy"));
    }
}

public String getbeleg() {
return getUrheber1() + getUrheber2() + getUrheber3() + getUrheber4() + getMehrUrheber(MehrUrheber) + getErscheinungsjahr() + getTitel() + getMedienart() + getHerausgeber() + getAnbieter() + getAbrufdatum();
}

@Override
public String generiereKurzbeleg() {
return "(" + getUrheber1() + ", "+ veroeffentlichungsdatum.getYear() + ")";
}

@Override
public String generiereLangbeleg() { 
return getbeleg();
}
}