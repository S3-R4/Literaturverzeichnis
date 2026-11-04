package QuellenTypen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

class PodcastfolgenTest {
	@Test
	void inheritsAudiovisualMetadata() {
		Podcastfolgen folge = new Podcastfolgen() {
			@Override
			public String erstelleQuellenangabe() {
				return getTitel();
			}
		};
		folge.setTitel("Folge 1");
		folge.setProduzent("Podcast-Team");

		assertInstanceOf(Audiovisuell.class, folge);
		assertEquals("Folge 1", folge.getTitel());
		assertEquals("Podcast-Team", folge.getProduzent());
		assertEquals("Folge 1", folge.erstelleQuellenangabe());
	}
}
