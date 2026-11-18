package testfolder.audioVisuelleQuellen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Audiovisuell.Audiovisuell;

class Test_Audiovisuell {
	private static class TestAudiovisuell extends Audiovisuell {
		@Override
		public String generiereKurzbeleg() {
			return getAutor().get(0);
		}

		@Override
		public String generiereLangbeleg() {
			return getTitel();
		}

		@Override
		public String datumFormatierung(LocalDate datum) {
			return "(" + datum.getYear() + ")";
		}
	}

	@Test
	void storesAndReturnsAudiovisualMetadata() {
		Audiovisuell quelle = new TestAudiovisuell();
		LocalDate veroeffentlichungsdatum = LocalDate.of(2024, 1, 2);
		LocalDate abrufdatum = LocalDate.of(2026, 10, 19);
		quelle.setAutor(List.of("Produzent"));
		quelle.setTitel("Titel");
		quelle.setVeroffentlichungsdatum(veroeffentlichungsdatum);
		quelle.setAbruffDatum(abrufdatum);
		quelle.setBeitragsArt("Video");
		quelle.setMedium("Web");
		quelle.setUrl("https://example.test");

		assertEquals(List.of("Produzent"), quelle.getAutor());
		assertEquals("Titel", quelle.getTitel());
		assertEquals(veroeffentlichungsdatum, quelle.getVeroeffentlichungsdatum());
		assertEquals(abrufdatum, quelle.getAbruffDatum());
		assertEquals("Video", quelle.getBeitragsArt());
		assertEquals("Web", quelle.getMedium());
		assertEquals("https://example.test", quelle.getUrl());
		assertEquals("Produzent", quelle.generiereKurzbeleg());
		assertEquals("Titel", quelle.generiereLangbeleg());
	}
}
