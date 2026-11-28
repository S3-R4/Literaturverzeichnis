package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Represents an AI tool source with model metadata, publication year, and retrieval date.
 */
public class KI_Tool extends Literatur {
    private String modellName, modellversion, url, toolArt;

    /**
     * Creates a KI_Tool instance with the supplied metadata.
     *
     * @param autor                   author list
     * @param veroeffentlichungsdatum publication date
     * @param modellName              name of the AI model
     * @param modellversion           version of the AI model
     * @param toolArt                 category or type of the AI tool
     * @param url                     URL
     * @param abrufdatum              retrieval date
     */
    public KI_Tool(List<String> autor, LocalDate veroeffentlichungsdatum, String modellName, String modellversion,
            String toolArt, String url, LocalDate abrufdatum) {
        setAutor(autor);
        setVeroeffentlichungsdatum(Optional.ofNullable(veroeffentlichungsdatum));
        this.modellName = modellName;
        this.modellversion = modellversion;
        this.toolArt = toolArt;
        this.url = url;
        setAbrufdatum(Optional.ofNullable(abrufdatum));
    }

    /**
     * Formats a short citation for this source.
     *
     * @return the short citation
     */
    @Override
    public String generiereKurzbeleg() {
        String textbaustein;
        // Autor
        if (autor.isEmpty()) {
            textbaustein = "A.u.";
        } else {
            textbaustein = autor.getFirst() + ". (" + getVeroeffentlichungsDatum().toString() + "). ";
        }
        // Modellinfos
        textbaustein += modellName;
        textbaustein += " (" + modellversion + " version) ";
        textbaustein += "[" + toolArt + "]. ";
        // URL
        textbaustein += this.url;
        textbaustein += " (" + getAbrufdatum(abrufdatum) + ").";

        return textbaustein;
    }

    /**
     * Formats a full bibliographic citation for this source.
     *
     * @return the full citation
     */
    @Override
    public String generiereLangbeleg() {
        // KI hat nur eine Belegart
        return generiereKurzbeleg();
    }

    /**
     * Counts populated attributes used to measure source completeness.
     *
     * @return the number of populated relevant attributes
     */
    @Override
    public int getAnzahlAusgefuellterAttribute() {
        int anzahl = 0;

        if (autor.stream().anyMatch(a -> a != null && !a.isBlank())) {
            anzahl++;
        }

        if (titel.filter(v -> !v.isBlank()).isPresent()) {
            anzahl++;
        }

        if (modellName != null && !modellName.isBlank()) {
            anzahl++;
        }

        if (modellversion != null && !modellversion.isBlank()) {
            anzahl++;
        }

        if (toolArt != null && !toolArt.isBlank()) {
            anzahl++;
        }

        if (veroeffentlichungsdatum.isPresent()) {
            anzahl++;
        }

        if (abrufdatum.isPresent()) {
            anzahl++;
        }

        if (!url.equals("")) {
            anzahl++;
        }
        return anzahl;
    }

    /**
     * Returns the number of attributes considered for source completeness.
     *
     * @return the total number of relevant attributes
     */
    @Override
    public int getAnzahlRelevanterAttribute() {
        return 7;
    }

    /**
     * Returns the publication year or a placeholder if absent.
     *
     * @return the publication year
     */
    public String getVeroeffentlichungsDatum() {
        return veroeffentlichungsdatum.map(datum -> String.valueOf(datum.getYear())).orElse("o. J.");
    }

    /**
     * Returns the formatted retrieval date.
     *
     * @param datum the optional retrieval date
     * @return the formatted retrieval date
     */
    public String getAbrufdatum(Optional<LocalDate> datum) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return datum.map(d -> d.format(formatter)).orElse("");
    }

        /**
     * Returns the AI model name.
     *
     * @return the model name
     */
    public String getModellName() {
        return modellName;
    }

    /**
     * Returns the AI model version.
     *
     * @return the model version
     */
    public String getModellversion() {
        return modellversion;
    }

    /**
     * Returns the AI tool category.
     *
     * @return the tool category
     */
    public String getToolArt() {
        return toolArt;
    }

    /**
     * Returns the URL.
     *
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

}
