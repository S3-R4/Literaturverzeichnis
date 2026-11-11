package testfolder.zeitungsartikel;

import org.junit.jupiter.api.Test;

import model.Zeitungsartikel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_Zeitungsartikel {

	@Test
	public void testZeitungsrtikel() {
		//gute Quelle Test langerBeleg
		Zeitungsartikel z1 = new Zeitungsartikel(LocalDate.of(2025, 3, 18), "'Rote Linien' für Änderungen beim Klimaschutz", "Frankfurter Allgemeine", "", "https://...", new ArrayList<String>(List.of("Gelinsky, K.")));
		assertEquals("Gelinsky, K. (2025, 18.März). 'Rote Linien' für Änderungen beim Klimaschutz. Frankfurter Allgemeine. https://...", z1.generiereLangbeleg());
		//schlechte Quelle Test langerBeleg
		Zeitungsartikel z2 = new Zeitungsartikel(LocalDate.of(2025, 3, 18), "'Rote Linien' für Änderungen beim Klimaschutz", "Frankfurter Allgemeine", "", "", new ArrayList<String>());
		assertEquals("Frankfurter Allgemeine (2025, 18.März). 'Rote Linien' für Änderungen beim Klimaschutz. Frankfurter Allgemeine", z2.generiereLangbeleg());
		
		//gute Quelle Test kurzerBeleg
		Zeitungsartikel z3 = new Zeitungsartikel(LocalDate.of(2025, 3, 18), "'Rote Linien' für Änderungen beim Klimaschutz", "Frankfurter Allgemeine", "S.67", "", new ArrayList<String>(List.of("Gelinsky, K.")));
		assertEquals("(Gelinsky, K., 2025, 18.März, S.67)", z3.generiereKurzbeleg());
		//schlechte Quelle Test kurzerBeleg
		Zeitungsartikel z4 = new Zeitungsartikel(LocalDate.of(2025, 3, 18), "'Rote Linien' für Änderungen beim Klimaschutz", "Frankfurter Allgemeine", "", "", new ArrayList<String>());
		assertEquals("(Frankfurter Allgemeine, 2025, 18.März)", z4.generiereKurzbeleg());
	}
}
