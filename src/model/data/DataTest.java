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
		// Website website = new Website(
		// List.of("Rafa testing again"),
		// "CoolWebsite that I created",
		// "barbod is a cutie patootie",
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

		System.out.println(manager.read_data()); // all of them
		System.out.println(manager.read_data("Website")); // all the websites
		System.out.println(manager.read_data_by_id("cd908f4e-5218-44c5-8866-bca62b89a091")); // only one entry
	}
}
