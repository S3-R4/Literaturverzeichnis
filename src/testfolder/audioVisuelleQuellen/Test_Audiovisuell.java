package testfolder.audioVisuelleQuellen;

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
			return getAutor().orElseThrow().get(0);
		}

		@Override
		public String generiereLangbeleg() {
			return getTitel().orElseThrow();
		}

		@Override
		public String datumFormatierung(Optional<LocalDate> datum) {
			return "(" + datum.orElseThrow().getYear() + ")";
		}

		@Override
		public int getAnzahlAusgefuellterAttribute() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getAnzahlRelevanterAttribute() {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	@Test
	void storesAndReturnsAudiovisualMetadata() {
		Audiovisuell quelle = new TestAudiovisuell();
		LocalDate veroeffentlichungsdatum = LocalDate.of(2024, 1, 2);
		LocalDate abrufdatum = LocalDate.of(2026, 10, 19);
		quelle.setAutor(Optional.of(List.of("Produzent")));
		quelle.setTitel(Optional.of("Titel"));
		quelle.setVeroffentlichungsdatum(Optional.of(veroeffentlichungsdatum));
		quelle.setAbruffDatum(Optional.of(abrufdatum));
		quelle.setBeitragsArt(Optional.of("Video"));
		quelle.setMedium(Optional.of("Web"));
		quelle.setUrl(Optional.of("https://example.test"));

		assertEquals(List.of("Produzent"), quelle.getAutor().orElseThrow());
		assertEquals("Titel", quelle.getTitel().orElseThrow());
		assertEquals(veroeffentlichungsdatum, quelle.getVeroeffentlichungsdatum().orElseThrow());
		assertEquals(abrufdatum, quelle.getAbruffDatum().orElseThrow());
		assertEquals("Video", quelle.getBeitragsArt().orElseThrow());
		assertEquals("Web", quelle.getMedium().orElseThrow());
		assertEquals("https://example.test", quelle.getUrl().orElseThrow());
		assertEquals("Produzent", quelle.generiereKurzbeleg());
		assertEquals("Titel", quelle.generiereLangbeleg());
	}
}
