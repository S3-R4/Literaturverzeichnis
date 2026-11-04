package QuellenTypen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SocialMediaBeitraegeTest {
	@Test
	void createsCitationAndStoresTextAndDescription() {
		SocialMediaBeitraege beitrag = new SocialMediaBeitraege();
		beitrag.setProduzent("Autor");
		beitrag.setErscheinungsDatum("2025");
		beitrag.setTitel("Beitrag");
		beitrag.setBeitragsArt("Post");
		beitrag.setMedium("Mastodon: ");
		beitrag.setUrl("https://example.test/post");
		beitrag.setAbruffDatum("19.10.2026");
		beitrag.setText("Text des Beitrags");
		beitrag.setBeschreibung("Beschreibung");

		assertEquals("Autor. 2025. Beitrag. [Post]. Mastodon: https://example.test/post 19.10.2026",
				beitrag.erstelleQuellenangabe());
		assertEquals("Text des Beitrags", beitrag.getText());
		assertEquals("Beschreibung", beitrag.getBeschreibung());
	}
}
