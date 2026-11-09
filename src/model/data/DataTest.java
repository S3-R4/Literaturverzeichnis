package model.data;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import model.Website;
import model.Zeitungsartikel;

public class DataTest {
	public static void main(String[] args) throws IOException {

		// Website website = new Website(
		// 		List.of("Rafa testing again"),
		// 		"CoolWebsite that I created",
		// 		"barbod is a cutie patootie",
		// 		"https://nonutnovemberisalie/true/day/20",
		// 		LocalDate.of(2026, 11, 9),
		// 		LocalDate.of(2026, 11, 1),
		// 		"Articlehehe");

		// new data_manager().add_data(website);

		// Zeitungsartikel zeitungsartikel = new Zeitungsartikel(
		// 		LocalDate.of(2026, 11, 8),
		// 		"Notícia de exemplo",
		// 		"Jornal de exemplo",
		// 		"p. 5",
		// 		"https://example.com/noticia",
		// 		List.of("Denis"));

		// new data_manager().add_data(zeitungsartikel);

		System.err.println(new data_manager().read_data());
	}
}
