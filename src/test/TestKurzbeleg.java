import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestKurzbeleg {
    @Test
    public void testT01NormalesKurzzitat() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller"), Optional.of(2022),
                Optional.of(45), Optional.empty());

        assertEquals("(Müller, 2022, S.45)", beleg.getBelegKurz());
    }

    @Test
    public void testT02ZweiAutoren() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller", "Schmidt"), Optional.of(2021),
                Optional.of(12), Optional.empty());

        assertEquals("(Müller & Schmidt, 2021, S.12)", beleg.getBelegKurz());
    }

    @Test
    public void testT03DreiAutoren() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller", "Schmidt", "Maier"), Optional.of(2022),
                Optional.of(45), Optional.empty());

        assertEquals("(Müller, Schmidt & Maier, 2022, S.45)", beleg.getBelegKurz());
    }

    @Test
    public void testT04AutorMitUmlaut() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller"), Optional.of(2020),
                Optional.of(30), Optional.empty());

        assertTrue(beleg.getBelegKurz().contains("Müller"));
    }

    @Test
    public void testT05FehlenderAutorenname() {
        Buchbeleg beleg = new Buchbeleg(List.of(""), Optional.of(2022),
                Optional.of(45), Optional.empty());

        assertEquals("(o.A., 2022, S.45)", beleg.getBelegKurz());
    }

    @Test
    public void testT06FehlendesErscheinungsjahr() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller"), Optional.empty(),
                Optional.of(45), Optional.empty());

        assertEquals("(Müller, o.J., S.45)", beleg.getBelegKurz());
    }

    @Test
    public void testT07UngueltigeSeitenzahl() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Buchbeleg(List.of("Müller"), Optional.of(2022),
                    Optional.of(-5), Optional.empty());
        });
    }

    @Test
    public void testT08SeitenzahlNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Buchbeleg(List.of("Müller"), Optional.of(2022),
                    Optional.of(0), Optional.empty());
        });
    }

    @Test
    public void testT09UeberfluessigeLeerzeichen() {
        Buchbeleg beleg = new Buchbeleg(List.of(" Müller "), Optional.of(2022),
                Optional.of(45), Optional.empty());

        assertEquals("Müller", beleg.getAutoren());
    }

    @Test
    public void testT10SehrLangerAutorenname() {
        Buchbeleg beleg = new Buchbeleg(List.of("Langer Nachname"), Optional.of(2022),
                Optional.of(45), Optional.empty());

        assertTrue(beleg.getBelegKurz().contains("Langer Nachname"));
    }

    @Test
    public void testT11MehrereSeiten() {
        Buchbeleg beleg = new Buchbeleg(List.of("Müller"), Optional.of(2022),
                Optional.of(45), Optional.of(47));

        assertEquals("(Müller, 2022, S.45-47)", beleg.getBelegKurz());
    }
}
