import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

public class Buchbeleg {
    private List<String> autoren;
    private Optional<Integer> erscheinungsjahr;
    private Optional<String> titel;
    private Optional<String> buchverlag;
    private Optional<String> erscheinungsort;
    private Seitenangabe seitenangabe;

    public Buchbeleg(List<String> autoren, Optional<Integer> erscheinungsjahr, Optional<String> titel, Optional<String> buchverlag, Optional<String> erscheinungsort, Optional<Integer> anfang, Optional<Integer> ende){
        setAutoren(autoren);
        setErscheinungsjahr(erscheinungsjahr);
        setTitel(titel);
        setBuchverlag(buchverlag);
        setErscheinungsort(erscheinungsort);
        setSeitenangabe(anfang, ende);
    }

    private void setAutoren(List<String> autoren) {
        this.autoren = new ArrayList<>();

        for(String autor : autoren) {
            if(!autor.isBlank()) {
                this.autoren.add(autor);
            }
        }
    }

    private void setErscheinungsjahr(Optional<Integer> erscheinungsjahr) {
        this.erscheinungsjahr = erscheinungsjahr;
    }

    private void setTitel(Optional<String> titel) {
        String wert = titel.orElse("");
        this.titel = wert.isBlank() ? Optional.empty() : titel;
    }

    private void setBuchverlag(Optional<String> buchverlag) {
        String wert = buchverlag.orElse("");
        this.buchverlag = wert.isBlank() ? Optional.empty() : buchverlag;
    }

    private void setErscheinungsort(Optional<String> erscheinungsort) {
        String wert = erscheinungsort.orElse("");
        this.erscheinungsort = wert.isBlank() ? Optional.empty() : erscheinungsort;
    }

    private void setSeitenangabe(Optional<Integer> anfang, Optional<Integer> ende) throws IllegalArgumentException {
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
                return ergebnis += "et al.";
            }

            if(i < this.autoren.size() - 1) {
                ergebnis += this.autoren.get(i) + ", ";
                continue;
            }
            ergebnis += this.autoren.get(i);
        }

        return ergebnis;
    }

    public String getErscheinungsjahr() {
        return this.erscheinungsjahr.isPresent() ? 
            this.erscheinungsjahr.get().toString() : "o.E.";
    }

    public String getTitel() {
        return this.titel.orElse("o.T.");
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

    public String getBelegLang() {
        return String.join(", ", 
                getAutoren(), 
                "(" + getErscheinungsjahr() + ")", 
                getTitel(), 
                getErscheinungsort(), 
                getBuchverlag());
    }

    public String getBelegKurz() {
        String beleg = getAutoren() + ", (" + getErscheinungsjahr() + ")";
        String seiten = getSeitenangabe();

        return seiten.isEmpty() ? beleg : beleg + ", " + seiten;
    }
}
