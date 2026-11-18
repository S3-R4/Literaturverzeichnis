package model.Audiovisuell;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import model.Literatur;

public abstract class Audiovisuell extends Literatur {
    protected Optional<String> beitragsArt = Optional.empty();
    protected Optional<String> medium = Optional.empty();
    protected Optional<String> url = Optional.empty();

    @Override
    public void setAutor(List<String> autoren) {
        Objects.requireNonNull(autoren, "autoren");
        List<String> bereinigt = new ArrayList<>();
        for (String wert : autoren) {
            if (wert != null && !wert.isBlank()) {
                bereinigt.add(wert.trim());
            }
        }
        super.setAutor(bereinigt);
    }

    @Override
    public int getAnzahlAusgefuellterAttribute() {
        int anzahl = 0;
        if (autor.stream().anyMatch(name -> name != null && !name.isBlank())) anzahl++;
        if (hatText(titel)) anzahl++;
        if (veroeffentlichungsdatum.isPresent()) anzahl++;
        if (abrufdatum.isPresent()) anzahl++;
        if (hatText(beitragsArt)) anzahl++;
        if (hatText(medium)) anzahl++;
        if (hatText(url)) anzahl++;
        return anzahl;
    }

    @Override
    public int getAnzahlRelevanterAttribute() {
        return 7;
    }

    protected static boolean hatText(Optional<String> text) {
        return text.filter(wert -> !wert.isBlank()).isPresent();
    }

    // Bestehende Schreibweise als Alias zur geerbten Methode beibehalten.
    public void setVeroffentlichungsdatum(Optional<LocalDate> datum) {
        setVeroeffentlichungsdatum(datum);
    }

    public Optional<String> getBeitragsArt() {
        return beitragsArt;
    }

    public void setBeitragsArt(Optional<String> beitragsArt) {
        this.beitragsArt = Objects.requireNonNull(beitragsArt, "beitragsArt");
    }

    public Optional<String> getMedium() {
        return medium;
    }

    public void setMedium(Optional<String> medium) {
        this.medium = Objects.requireNonNull(medium, "medium");
    }

    public Optional<String> getUrl() {
        return url;
    }

    public void setUrl(Optional<String> url) {
        this.url = Objects.requireNonNull(url, "url");
    }

    public Optional<LocalDate> getAbruffDatum() {
        return getAbrufdatum();
    }

    public void setAbruffDatum(Optional<LocalDate> datum) {
        setAbrufdatum(datum);
    }

    public abstract String datumFormatierung(Optional<LocalDate> datum);

    public String getAutorsString(List<String> autors) {
        return String.join(" ,", autors);
    }
}
