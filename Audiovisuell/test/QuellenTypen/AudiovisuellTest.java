package QuellenTypen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AudiovisuellTest {
    private TestAudiovisuell quelle;

    @BeforeEach
    void setUp() {
        quelle = new TestAudiovisuell();
    }

    @Test
    void titelWirdGespeichert() {
        quelle.setTitel("Titel");

        assertEquals("Titel", quelle.getTitel());
    }

    @Test
    void produzentWirdGespeichert() {
        quelle.setProduzent("Produzent");

        assertEquals("Produzent", quelle.getProduzent());
    }

    @Test
    void erscheinungsDatumWirdGespeichert() {
        quelle.setErscheinungsDatum("2025");

        assertEquals("2025", quelle.getErscheinungsDatum());
    }

    @Test
    void beitragsArtWirdGespeichert() {
        quelle.setBeitragsArt("Podcast");

        assertEquals("Podcast", quelle.getBeitragsArt());
    }

    @Test
    void mediumWirdGespeichert() {
        quelle.setMedium("Audio");

        assertEquals("Audio", quelle.getMedium());
    }

    @Test
    void urlWirdGespeichert() {
        quelle.setUrl("https://example.com");

        assertEquals("https://example.com", quelle.getUrl());
    }

    @Test
    void abruffDatumWirdGespeichert() {
        quelle.setAbruffDatum("14.10.2026");

        assertEquals("14.10.2026", quelle.getAbruffDatum());
    }

    @Test
    void konkreteUnterklasseKannQuellenangabeErstellen() {
        assertEquals("Quellenangabe", quelle.erstelleQuellenangabe());
    }

    private static class TestAudiovisuell extends Audiovisuell {
        @Override
        public String erstelleQuellenangabe() {
            return "Quellenangabe";
        }
    }
}
