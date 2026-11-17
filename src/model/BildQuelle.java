
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BildQuelle extends Literatur {

    private Optional<String> Medienart = Optional.empty();
    private Optional<String> Herausgeber = Optional.empty();
    private Optional<String> Anbieter = Optional.empty();
    private boolean MehrUrheber = false;

    public BildQuelle() {
        autor = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            autor.add("");
        }
        titel = Optional.empty();
        veroeffentlichungsdatum = Optional.empty();
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

        this.veroeffentlichungsdatum = Optional.ofNullable(veroeffentlichungsdatum);

    }

    public List<String> validiere() {
        List<String> fehler = new ArrayList<>();

        if (titel != null && !titel.isBlank()) {
            Optional<String> fehlerText = Validierung.pruefeText(titel);

            if (fehlerText.isPresent()) {
                fehler.add(fehlerText.get());
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

    public void setMehrUrheber(Optional<Boolean> mehrurheber) {
        MehrUrheber = mehrurheber;
    }

    @Override
    public Optional<String> getTitel() {
        return titel == null ? Optional.empty() : titel;
    }

    @Override
    public void setTitel(Optional<String> titel) {
        this.titel = titel == null ? Optional.empty() : titel.map(String::trim);
    }

    public String getMedienart() {
        if (Medienart == null || Medienart.isEmpty()) {
            return "";
        }
        return "[" + Medienart + "]. ";
    }

    public void setMedienart(Optional<String> medienart) {
        Medienart = medienart == null ? Optional.empty() : medienart.map(String::trim);
    }

    public String getveroeffentlichungsdatum() {
        if (veroeffentlichungsdatum == null) {
            return "";
        }

        return " (" + veroeffentlichungsdatum.get().getYear() + "). ";
    }

    public Optional<String> setVeröffentlichungsdatum(Optional<String> veroffentlichungsdatum) {
        if (veroffentlichungsdatum == null || veroffentlichungsdatum.map(String::trim).isEmpty()) {
            this.veroeffentlichungsdatum = Optional.empty();
            return Optional.empty();
        }

        veroffentlichungsdatum = veroffentlichungsdatum.map(String::trim);

        Optional<String> fehler = Validierung.pruefeJahr(veroffentlichungsdatum);

        if (fehler.isPresent()) {
            return Optional.of(fehler.get());
        }

        this.veroeffentlichungsdatum = Optional.of(LocalDate.of(Integer.parseInt(veroffentlichungsdatum.get()), 1, 1));
        return Optional.empty();
    }

    public String getHerausgeber() {
        if (Herausgeber.equals("")) {
            return "";
        }

        return Herausgeber + ". ";
    }

    public void setHerausgeber(Optional<String> herausgeber) {
        Herausgeber = herausgeber == null ? Optional.empty() : herausgeber;
    }

    public Optional<String> getAnbieter() {
        return Anbieter;
    }

    public void setAnbieter(Optional<String> anbieter) {
        Anbieter = anbieter == null ? Optional.empty() : anbieter;
    }

    public String DatumFormatierung() {
        if (abrufdatum == null) {
            return "";
        }

        return "[" + abrufdatum.get().format(java.time.format.DateTimeFormatter.ofPattern("d.M.yyyy")) + "].";
    }

    public Optional<String> setAbrufdatum(Optional<String> abrufdatum) {
        if (abrufdatum == null || abrufdatum.map(String::trim).isEmpty()) {
            this.abrufdatum = Optional.empty();
            return Optional.empty();
        }

        abrufdatum = abrufdatum.map(String::trim);

        Optional<String> fehler = Validierung.pruefeDatum(abrufdatum);

        if (fehler.isPresent()) {
            return Optional.of(fehler);
        }

        this.abrufdatum = LocalDate.parse(abrufdatum, java.time.format.DateTimeFormatter.ofPattern("d.M.uuuu"));

        return Optional.empty();
    }

    public String getbeleg() {
        return getUrheber1() + getUrheber2() + getUrheber3() + getUrheber4() + getMehrUrheber(MehrUrheber)
                + getErscheinungsjahr() + getTitel() + getMedienart() + getHerausgeber() + getAnbieter()
                + DatumFormatierung();
    }

    @Override
    public String generiereKurzbeleg() {
        if (veroeffentlichungsdatum == null) {
            return "(" + getUrheber1() + ")";
        }

        return "(" + getUrheber1() + ", " + veroeffentlichungsdatum.getYear() + ")";
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
        if (autor != null && autor.stream()
                .anyMatch(a -> a != null && !a.isBlank())) {
            anzahl++;
        }

        // Titel
        if (titel != null && !titel.isBlank()) {
            anzahl++;
        }

        // Medienart
        if (Medienart != null && !Medienart.isBlank()) {
            anzahl++;
        }

        // Erscheinungsjahr
        if (veroeffentlichungsdatum != null) {
            anzahl++;
        }

        // Herausgeber oder Anbieter
        if ((Herausgeber != null && !Herausgeber.isBlank()) || (Anbieter != null && !Anbieter.isBlank())) {
            anzahl++;
        }

        return anzahl;
    }

    @Override
    public int getAnzahlRelevanterAttribute() {
        return 5;
    }
}
