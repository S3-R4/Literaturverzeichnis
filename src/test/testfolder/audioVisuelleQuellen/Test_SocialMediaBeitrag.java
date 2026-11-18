package testfolder.audioVisuelleQuellen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Audiovisuell.SocialMediaBeitraege;

class Test_SocialMediaBeitrag {
	@Test
	void createsCitationAndStoresTextAndDescription() {
		SocialMediaBeitraege beitrag = new SocialMediaBeitraege();
		beitrag.setAutor(List.of("Autor"));
		beitrag.setVeroffentlichungsdatum(Optional.of(LocalDate.of(2025, 4, 5)));
		beitrag.setAbruffDatum(Optional.of(LocalDate.of(2026, 10, 19)));
		beitrag.setTitel(Optional.of("Beitrag"));
		beitrag.setBeitragsArt(Optional.of("Post"));
		beitrag.setMedium(Optional.of("Mastodon: "));
		beitrag.setUrl(Optional.of("https://example.test/post"));
		beitrag.setText(Optional.of("Text des Beitrags"));
		beitrag.setBeschreibung(Optional.of("Beschreibung"));

		assertEquals("Autor. (2025). Beitrag. [Post]. Mastodon: https://example.test/post 19.10.2026",
				beitrag.generiereLangbeleg());
		assertEquals("Autor. (2025). Beitrag", beitrag.generiereKurzbeleg());
		assertEquals("Text des Beitrags", beitrag.getText().orElseThrow());
		assertEquals("Beschreibung", beitrag.getBeschreibung().orElseThrow());
	}
}
