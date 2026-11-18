package testfolder.buch;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;

import model.Buchbeleg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Test_Buch_Langbeleg {
    @Test
    public void testT13NormalerLangbeleg() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna"), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of("Informatik"), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertEquals("Müller, Anna (2022): Informatik. Berlin: Springer.", beleg.generiereLangbeleg());
    }

    @Test
    public void testT14ZweiAutoren() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna", "Schmidt, Peter"),  Optional.of(LocalDate.of(2021, 1, 1)),
                Optional.of("Informatik"), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertEquals("Müller, Anna & Schmidt, Peter (2021): Informatik. Berlin: Springer.", beleg.generiereLangbeleg());
    }

    @Test
    public void testT15DreiAutoren() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna", "Schmidt, Peter", "Maier, Lisa"), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of("Informatik"), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertEquals("Müller, Anna, Schmidt, Peter & Maier, Lisa (2022): Informatik. Berlin: Springer.", beleg.generiereLangbeleg());
    }

    @Test
    public void testT16MehrAlsDreiAutoren() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna", "Schmidt, Peter", "Maier, Lisa", "Weber, Tom"), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of("Informatik"), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertEquals("Müller, Anna, Schmidt, Peter, Maier, Lisa, et al. (2022): Informatik. Berlin: Springer.", beleg.generiereLangbeleg());
    }

    @Test
    public void testT17AutorMitUmlaut() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Jörg"), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of("Informatik"), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertTrue(beleg.generiereLangbeleg().contains("Müller, Jörg"));
    }

    @Test
    public void testT18FehlenderAutorenname() {
        Buchbeleg beleg = new Buchbeleg(List.of(""), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of("Informatik"), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertEquals("o.A. (2022): Informatik. Berlin: Springer.", beleg.generiereLangbeleg());
    }

    @Test
    public void testT19FehlendesErscheinungsjahr() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna"), Optional.empty(),
                Optional.of("Informatik"), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertEquals("Müller, Anna (o.J.): Informatik. Berlin: Springer.", beleg.generiereLangbeleg());
    }

    @Test
    public void testT20FehlenderBuchtitel() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna"), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of(""), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> beleg.generiereLangbeleg());
    }

    @Test
    public void testT21FehlenderVerlag() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna"), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of("Informatik"), Optional.of(""), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> beleg.generiereLangbeleg());
    }

    @Test
    public void testT22FehlenderErscheinungsort() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna"), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of("Informatik"), Optional.of("Springer"), Optional.empty(), Optional.of(""), Optional.empty(), Optional.empty());

        assertEquals("Müller, Anna (2022): Informatik. o.O.: Springer.", beleg.generiereLangbeleg());
    }

    @Test
    public void testT23BuchOhneAuflagenangabe() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna"), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of("Informatik"), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertFalse(beleg.generiereLangbeleg().contains("Aufl."));
    }

    @Test
    public void testT24BuchMitAuflage() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna"), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of("Informatik"), Optional.of("Springer"), Optional.of(3), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertEquals("Müller, Anna (2022): Informatik. 3. Aufl. Berlin: Springer.", beleg.generiereLangbeleg());
    }

    @Test
    public void testT25UngueltigeAuflage() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Buchbeleg(List.of("Müller, Anna"), Optional.of(LocalDate.of(2022, 1, 1)),
                    Optional.of("Informatik"), Optional.of("Springer"), Optional.of(-2), Optional.of("Berlin"), Optional.empty(), Optional.empty());
        });
    }

    @Test
    public void testT26UngueltigesErscheinungsjahr() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Buchbeleg(List.of("Müller, Anna"), Optional.of(LocalDate.of(2030, 1, 1)),
                    Optional.of("Informatik"), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());
        });
    }

    @Test
    public void testT27UeberfluessigeLeerzeichen() {
        Buchbeleg beleg = new Buchbeleg(List.of(" Müller, Anna "), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of(" Informatik "), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertEquals("Müller, Anna", beleg.getAutoren());
        assertEquals("Informatik", beleg.getTitel());
    }

    @Test
    public void testT28SehrLangerBuchtitel() {
        String titel = "sehr langer Titel";
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna"), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of(titel), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertTrue(beleg.generiereLangbeleg().contains(titel));
    }

    @Test
    public void testT29SonderzeichenImBuchtitel() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller, Anna"), Optional.of(LocalDate.of(2022, 1, 1)),
                Optional.of("Einführung in die künstliche Intelligenz"), Optional.of("Springer"), Optional.empty(), Optional.of("Berlin"), Optional.empty(), Optional.empty());

        assertTrue(beleg.generiereLangbeleg().contains("Einführung in die künstliche Intelligenz"));
    }
}
