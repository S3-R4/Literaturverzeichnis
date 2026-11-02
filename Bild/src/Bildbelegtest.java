import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Bildbelegtest {

	@Test 
	public void test() {
	Bildbelegclass bild01 = new Bildbelegclass("Müller T.","", "", "", false, "Psychologische Grundlagen der Motivation", "Diagram", "2022", "Springer", "https://...", "21.2.2026");
	assertEquals(bild01.getbeleg(bild01),"Müller T. (2022). Psychologische Grundlagen der Motivation[Diagram]. Springer. https://...[21.2.2026]."); //Full

	Bildbelegclass bild02 = new Bildbelegclass("eigene Darstellung","","","",false,"","","","","",""); //Empty
	assertEquals(bild02.getbeleg(bild02),"eigene Darstellung");

	Bildbelegclass bild03 = new Bildbelegclass("Müller T.","Schmidt A.", "Weber K.", "Fischer L.", false, "Motivation im Alltag", "Foto", "2024", "Springer", "https://example.com", "10.10.2026");
	assertEquals(bild03.getbeleg(bild03),"Müller T., Schmidt A., Weber K., Fischer L. (2024). Motivation im Alltag[Foto]. Springer. https://example.com[10.10.2026]."); //More Urheber
	
	Bildbelegclass bild04 = new Bildbelegclass("Müller T.","Schmidt A.", "Weber K.", "Fischer L.", true, "Motivation im Alltag", "Foto", "2024", "Springer", "https://example.com", "10.10.2026");
	assertEquals(bild04.getbeleg(bild04),"Müller T., Schmidt A., Weber K., Fischer L. et al. (2024). Motivation im Alltag[Foto]. Springer. https://example.com[10.10.2026].");
	}
	
		

	
}