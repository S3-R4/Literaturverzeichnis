import java.util.Optional;

public class Seitenangabe {
    private Optional<Integer> anfangsseite;
    private Optional<Integer> endseite;

    public Seitenangabe(Optional<Integer> anfangsseite, Optional<Integer> endseite) {
        this.setSeitenangabe(anfangsseite, endseite);
    }

    public void setSeitenangabe(Optional<Integer> anfangseite, Optional<Integer> endseite) throws IllegalArgumentException {
        if(anfangseite.isEmpty() && endseite.isPresent()) {
            throw new IllegalArgumentException("Eine Endseite benötigt eine Anfangsseite");
        }

        if(anfangseite.isPresent()) {
            int anfang = anfangseite.get();
            int ende = endseite.orElse(anfang);

            if(anfang < 1 || ende < anfang) {
                throw new IllegalArgumentException("Ungueltiger Seitenbereich");
            }
        }
        
        this.anfangsseite = anfangseite;
        this.endseite = endseite;
    }

    public void setAnfangseite(Optional<Integer> seite) {
        setSeitenangabe(seite, this.endseite);
    }

    public void setEndseite(Optional<Integer> seite) {
        setSeitenangabe(this.anfangsseite, seite);
    }

    public String getSeitenangabe() {
        if(this.anfangsseite.isEmpty()) {
            return "";
        }

        int anfang = this.anfangsseite.get();
        int ende = this.endseite.orElse(anfang);

        return anfang == ende
        ? String.valueOf(anfang)
        : anfang + "-" + ende; 
    }

    public Optional<Integer> getAnfangseite() {
        return this.anfangsseite;
    }

    public Optional<Integer> getEndseite() {
        return this.endseite;
    }
}
