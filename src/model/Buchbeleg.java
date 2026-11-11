package model;

import java.util.Optional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Buchbeleg extends Literatur {
    private Optional<String> buchverlag;
    private Optional<Integer> auflage;
    private Optional<String> erscheinungsort;
    private Seitenangabe seitenangabe;

    public Buchbeleg(List<String> autor, Optional<LocalDate> veroeffentlichungsdatum, Optional<String> titel, Optional<String> buchverlag, Optional<Integer> auflage, Optional<String> erscheinungsort, Optional<Integer> anfang, Optional<Integer> ende){
        setAutoren(autor);
        setErscheinungsdatum(veroeffentlichungsdatum);
        setTitel(titel);
        setAuflage(auflage);
        setBuchverlag(buchverlag);
        setErscheinungsort(erscheinungsort);
        setSeitenangabe(anfang, ende);
    }

    public Buchbeleg(List<String> autoren, Optional<LocalDate> erscheinungsjahr, Optional<Integer> anfang, Optional<Integer> ende) { //Kurzbeleg
        setAutoren(autoren);
        setErscheinungsdatum(erscheinungsjahr);
        setSeitenangabe(anfang, ende);
    }

    public void setAutoren(List<String> autoren) {
        this.autor = new ArrayList<>();

        for(String autor : autoren) {
            if(!autor.isBlank()) {
                this.autor.add(autor.trim());
            }
        }
    }

    public void setErscheinungsdatum(Optional<LocalDate> erscheinungsdatum) throws IllegalArgumentException {
        if(erscheinungsdatum.isPresent() && erscheinungsdatum.get().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Das Veröffentlichungsdatum darf nicht in der Zukunft liegen.");
        }
        this.veroeffentlichungsdatum = erscheinungsdatum;
    }

    public void setTitel(Optional<String> titel) {
        String wert = titel.orElse("");
        this.titel = wert.isBlank() ? Optional.empty() : Optional.of(wert.trim());
    }

    public void setAuflage(Optional<Integer> auflage) throws IllegalArgumentException {
        if(auflage.isPresent() && auflage.get() <= 0) {
            throw new IllegalArgumentException("Die Auflage muss größer als 0 sein.");
        }
        this.auflage = auflage;
    }

    public void setBuchverlag(Optional<String> buchverlag) {
        String wert = buchverlag.orElse("");
        this.buchverlag = wert.isBlank() ? Optional.empty() : buchverlag;
    }

    public void setErscheinungsort(Optional<String> erscheinungsort) {
        String wert = erscheinungsort.orElse("");
        this.erscheinungsort = wert.isBlank() ? Optional.empty() : erscheinungsort;
    }

    public void setSeitenangabe(Optional<Integer> anfang, Optional<Integer> ende) throws IllegalArgumentException {
        this.seitenangabe = new Seitenangabe(anfang, ende);
    }

    public String getAutoren() {
        return this.autor.isEmpty() ? 
            "o.A." : formatiereAutoren();
    }

    private String formatiereAutoren() {
        final int MAX_AUTORENANZAHL = 3;
        String ergebnis = "";

        for(int i = 0; i < this.autor.size(); i++) {
            if(i >= MAX_AUTORENANZAHL) {
                return ergebnis += ", et al.";
            }

            if(i > 0) {
                ergebnis += 
                    i == this.autor.size() - 1 ? 
                    " & " : ", ";
            }
            ergebnis += this.autor.get(i);
        }

        return ergebnis;
    }

    public String getErscheinungsjahr() {
        return this.veroeffentlichungsdatum.isPresent() ? 
            String.valueOf(this.veroeffentlichungsdatum.get().getYear()) : "o.J.";
    }
    
    public String getTitel() {
        return this.titel.orElse("o.T.");
    }

    public String getAuflage() {
        return this.auflage.isPresent() ? 
            this.auflage.get().toString() : "";
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

        return seiten.isEmpty() ? "(" + beleg + ")" : "(" + beleg + ", " + "S." + seiten + ")";
	}

	@Override
	public String generiereLangbeleg() {
		if(this.titel.isEmpty() || this.buchverlag.isEmpty()) {
            throw new IllegalArgumentException("Für einen Langbeleg müssen Titel und Verlag angegeben sein.");
        }

        String titelMitAuflage = getTitel() + ".";
        if(this.auflage.isPresent()) {
            titelMitAuflage += " " + getAuflage() + ". Aufl.";
        }

        return String.join(" ", 
                getAutoren(),
                "(" + getErscheinungsjahr() + "): " +
                titelMitAuflage,
                getErscheinungsort() + ": " +
                getBuchverlag() + "."
                );
    }
}
