package testfolder.audioVisuelleQuellen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Audiovisuell.PodcastGesamtwerk;

class Test_PodcastGesamtwerk {
	@Test
	void createsCitationAndStoresPublishingOrganization() {
		PodcastGesamtwerk podcast = new PodcastGesamtwerk();
		podcast.setAutor(List.of("Produzent"));
		podcast.setVeroffentlichungsdatum(LocalDate.of(2023, 5, 6));
		podcast.setAbruffDatum(LocalDate.of(2026, 10, 19));
		podcast.setTitel("Podcast");
		podcast.setHerausgebendeOrganisation("Organisation");
		podcast.setUrl("https://example.test/podcast");

		assertEquals("Produzent. (2023). [Podcast]. Organisation. https://example.test/podcast 19.10.2026",
				podcast.generiereLangbeleg());
		assertEquals("Produzent. (2023). [Podcast]", podcast.generiereKurzbeleg());
		assertEquals("Organisation", podcast.getHerausgebendeOrganisation());
	}
}
