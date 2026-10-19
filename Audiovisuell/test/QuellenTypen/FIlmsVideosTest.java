package QuellenTypen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FIlmsVideosTest {
	@Test
	void createsCitationWithUrl() {
		FIlmsVideos film = new FIlmsVideos();
		film.setProduzent("Regie");
		film.setErscheinungsDatum("2024");
		film.setTitel("Film");
		film.setBeitragsArt("Dokumentation");
		film.setProduktionsfirma("Studio");
		film.setUrl("https://example.test/film");

		assertEquals("Regie. 2024. Film [Dokumentation]. Studio. https://example.test/film.",
				film.erstelleQuellenangabe());
		assertEquals("Studio", film.getProduktionsfirma());
	}

	@Test
	void createsCitationWithoutUrl() {
		FIlmsVideos film = new FIlmsVideos();
		film.setProduzent("Regie");
		film.setErscheinungsDatum("2024");
		film.setTitel("Film");
		film.setBeitragsArt("Dokumentation");
		film.setProduktionsfirma("Studio");

		assertEquals("Regie. 2024. Film [Dokumentation]. Studio.", film.erstelleQuellenangabe());
	}
}
