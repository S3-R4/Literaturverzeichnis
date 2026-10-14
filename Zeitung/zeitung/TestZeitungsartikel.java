package zeitung;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestZeitungsartikel {

	@Test
	public void testZeitungsrtikel() {
		//gute Quelle Test langerBeleg
		Zeitungsartikel z1 = new Zeitungsartikel("2025, 18.März", "'Rote Linien' für Änderungen beim Klimaschutz", "Frankfurter Allgemeine", "", "https://...", "Gelinsky, K.");
		assertEquals("Gelinsky, K. (2025, 18.März). 'Rote Linien' für Änderungen beim Klimaschutz. Frankfurter Allgemeine. https://...", z1.getLangerBeleg());
		//schlechte Quelle Test langerBeleg
		Zeitungsartikel z2 = new Zeitungsartikel("2025, 18.März", "'Rote Linien' für Änderungen beim Klimaschutz", "Frankfurter Allgemeine", "", "");
		assertEquals("Frankfurter Allgemeine (2025, 18.März). 'Rote Linien' für Änderungen beim Klimaschutz. Frankfurter Allgemeine", z2.getLangerBeleg());
		
		//gute Quelle Test langerBeleg
		Zeitungsartikel z3 = new Zeitungsartikel("2025, 18.März", "'Rote Linien' für Änderungen beim Klimaschutz", "Frankfurter Allgemeine", "S.67", "", "Gelinsky, K.");
		assertEquals("(Gelinsky, K., 2025, 18.März, S.67)", z3.getKurzerBeleg());
		//schlechte Quelle Test langerBeleg
		Zeitungsartikel z4 = new Zeitungsartikel("2025, 18.März", "'Rote Linien' für Änderungen beim Klimaschutz", "Frankfurter Allgemeine", "", "", "");
		assertEquals("(Frankfurter Allgemeine, 2025, 18.März)", z4.getKurzerBeleg());
	}
}
