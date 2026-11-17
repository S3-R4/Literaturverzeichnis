package testfolder.bild;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import model.BildQuelle;

public class Test_Bildbeleg {

	@Test 
	public void test() {
	BildQuelle bild01 = new BildQuelle("Müller T.","", "", "", false, Optional.of("Psychologische Grundlagen der Motivation"), Optional.of("Diagram"), Optional.of(LocalDate.of(2022, 1, 1)), Optional.of("Springer"), Optional.of("https://..."));
	assertEquals(bild01.generiereLangbeleg(),"Müller T. (2022). Psychologische Grundlagen der Motivation[Diagram]. Springer. https://..." + bild01.DatumFormatierung()); //Full

	BildQuelle bild02 = new BildQuelle("eigene Darstellung","","","",false, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()); //Empty
	assertEquals(bild02.generiereKurzbeleg(),"(eigene Darstellung)");

	BildQuelle bild03 = new BildQuelle("Müller T.","Schmidt A.", "Weber K.", "Fischer L.", false, Optional.of("Motivation im Alltag"), Optional.of("Foto"), Optional.of(LocalDate.of(2024, 1, 1)), Optional.of("Springer"), Optional.of("https://example.com"));
	assertEquals(bild03.getbeleg(),"Müller T., Schmidt A., Weber K., Fischer L. (2024). Motivation im Alltag[Foto]. Springer. https://example.com" + bild03.DatumFormatierung()); //More Urheber
	
	BildQuelle bild04 = new BildQuelle("Müller T.","Schmidt A.", "Weber K.", "Fischer L.", true, Optional.of("Motivation im Alltag"), Optional.of("Foto"), Optional.of(LocalDate.of(2024, 1, 1)), Optional.of("Springer"), Optional.of("https://example.com"));
	assertEquals(bild04.getbeleg(),"Müller T., Schmidt A., Weber K., Fischer L. et al. (2024). Motivation im Alltag[Foto]. Springer. https://example.com" + bild04.DatumFormatierung());
	
	BildQuelle bild05 = new BildQuelle("","","","",false, Optional.of("test"), Optional.of("Foto"), Optional.of(LocalDate.of(2024, 1, 1)), Optional.of("Springer"), Optional.of("https://example.com")); //Kein Autor
	assertEquals(bild05.getbeleg(),"Unbekannter Autor (2024). test[Foto]. Springer. https://example.com" + bild05.DatumFormatierung());	

	}	
}
