
package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BildQuelle extends Literatur {

    private Optional<String> Medienart = Optional.empty();
    private Optional<String> Herausgeber = Optional.empty();
    private Optional<String> Anbieter = Optional.empty();
    private boolean MehrUrheber = false;

    public BildQuelle() {
        abrufdatum = Optional.of(LocalDate.now());
    }

    public BildQuelle(String urheber1, String urheber2, String urheber3, String urheber4, boolean mehrurheber,
            Optional<String> titel, Optional<String> medienart, Optional<LocalDate> veroeffentlichungsdatum,
            Optional<String> herausgeber, Optional<String> anbieter) {
        this();

        setUrheber1(urheber1);
        setUrheber2(urheber2);
        setUrheber3(urheber3);
        setUrheber4(urheber4);

        setMedienart(medienart);
        setHerausgeber(herausgeber);
        setAnbieter(anbieter);

        this.MehrUrheber = mehrurheber;
        setTitel(titel);

        setVeroeffentlichungsdatum(veroeffentlichungsdatum);

    }

    public List<String> validiere() {
        List<String> fehler = new ArrayList<>();

        if (getTitel().isPresent()) {
            Optional<String> fehlerText = Validierung.pruefeText(titel);

            if (fehlerText.isPresent()) {
                fehler.add(fehlerText.get());
            }
        }

        return fehler;
    }

    private String urheber(int index) {
        List<String> urheber = getAutor().orElse(List.of());
        return index < urheber.size() && urheber.get(index) != null ? urheber.get(index) : "";
    }

    private void setUrheber(int index, String name) {
        List<String> urheber = new ArrayList<>(getAutor().orElse(List.of()));
        while (urheber.size() <= index) {
            urheber.add("");
        }
        urheber.set(index, name == null ? "" : name);
        setAutor(Optional.of(urheber));
    }

    public String getUrheber1() {
        String name = urheber(0);
        return name.isBlank() ? "Unbekannter Autor" : name;
    }

    public void setUrheber1(String name) {
        setUrheber(0, name);
    }

    public String getUrheber2() {
        String name = urheber(1);
        return name.isBlank() ? "" : ", " + name;
    }

    public void setUrheber2(String name) {
        setUrheber(1, name);
    }

    public String getUrheber3() {
        String name = urheber(2);
        return name.isBlank() ? "" : ", " + name;
    }

    public void setUrheber3(String name) {
        setUrheber(2, name);
    }

    public String getUrheber4() {
        String name = urheber(3);
        return name.isBlank() ? "" : ", " + name;
    }

    public void setUrheber4(String name) {
        setUrheber(3, name);
    }

    public String getMehrUrheber(boolean mehrurheber) {
        if (mehrurheber) {
            return " et al.";
        }

        return "";
    }

    public void setMehrUrheber(Optional<Boolean> mehrurheber) {
        MehrUrheber = mehrurheber.orElse(false);
    }

    @Override
    public void setTitel(Optional<String> titel) {
        super.setTitel(titel.map(String::trim).filter(wert -> !wert.isBlank()));
    }

    public String getMedienart() {
        return Medienart.map(wert -> "[" + wert + "]. ").orElse("");
    }

    public void setMedienart(Optional<String> medienart) {
        Medienart = medienart.map(String::trim).filter(wert -> !wert.isBlank());
    }

    public String getveroeffentlichungsdatum() {
        return getVeroeffentlichungsdatum().map(datum -> " (" + datum.getYear() + "). ").orElse("");
    }

    public Optional<String> setVeröffentlichungsdatum(Optional<String> veroffentlichungsdatum) {
        if (veroffentlichungsdatum == null || veroffentlichungsdatum.map(String::trim).isEmpty()) {
            this.veroeffentlichungsdatum = Optional.empty();
            return Optional.empty();
        }

        veroffentlichungsdatum = veroffentlichungsdatum.map(String::trim);

        Optional<String> fehler = Validierung.pruefeJahr(veroffentlichungsdatum);

        if (fehler.isPresent()) {
            return fehler;
        }

        this.veroeffentlichungsdatum = Optional.of(LocalDate.of(Integer.parseInt(veroffentlichungsdatum.get()), 1, 1));
        return Optional.empty();
    }

    public String getHerausgeber() {
        return Herausgeber.map(wert -> wert + ". ").orElse("");
    }

    public void setHerausgeber(Optional<String> herausgeber) {
        Herausgeber = herausgeber.map(String::trim).filter(wert -> !wert.isBlank());
    }

    public Optional<String> getAnbieter() {
        return Anbieter;
    }

    public void setAnbieter(Optional<String> anbieter) {
        Anbieter = anbieter.map(String::trim).filter(wert -> !wert.isBlank());
    }

    public String DatumFormatierung() {
        return getAbrufdatum().map(datum -> "[" + datum.format(DateTimeFormatter.ofPattern("d.M.yyyy")) + "].")
                .orElse("");
    }

    public Optional<String> setAbrufdatumText(Optional<String> abrufdatum) {
        Optional<String> datum = abrufdatum.map(String::trim).filter(wert -> !wert.isBlank());
        if (datum.isEmpty()) {
            setAbrufdatum(Optional.empty());
            return Optional.empty();
        }

        Optional<String> fehler = Validierung.pruefeDatum(datum);
        if (fehler.isEmpty()) {
            setAbrufdatum(datum.map(wert -> LocalDate.parse(wert, DateTimeFormatter.ofPattern("d.M.uuuu"))));
        }
        return fehler;
    }

    public String getbeleg() {
        return getUrheber1() + getUrheber2() + getUrheber3() + getUrheber4() + getMehrUrheber(MehrUrheber)
                + getveroeffentlichungsdatum() + getTitel().orElse("") + getMedienart() + getHerausgeber() + getAnbieter().orElse("")
                + DatumFormatierung();
    }

    @Override
    public String generiereKurzbeleg() {
        return "(" + getUrheber1()
                + getVeroeffentlichungsdatum().map(datum -> ", " + datum.getYear()).orElse("") + ")";
    }

    @Override
    public String generiereLangbeleg() {
        return getbeleg();
    }

    // US05: Erfassungsstand berechnen

    @Override
    public int getAnzahlAusgefuellterAttribute() {
        int anzahl = 0;

        // Urheber
        if (getAutor().orElse(List.of()).stream()
                .anyMatch(a -> a != null && !a.isBlank())) {
            anzahl++;
        }

        // Titel
        if (getTitel().isPresent()) {
            anzahl++;
        }

        // Medienart
        if (Medienart.isPresent()) {
            anzahl++;
        }

        // Erscheinungsjahr
        if (getVeroeffentlichungsdatum().isPresent()) {
            anzahl++;
        }

        // Herausgeber oder Anbieter
        if (Herausgeber.isPresent() || Anbieter.isPresent()) {
            anzahl++;
        }

        return anzahl;
    }

    @Override
    public int getAnzahlRelevanterAttribute() {
        return 5;
    }
}
