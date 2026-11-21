package model.Audiovisuell;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import model.Literatur;

/**
 * Provides shared metadata and formatting helpers for audiovisual sources.
 */
public abstract class Audiovisuell extends Literatur {
    protected Optional<String> beitragsArt = Optional.empty();
    protected Optional<String> medium = Optional.empty();
    protected Optional<String> url = Optional.empty();

    /**
     * Sets the author list.
     *
     * @param autoren author list
     */
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
    /**
     * Counts populated attributes used to measure source completeness.
     *
     * @return the number of populated relevant attributes
     */
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
    /**
     * Returns the number of attributes considered for source completeness.
     *
     * @return the total number of relevant attributes
     */
    public int getAnzahlRelevanterAttribute() {
        return 7;
    }

    /**
     * Checks whether an optional string contains nonblank text.
     *
     * @param text text
     * @return whether nonblank text is present
     */
    protected static boolean hatText(Optional<String> text) {
        return text.filter(wert -> !wert.isBlank()).isPresent();
    }

    // Bestehende Schreibweise als Alias zur geerbten Methode beibehalten.
    /**
     * Sets the publication date.
     *
     * @param datum date
     */
    public void setVeroffentlichungsdatum(Optional<LocalDate> datum) {
        setVeroeffentlichungsdatum(datum);
    }

    /**
     * Returns the contribution type.
     *
     * @return the contribution type
     */
    public Optional<String> getBeitragsArt() {
        return beitragsArt;
    }

    /**
     * Sets the contribution type.
     *
     * @param beitragsArt contribution type
     */
    public void setBeitragsArt(Optional<String> beitragsArt) {
        this.beitragsArt = Objects.requireNonNull(beitragsArt, "beitragsArt");
    }

    /**
     * Returns the medium.
     *
     * @return the medium
     */
    public Optional<String> getMedium() {
        return medium;
    }

    /**
     * Sets the medium.
     *
     * @param medium medium
     */
    public void setMedium(Optional<String> medium) {
        this.medium = Objects.requireNonNull(medium, "medium");
    }

    /**
     * Returns the URL.
     *
     * @return the URL
     */
    public Optional<String> getUrl() {
        return url;
    }

    /**
     * Sets the URL.
     *
     * @param url URL
     */
    public void setUrl(Optional<String> url) {
        this.url = Objects.requireNonNull(url, "url");
    }

    /**
     * Returns the retrieval date.
     *
     * @return the retrieval date
     */
    public Optional<LocalDate> getAbruffDatum() {
        return getAbrufdatum();
    }

    /**
     * Sets the retrieval date.
     *
     * @param datum date
     */
    public void setAbruffDatum(Optional<LocalDate> datum) {
        setAbrufdatum(datum);
    }

    /**
     * Formats a publication date according to this source type.
     *
     * @param datum date
     * @return the formatted date, or an empty string when absent
     */
    public abstract String datumFormatierung(Optional<LocalDate> datum);

    /**
     * Joins author names using the audiovisual citation separator.
     *
     * @param autors author list
     * @return the joined author names
     */
    public String getAutorsString(List<String> autors) {
        return String.join(" ,", autors);
    }
}
