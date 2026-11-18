package testfolder.audioVisuelleQuellen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Audiovisuell.FIlmsVideos;

class Test_FilmVideo {
	@Test
	void createsCitationWithUrl() {
		FIlmsVideos film = new FIlmsVideos();
		film.setAutor(List.of("Regie"));
		film.setVeroffentlichungsdatum(Optional.of(LocalDate.of(2024, 1, 1)));
		film.setAbruffDatum(Optional.of(LocalDate.of(2024, 1, 2)));
		film.setTitel(Optional.of("Film"));
		film.setBeitragsArt(Optional.of("Dokumentation"));
		film.setProduktionsfirma(Optional.of("Studio"));
		film.setUrl(Optional.of("https://example.test/film"));

		assertEquals("Regie. (2024). Film [Dokumentation]. Studio. https://example.test/film.",
				film.generiereLangbeleg());
		assertEquals("Studio", film.getProduktionsfirma().orElseThrow());
	}

	@Test
	void createsCitationWithoutUrl() {
		FIlmsVideos film = new FIlmsVideos();
		film.setAutor(List.of("Regie"));
		film.setVeroffentlichungsdatum(Optional.of(LocalDate.of(2024, 1, 1)));
		film.setAbruffDatum(Optional.of(LocalDate.of(2024, 1, 2)));
		film.setTitel(Optional.of("Film"));
		film.setBeitragsArt(Optional.of("Dokumentation"));
		film.setProduktionsfirma(Optional.of("Studio"));

		assertEquals("Regie. (2024). Film [Dokumentation]. Studio.", film.generiereLangbeleg());
	}
}
