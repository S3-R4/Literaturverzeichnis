package testfolder.audioVisuelleQuellen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Audiovisuell.Youtube;

class Test_Youtube {
	@Test
	void createsCitation() {
		Youtube video = new Youtube();
		video.setAutor(Optional.of(List.of("Kanal")));
		video.setVeroffentlichungsdatum(Optional.of(LocalDate.of(2024, 1, 2)));
		video.setAbruffDatum(Optional.of(LocalDate.of(2026, 10, 19)));
		video.setTitel(Optional.of("Video"));
		video.setBeitragsArt(Optional.of("Tutorial"));
		video.setMedium(Optional.of("YouTube"));
		video.setUrl(Optional.of("https://example.test/video"));

		assertEquals("Kanal. (2024). Video. [Tutorial]. YouTube. https://example.test/video 19.10.2026",
				video.generiereLangbeleg());
		assertEquals("Kanal. (2024). Video", video.generiereKurzbeleg());
	}
}
