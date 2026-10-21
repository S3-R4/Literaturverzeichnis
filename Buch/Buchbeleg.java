import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

public class Buchbeleg {
    private List<String> autoren;
    private Optional<Integer> erscheinungsjahr;
    private Optional<String> titel;
    private Optional<String> buchverlag;
    private Optional<Integer> auflage;
    private Optional<String> erscheinungsort;
    private Seitenangabe seitenangabe;

    public Buchbeleg(List<String> autoren, Optional<Integer> erscheinungsjahr, Optional<String> titel, Optional<String> buchverlag, Optional<Integer> auflage, Optional<String> erscheinungsort, Optional<Integer> anfang, Optional<Integer> ende){
        setAutoren(autoren);
        setErscheinungsjahr(erscheinungsjahr);
        setTitel(titel);
        setAuflage(auflage);
        setBuchverlag(buchverlag);
        setErscheinungsort(erscheinungsort);
        setSeitenangabe(anfang, ende);
    }

    public Buchbeleg(List<String> autoren, Optional<Integer> erscheinungsjahr, Optional<Integer> anfang, Optional<Integer> ende) { //Kurzbeleg
        setAutoren(autoren);
        setErscheinungsjahr(erscheinungsjahr);
        setSeitenangabe(anfang, ende);
    }

    public void setAutoren(List<String> autoren) {
        this.autoren = new ArrayList<>();

        for(String autor : autoren) {
            if(!autor.isBlank()) {
                this.autoren.add(autor.trim());
            }
        }
    }

    public void setErscheinungsjahr(Optional<Integer> erscheinungsjahr) throws IllegalArgumentException {
        if(erscheinungsjahr.isPresent() && erscheinungsjahr.get() <= 0) {
            throw new IllegalArgumentException("Das Erscheinungsjahr muss größer als 0 sein.");
        }
        this.erscheinungsjahr = erscheinungsjahr;
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
        return this.autoren.isEmpty() ? 
            "o.A." : formatiereAutoren();
    }

    private String formatiereAutoren() {
        final int MAX_AUTORENANZAHL = 3;
        String ergebnis = "";

        for(int i = 0; i < this.autoren.size(); i++) {
            if(i >= MAX_AUTORENANZAHL) {
                return ergebnis += ", et al.";
            }

            if(i > 0) {
                ergebnis += 
                    i == this.autoren.size() - 1 ? 
                    " & " : ", ";
            }
            ergebnis += this.autoren.get(i);
        }

        return ergebnis;
    }

    public String getErscheinungsjahr() {
        return this.erscheinungsjahr.isPresent() ? 
            this.erscheinungsjahr.get().toString() : "o.J.";
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

    public String getBelegLang() throws IllegalArgumentException {
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

    public String getBelegKurz() {
        String beleg = getAutoren() + ", " + getErscheinungsjahr();
        String seiten = getSeitenangabe();

        return seiten.isEmpty() ? "(" + beleg + ")" : "(" + beleg + ", " + "S." + seiten + ")";
    }
}
