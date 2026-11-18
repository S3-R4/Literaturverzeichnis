package testfolder.audioVisuelleQuellen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Audiovisuell.Youtube;

class Test_Youtube {
	@Test
	void createsCitation() {
		Youtube video = new Youtube();
		video.setAutor(List.of("Kanal"));
		video.setVeroffentlichungsdatum(LocalDate.of(2024, 1, 2));
		video.setAbruffDatum(LocalDate.of(2026, 10, 19));
		video.setTitel("Video");
		video.setBeitragsArt("Tutorial");
		video.setMedium("YouTube");
		video.setUrl("https://example.test/video");

		assertEquals("Kanal. (2024). Video. [Tutorial]. YouTube. https://example.test/video 19.10.2026",
				video.generiereLangbeleg());
		assertEquals("Kanal. (2024). Video", video.generiereKurzbeleg());
	}
}
