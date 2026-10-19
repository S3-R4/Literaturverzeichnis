package QuellenTypen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class YoutubeTest {
	@Test
	void createsCitation() {
		Youtube video = new Youtube();
		video.setProduzent("Kanal");
		video.setErscheinungsDatum("2024");
		video.setTitel("Video");
		video.setBeitragsArt("Tutorial");
		video.setMedium("YouTube");
		video.setUrl("https://example.test/video");
		video.setAbruffDatum("19.10.2026");

		assertEquals("Kanal. 2024. Video. [Tutorial]. YouTube. https://example.test/video 19.10.2026",
				video.erstelleQuellenangabe());
	}
}
