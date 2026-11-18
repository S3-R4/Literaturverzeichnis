package test.testfolder.audioVisuelleQuellen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;
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
			return getTitel().orElseThrow();
		}

		@Override
		public String datumFormatierung(Optional<LocalDate> datum) {
			return "(" + datum.orElseThrow().getYear() + ")";
		}

	}

	@Test
	void storesAndReturnsAudiovisualMetadata() {
		Audiovisuell quelle = new TestAudiovisuell();
		assertEquals(0, quelle.getAnzahlAusgefuellterAttribute());
		assertEquals(7, quelle.getAnzahlRelevanterAttribute());
		LocalDate veroeffentlichungsdatum = LocalDate.of(2024, 1, 2);
		LocalDate abrufdatum = LocalDate.of(2026, 10, 19);
		quelle.setAutor(List.of("Produzent"));
		quelle.setTitel(Optional.of("Titel"));
		quelle.setVeroffentlichungsdatum(Optional.of(veroeffentlichungsdatum));
		quelle.setAbruffDatum(Optional.of(abrufdatum));
		quelle.setBeitragsArt(Optional.of("Video"));
		quelle.setMedium(Optional.of("Web"));
		quelle.setUrl(Optional.of("https://example.test"));

		assertEquals(List.of("Produzent"), quelle.getAutor());
		assertEquals("Titel", quelle.getTitel().orElseThrow());
		assertEquals(veroeffentlichungsdatum, quelle.getVeroeffentlichungsdatum().orElseThrow());
		assertEquals(abrufdatum, quelle.getAbruffDatum().orElseThrow());
		assertEquals("Video", quelle.getBeitragsArt().orElseThrow());
		assertEquals("Web", quelle.getMedium().orElseThrow());
		assertEquals("https://example.test", quelle.getUrl().orElseThrow());
		assertEquals("Produzent", quelle.generiereKurzbeleg());
		assertEquals("Titel", quelle.generiereLangbeleg());
		assertEquals(7, quelle.getAnzahlAusgefuellterAttribute());
	}
}
