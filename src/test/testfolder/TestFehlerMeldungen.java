package test.testfolder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import model.Fehlermeldungen;

public class TestFehlerMeldungen {

    @Test
    public void testFehlermeldungen() {

        assertEquals("Die Eingabe enthält ungültige Angaben.", Fehlermeldungen.getAllgemeinerFehler());

        assertEquals("Dieses Feld darf nicht leer sein.", Fehlermeldungen.getLeeresFeld());

        assertEquals("Ungültiges Erscheinungsjahr.", Fehlermeldungen.getJahrFehler());

        assertEquals("Ungültiges Datum.", Fehlermeldungen.getDatumFehler());

        assertEquals("Ungültige Texteingabe.", Fehlermeldungen.getTextFehler());

        assertEquals("Ungültige URL.", Fehlermeldungen.getURLFehler());
    }
}
