package model.data;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import model.Website;
import model.Zeitungsartikel;
import model.data.data_manager;

public class DataTest {
	public static void main(String[] args) throws IOException {

		data_manager manager = new data_manager();
		String id = "19266f79-589f-434f-9e57-0b686336e2f6";
		// Website website = new Website(
		// List.of("Rafa"),
		// "Edited website muhehehe",
		// "barbod is NOT a cutie patootie",
		// "https://nonutnovemberisalie/true/day/20",
		// LocalDate.of(2026, 11, 9),
		// LocalDate.of(2026, 11, 1),
		// "Articlehehe");

		// new data_manager().add_data(website);

		// Zeitungsartikel zeitungsartikel = new Zeitungsartikel(
		// LocalDate.of(2026, 11, 8),
		// "Notícia de exemplo",
		// "Jornal de exemplo",
		// "p. 5",
		// "https://example.com/noticia",
		// List.of("Denis"));

		// new data_manager().add_data(zeitungsartikel);

		// System.out.println(manager.read_data()); // all of them
		// System.out.println(manager.read_data("Website")); // all the websites

		Website edited_website = new Website(
		List.of("Rafa"),
		"Edited damn website muhehehe",
		"barbod is a REAL cutie patootie",
		"https://nonutnovemberisalie/true/day/20",
		LocalDate.of(2026, 11, 9),
		LocalDate.of(2026, 11, 1),
		"Articlehehe");


		System.out.println(manager.read_data_by_id(id)); // only one entry
		manager.edit_entry(id, edited_website);
		System.out.println(manager.read_data_by_id(id)); // read the second entry
	}
}
