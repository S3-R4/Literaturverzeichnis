package model;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class Validierung {
public Validierung(){

}
public static String pruefeJahr(String jahr) {
if (jahr == null || jahr.trim().isEmpty()) {
    return Fehlermeldungen.getJahrFehler();
    }

    try {
    jahr = jahr.trim();
    int jahrzahl = Integer.parseInt(jahr);

    if (jahrzahl < 1000 || jahrzahl > 2027) {
    return Fehlermeldungen.getJahrFehler();
        }

    } catch (NumberFormatException e) {
        return Fehlermeldungen.getJahrFehler();
    }

    return null;
}

public static String pruefeDatum(String datum) {
if (datum == null || datum.trim().isEmpty()) {
    return Fehlermeldungen.getDatumFehler();
    }

    try {
        datum = datum.trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.uuuu").withResolverStyle(ResolverStyle.STRICT);

        LocalDate.parse(datum, formatter);

    } catch (DateTimeParseException e) {
        return Fehlermeldungen.getDatumFehler();
    }

    return null;
}

public static String pruefeText(String text) {
if (text == null || text.trim().isEmpty()) {
    return Fehlermeldungen.getTextFehler();
    }

    return null;
}

public static String pruefeURL(String url) {
if (url == null || url.trim().isEmpty()) {
    return Fehlermeldungen.getURLFehler();
    }

    try {
    URI uri = new URI(url.trim());

        if (!("http".equalsIgnoreCase(uri.getScheme())|| "https".equalsIgnoreCase(uri.getScheme()))|| uri.getHost() == null) {

            return Fehlermeldungen.getURLFehler();
        }

    } catch (URISyntaxException e) {
        return Fehlermeldungen.getURLFehler();
    }

    return null;
}
}

