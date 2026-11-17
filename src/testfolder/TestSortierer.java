
package testfolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import model.Literatur;
import model.Sortierer;
import model.BildQuelle;

public class TestSortierer {

//AI given tests
@Test
public void testSortierenMitMehrerenPrioritaeten() {

BildQuelle bild1 = new BildQuelle("Müller", "", "", "", false, Optional.of("Zebra"), Optional.of("Foto"), Optional.of(LocalDate.of(2022, 1, 1)), Optional.of("Springer"), Optional.of("Anbieter"));

BildQuelle bild2 = new BildQuelle("Müller", "", "", "", false, Optional.of("Apfel"), Optional.of("Foto"), Optional.of(LocalDate.of(2024, 1, 1)), Optional.of("Springer"), Optional.of("Anbieter"));

BildQuelle bild3 = new BildQuelle("Schmidt", "", "", "", false, Optional.of("Mango"), Optional.of("Foto"), Optional.of(LocalDate.of(2023, 1, 1)), Optional.of("Springer"), Optional.of("Anbieter"));

List<Literatur> quellen = List.of(bild1, bild2, bild3);

Sortierer sortierer = new Sortierer();

List<Sortierer.SortierPrioritaet> prioritaeten = List.of(new Sortierer.SortierPrioritaet(Sortierer.Kriterium.AUTOR,Sortierer.Richtung.AUFSTEIGEND),new Sortierer.SortierPrioritaet(Sortierer.Kriterium.JAHR,Sortierer.Richtung.ABSTEIGEND));

List<Literatur> ergebnis = sortierer.sortiere(quellen, prioritaeten);

assertEquals("Apfel", ergebnis.get(0).getTitel().orElse(""));
assertEquals("Zebra", ergebnis.get(1).getTitel().orElse(""));
assertEquals("Mango", ergebnis.get(2).getTitel().orElse(""));
}


@Test
public void testAbsteigendUndLeereWerte() {

BildQuelle bild1 = new BildQuelle("Müller", "", "", "", false, Optional.of("Apfel"), Optional.of("Foto"), Optional.of(LocalDate.of(2022, 1, 1)), Optional.of("Springer"), Optional.of("Anbieter"));

BildQuelle bild2 = new BildQuelle("Schmidt", "", "", "", false, Optional.of("Zebra"), Optional.of("Foto"), Optional.of(LocalDate.of(2023, 1, 1)), Optional.of("Springer"), Optional.of("Anbieter"));

BildQuelle bild3 = new BildQuelle("Weber", "", "", "", false, Optional.empty(), Optional.of("Foto"), Optional.of(LocalDate.of(2024, 1, 1)), Optional.of("Springer"), Optional.of("Anbieter"));

List<Literatur> quellen = List.of(bild1, bild2, bild3);

Sortierer sortierer = new Sortierer();

List<Sortierer.SortierPrioritaet> prioritaeten = List.of(new Sortierer.SortierPrioritaet(Sortierer.Kriterium.TITEL,Sortierer.Richtung.ABSTEIGEND));

List<Literatur> ergebnis = sortierer.sortiere(quellen, prioritaeten);

// Absteigende Reihenfolge
assertEquals("Zebra", ergebnis.get(0).getTitel().orElse(""));
assertEquals("Apfel", ergebnis.get(1).getTitel().orElse(""));

// Leerer Titel bleibt am Ende
assertEquals("", ergebnis.get(2).getTitel().orElse(""));

// Ursprüngliche Liste bleibt unverändert
assertEquals("Apfel", quellen.get(0).getTitel().orElse(""));
}

@Test
public void testLeereAutorenUndJahre() {

BildQuelle bild1 = new BildQuelle("Müller", "", "", "", false, Optional.of("Apfel"), Optional.of("Foto"), Optional.of(LocalDate.of(2022, 1, 1)), Optional.of("Springer"), Optional.of("Anbieter"));

BildQuelle bild2 = new BildQuelle("", "", "", "", false, Optional.of("Zebra"), Optional.of("Foto"), Optional.of(LocalDate.of(2023, 1, 1)), Optional.of("Springer"), Optional.of("Anbieter"));

BildQuelle bild3 = new BildQuelle("Schmidt", "", "", "", false, Optional.of("Mango"), Optional.of("Foto"), Optional.empty(), Optional.of("Springer"), Optional.of("Anbieter"));

List<Literatur> quellen = List.of(bild1, bild2, bild3);

Sortierer sortierer = new Sortierer();

List<Sortierer.SortierPrioritaet> prioritaeten = List.of(new Sortierer.SortierPrioritaet(Sortierer.Kriterium.AUTOR,Sortierer.Richtung.ABSTEIGEND),new Sortierer.SortierPrioritaet(Sortierer.Kriterium.JAHR,Sortierer.Richtung.ABSTEIGEND));

List<Literatur> ergebnis = sortierer.sortiere(quellen, prioritaeten);

assertEquals("Mango", ergebnis.get(0).getTitel().orElse(""));
assertEquals("Apfel", ergebnis.get(1).getTitel().orElse(""));
assertEquals("Zebra", ergebnis.get(2).getTitel().orElse(""));
}
}
