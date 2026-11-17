package testfolder.audioVisuelleQuellen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;

import model.Audiovisuell.Audiovisuell;
import model.Audiovisuell.Podcastfolgen;

class Test_Podcastfolgen {
	@Test
	void inheritsAudiovisualMetadata() {
		Podcastfolgen folge = new Podcastfolgen();
		folge.setTitel(Optional.of("TestPodcast"));
		folge.setPodcastFolgenName(Optional.of("Test Folge - Die test folge!"));
		folge.setPodcastFolge(Optional.of(40));
		folge.setAutor(Optional.of(List.of("Person, M.", "AnderePerson.")));
		folge.setHerausgebendeOrganisation(Optional.of("Podcast-Team"));
		folge.setVeroffentlichungsdatum(Optional.of(LocalDate.of(2029, 1, 1)));
		folge.setUrl(Optional.of("https://www.real-link-not-suspicious.com/fole#40 - testfolge"));
		folge.setAbruffDatum(Optional.of(LocalDate.of(2329, 10, 7)));
		assertInstanceOf(Audiovisuell.class, folge);
		assertEquals("TestPodcast", folge.getTitel().orElseThrow());
		assertEquals(List.of("Person, M.", "AnderePerson."), folge.getAutor().orElseThrow());
		assertEquals("Person, M. & AnderePerson. (2029, 1, Januar). Folge #40 - Test Folge - Die test folge!. [Podcast-Folge]. In: TestPodcast Podcast-Team. https://www.real-link-not-suspicious.com/fole#40 - testfolge (07.10.2329).",
				folge.generiereLangbeleg());
		assertEquals("Person, M. & AnderePerson. (2029, 1, Januar) Folge #40", folge.generiereKurzbeleg());
	}
}
