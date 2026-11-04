package QuellenTypen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PodcastGesamtwerkTest {
	@Test
	void createsCitationAndStoresPublishingOrganization() {
		PodcastGesamtwerk podcast = new PodcastGesamtwerk();
		podcast.setProduzent("Produzent");
		podcast.setErscheinungsDatum("2023");
		podcast.setTitel("Podcast");
		podcast.setHerausgebendeOrganisation("Organisation");
		podcast.setUrl("https://example.test/podcast");
		podcast.setAbruffDatum("19.10.2026");

		assertEquals("Produzent. 2023. [Podcast]. Organisation. https://example.test/podcast 19.10.2026",
				podcast.erstelleQuellenangabe());
		assertEquals("Organisation", podcast.getHerausgebendeOrganisation());
	}
}
