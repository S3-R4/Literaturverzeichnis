
package testfolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import model.Literatur;
import model.Sortierer;
import model.BildQuelle;

public class TestSortierer {

@Test
public void testSortierenMitMehrerenPrioritaeten() {

BildQuelle bild1 = new BildQuelle("Müller", "", "", "", false,"Zebra", "Foto", LocalDate.of(2022, 1, 1),"Springer", "Anbieter");

BildQuelle bild2 = new BildQuelle("Müller", "", "", "", false,"Apfel", "Foto", LocalDate.of(2024, 1, 1),"Springer", "Anbieter");

BildQuelle bild3 = new BildQuelle("Schmidt", "", "", "", false,"Mango", "Foto", LocalDate.of(2023, 1, 1),"Springer", "Anbieter");

List<Literatur> quellen = List.of(bild1, bild2, bild3);

Sortierer sortierer = new Sortierer();

List<Sortierer.SortierPrioritaet> prioritaeten = List.of(new Sortierer.SortierPrioritaet(Sortierer.Kriterium.AUTOR,Sortierer.Richtung.AUFSTEIGEND),new Sortierer.SortierPrioritaet(Sortierer.Kriterium.JAHR,Sortierer.Richtung.ABSTEIGEND));

List<Literatur> ergebnis = sortierer.sortiere(quellen, prioritaeten);

assertEquals("Apfel", ergebnis.get(0).getTitel());
assertEquals("Zebra", ergebnis.get(1).getTitel());
assertEquals("Mango", ergebnis.get(2).getTitel());
}


@Test
public void testAbsteigendUndLeereWerte() {

BildQuelle bild1 = new BildQuelle("Müller", "", "", "", false,"Apfel", "Foto", LocalDate.of(2022, 1, 1),"Springer", "Anbieter");

BildQuelle bild2 = new BildQuelle("Schmidt", "", "", "", false,"Zebra", "Foto", LocalDate.of(2023, 1, 1),"Springer", "Anbieter");

BildQuelle bild3 = new BildQuelle("Weber", "", "", "", false,"", "Foto", LocalDate.of(2024, 1, 1),"Springer", "Anbieter");

List<Literatur> quellen = List.of(bild1, bild2, bild3);

Sortierer sortierer = new Sortierer();

List<Sortierer.SortierPrioritaet> prioritaeten = List.of(new Sortierer.SortierPrioritaet(Sortierer.Kriterium.TITEL,Sortierer.Richtung.ABSTEIGEND));

List<Literatur> ergebnis = sortierer.sortiere(quellen, prioritaeten);

// Absteigende Reihenfolge
assertEquals("Zebra", ergebnis.get(0).getTitel());
assertEquals("Apfel", ergebnis.get(1).getTitel());

// Leerer Titel bleibt am Ende
assertEquals("", ergebnis.get(2).getTitel());

// Ursprüngliche Liste bleibt unverändert
assertEquals("Apfel", quellen.get(0).getTitel());
}
}
