import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Bildbelegtest {

	@Test 
	public void test() {
	BildQuelle bild01 = new BildQuelle("Müller T.","", "", "", false, "Psychologische Grundlagen der Motivation", "Diagram", "2022", "Springer", "https://...", "21.2.2026");
	assertEquals(bild01.getbeleg(),"Müller T. (2022). Psychologische Grundlagen der Motivation[Diagram]. Springer. https://...[21.2.2026]."); //Full

	BildQuelle bild02 = new BildQuelle("eigene Darstellung","","","",false,"","","","","",""); //Empty
	assertEquals(bild02.getbeleg(),"eigene Darstellung");

	BildQuelle bild03 = new BildQuelle("Müller T.","Schmidt A.", "Weber K.", "Fischer L.", false, "Motivation im Alltag", "Foto", "2024", "Springer", "https://example.com", "10.10.2026");
	assertEquals(bild03.getbeleg(),"Müller T., Schmidt A., Weber K., Fischer L. (2024). Motivation im Alltag[Foto]. Springer. https://example.com[10.10.2026]."); //More Urheber
	
	BildQuelle bild04 = new BildQuelle("Müller T.","Schmidt A.", "Weber K.", "Fischer L.", true, "Motivation im Alltag", "Foto", "2024", "Springer", "https://example.com", "10.10.2026");
	assertEquals(bild04.getbeleg(),"Müller T., Schmidt A., Weber K., Fischer L. et al. (2024). Motivation im Alltag[Foto]. Springer. https://example.com[10.10.2026].");
	
	BildQuelle bild05 = new BildQuelle("","","","",false,"test","Foto","2024","Springer","https://example.com","10.10.2026"); //Kein Autor
	assertEquals(bild05.getbeleg(),"Unbekannter Autor (2024). test[Foto]. Springer. https://example.com[10.10.2026].");	

	}
	@Test 
	public void test2() {
	 Bildbelegclass2 bild01 = new Bildbelegclass2("Müller T.", "Schmidt A.", "", "", false,"Psychologische Grundlagen der Motivation","Diagram", "2022", "Springer", "https://...", "21.2.2026");

        assertEquals(bild01.generiereLangbeleg(),"Müller T., Schmidt A. (2022). Psychologische Grundlagen der Motivation[Diagram]. Springer. https://...[21.2.2026].");
    }	
}
