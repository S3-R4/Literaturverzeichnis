package QuellenTypen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AudiovisuellTest {
	private static class TestAudiovisuell extends Audiovisuell {
		@Override
		public String erstelleQuellenangabe() {
			return "test";
		}
	}

	@Test
	void storesAndReturnsAllMetadata() {
		Audiovisuell quelle = new TestAudiovisuell();
		quelle.setTitel("Titel");
		quelle.setProduzent("Produzent");
		quelle.setErscheinungsDatum("2024");
		quelle.setBeitragsArt("Video");
		quelle.setMedium("Web");
		quelle.setUrl("https://example.test");
		quelle.setAbruffDatum("19.10.2026");

		assertEquals("Titel", quelle.getTitel());
		assertEquals("Produzent", quelle.getProduzent());
		assertEquals("2024", quelle.getErscheinungsDatum());
		assertEquals("Video", quelle.getBeitragsArt());
		assertEquals("Web", quelle.getMedium());
		assertEquals("https://example.test", quelle.getUrl());
		assertEquals("19.10.2026", quelle.getAbruffDatum());
		assertEquals("test", quelle.erstelleQuellenangabe());
		quelle.add();
		quelle.update();
		quelle.delete();
	}
}
