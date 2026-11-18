package test.testfolder;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import model.Literatur;
import model.Sortierer;
import model.Sortierer.Kriterium;
import model.Sortierer.Richtung;
import model.Sortierer.SortierPrioritaet;
import model.Website;
import org.junit.jupiter.api.Test;

public class TestTabellenSortierung {
    private final Sortierer sortierer = new Sortierer();

    @Test
    public void mehrereSchluesselUndEingabeBleibtUnveraendert() {
        Website alt = quelle("Alt", LocalDate.of(2020, 1, 1));
        Website neu = quelle("Neu", LocalDate.of(2024, 1, 1));
        Website leer = quelle("Leer", null);
        alt.setAutor(Arrays.asList(null, "", "Mueller"));
        neu.setAutor(List.of("mueller"));
        leer.setAutor(List.of());
        List<Literatur> eingabe = List.of(alt, leer, neu);
        assertEquals(List.of(neu, alt, leer), sortierer.sortiere(eingabe, List.of(
                new SortierPrioritaet(Kriterium.AUTOR, Richtung.AUFSTEIGEND),
                new SortierPrioritaet(Kriterium.JAHR, Richtung.ABSTEIGEND))));
        assertEquals(List.of(alt, leer, neu), eingabe);
    }

    @Test
    public void jahreWerdenChronologischSortiert() {
        Website alt = quelle("Alt", LocalDate.of(900, 1, 1));
        Website neu = quelle("Neu", LocalDate.of(1915, 1, 1));
        assertEquals(List.of(alt, neu), sortierer.sortiere(List.of(neu, alt),
                List.of(new SortierPrioritaet(Kriterium.JAHR, Richtung.AUFSTEIGEND))));
    }

    @Test
    public void fehlendeWerteBleibenInBeidenRichtungenAmEnde() {
        Website leer = quelle("", null);
        Website ohneJahr = quelle("  ", null);
        Website a = quelle("Apfel", LocalDate.of(1915, 1, 1));
        Website z = quelle("Zebra", LocalDate.of(2024, 1, 1));
        List<Literatur> eingabe = List.of(leer, a, ohneJahr, z);
        for (Kriterium kriterium : List.of(Kriterium.TITEL, Kriterium.JAHR)) {
            assertEquals(List.of(a, z, leer, ohneJahr), sortierer.sortiere(eingabe,
                    List.of(new SortierPrioritaet(kriterium, Richtung.AUFSTEIGEND))));
            assertEquals(List.of(z, a, leer, ohneJahr), sortierer.sortiere(eingabe,
                    List.of(new SortierPrioritaet(kriterium, Richtung.ABSTEIGEND))));
        }
    }

    @Test
    public void leereEingabeUndKeinePrioritaetenSindErlaubt() {
        assertTrue(sortierer.sortiere(List.of(), List.of(
                new SortierPrioritaet(Kriterium.TITEL, Richtung.AUFSTEIGEND))).isEmpty());
        List<Literatur> eingabe = List.of(quelle("B", null), quelle("A", null));
        List<Literatur> ergebnis = sortierer.sortiere(eingabe, List.of());
        assertEquals(eingabe, ergebnis);
        assertNotSame(eingabe, ergebnis);
    }

    private Website quelle(String titel, LocalDate datum) {
        return new Website(List.of(), "", Optional.of(titel), "", null, datum, "");
    }
}
