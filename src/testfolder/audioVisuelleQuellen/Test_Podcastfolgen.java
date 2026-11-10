package testfolder.audioVisuelleQuellen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import model.Audiovisuell.Audiovisuell;
import model.Audiovisuell.Podcastfolgen;

class Test_Podcastfolgen {
	@Test
	void inheritsAudiovisualMetadata() {
		Podcastfolgen folge = new Podcastfolgen();
		folge.setTitel("TestPodcast");
		folge.setPodcastFolgenName("Test Folge - Die test folge!");
		folge.setPodcastFolge(40);
		folge.setProduzent("Person, M.");
		folge.addProduzent("AnderePerson");
		folge.setHerausgebendeOrganisation("Podcast-Team");
		folge.setErscheinungsDatum("01.01.2029");
		folge.setUrl("https://www.real-link-not-suspicious.com/fole#40 - testfolge");
		folge.setAbruffDatum("07.10.2329");
		assertInstanceOf(Audiovisuell.class, folge);
		assertEquals("TestPodcast", folge.getTitel());
		assertEquals("Person, M.", folge.getProduzent());
		assertEquals("Person, M., AnderePerson. (01.01.2029) Folge #40 - Test Folge - Die test folge!. [Podcast-Folge] In: TestPodcast Podcast-Team. https://www.real-link-not-suspicious.com/fole#40 - testfolge (07.10.2329).", folge.erstelleQuellenangabe());
	}
}
