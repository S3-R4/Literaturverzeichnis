package testfolder;

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
        Object[] alt = row("01", "Alt", "Buch", "Mueller", 1915);
        Object[] neu = row("02", "Neu", "Buch", "mueller", 2024);
        Object[] andere = row("03", "Andere", "Website", "Schmidt", 2000);
        List<Object[]> eingabe = List.of(alt, andere, neu);
        List<Object[]> ergebnis = sortierer.sortiereTabellenZeilen(eingabe, List.of(
                new SortierPrioritaet(Kriterium.AUTOR, Richtung.AUFSTEIGEND),
                new SortierPrioritaet(Kriterium.JAHR, Richtung.ABSTEIGEND)));
        assertEquals(List.of(neu, alt, andere), ergebnis);
        assertEquals(List.of(alt, andere, neu), eingabe);
    }

    @Test
    public void nummernUndJahreWerdenNumerischSortiert() {
        Object[] zehn = row("10", "Zehn", "Buch", "A", 900);
        Object[] zwei = row("02", "Zwei", "Buch", "A", "1915");
        List<Object[]> eingabe = List.of(zehn, zwei);
        assertEquals(List.of(zwei, zehn), sortierer.sortiereTabellenZeilen(eingabe,
                List.of(new SortierPrioritaet(Kriterium.NUMMER, Richtung.AUFSTEIGEND))));
        assertEquals(List.of(zehn, zwei), sortierer.sortiereTabellenZeilen(eingabe,
                List.of(new SortierPrioritaet(Kriterium.JAHR, Richtung.AUFSTEIGEND))));
    }

    @Test
    public void fehlendeWerteBleibenInBeidenRichtungenAmEnde() {
        Object[] leer = row("01", "", "Buch", null, null);
        Object[] ohneJahr = row("02", "  ", "Buch", "A", "o.J.");
        Object[] a = row("03", "Apfel", "Buch", "A", 1915);
        Object[] z = row("04", "Zebra", "Buch", "A", 2024);
        List<Object[]> eingabe = List.of(leer, a, ohneJahr, z);
        for (Kriterium kriterium : List.of(Kriterium.TITEL, Kriterium.JAHR)) {
            assertEquals(List.of(a, z, leer, ohneJahr), sortierer.sortiereTabellenZeilen(eingabe,
                    List.of(new SortierPrioritaet(kriterium, Richtung.AUFSTEIGEND))));
            assertEquals(List.of(z, a, leer, ohneJahr), sortierer.sortiereTabellenZeilen(eingabe,
                    List.of(new SortierPrioritaet(kriterium, Richtung.ABSTEIGEND))));
        }
    }

    @Test
    public void leereTabelleUndKeinePrioritaetenSindErlaubt() {
        assertTrue(sortierer.sortiereTabellenZeilen(List.of(), List.of(
                new SortierPrioritaet(Kriterium.TITEL, Richtung.AUFSTEIGEND))).isEmpty());
        Object[] a = row("02", "B", "Buch", "A", 2000);
        Object[] b = row("01", "A", "Buch", "A", 2001);
        List<Object[]> eingabe = List.of(a, b);
        List<Object[]> ergebnis = sortierer.sortiereTabellenZeilen(eingabe, List.of());
        assertEquals(eingabe, ergebnis);
        assertNotSame(eingabe, ergebnis);
    }

    @Test
    public void literaturSortierungVerwendetWeiterhinAutorUndDatum() {
        Website alt = quelle("Alt", LocalDate.of(2020, 1, 1));
        Website neu = quelle("Neu", LocalDate.of(2024, 1, 1));
        Website leer = quelle("Leer", null);
        alt.setAutor(Arrays.asList(null, "", "Mueller"));
        neu.setAutor(List.of("mueller"));
        leer.setAutor(null);
        List<Literatur> eingabe = List.of(alt, leer, neu);
        assertEquals(List.of(neu, alt, leer), sortierer.sortiere(eingabe, List.of(
                new SortierPrioritaet(Kriterium.AUTOR, Richtung.AUFSTEIGEND),
                new SortierPrioritaet(Kriterium.JAHR, Richtung.ABSTEIGEND))));
        assertEquals(List.of(alt, leer, neu), eingabe);
    }

    private Object[] row(String nummer, String titel, String typ, String autor, Object jahr) {
        return new Object[] {nummer, titel, typ, autor, jahr};
    }

    private Website quelle(String titel, LocalDate datum) {
        return new Website(List.of(), "", Optional.of(titel), "", null, datum, "");
    }
}
