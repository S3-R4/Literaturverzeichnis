package testfolder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import model.FuzzySearch;

public class TestFuzzySearch {

@Test
public void testBerechneDistanz() {

FuzzySearch fuzzy = new FuzzySearch();

// Identische Wörter
assertEquals(0, fuzzy.berechneDistanz("Barbod", "Barbod"));

// Ein Buchstabe unterschiedlich
assertEquals(1, fuzzy.berechneDistanz("Barbod", "Aarbod"));

// Ein Buchstabe fehlt
assertEquals(1, fuzzy.berechneDistanz("Barbod", "Barbo"));

// Groß- und Kleinschreibung ignorieren
assertEquals(0, fuzzy.berechneDistanz("BARBOD", "barbod"));

// Komplett unterschiedliche Wörter
assertEquals(4, fuzzy.berechneDistanz("Rafael", "Ahmet"));

// Leerer Text
assertEquals(4, fuzzy.berechneDistanz("", "yess"));
}
}