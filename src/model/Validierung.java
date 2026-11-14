package model;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validierung {
public Validierung(){

}
public static boolean istJahrGueltig(String jahr) {
if (jahr == null || jahr.isEmpty()) {
    return false;
    }

    try {
    int jahrzahl = Integer.parseInt(jahr);
    return jahrzahl >= 1000 && jahrzahl <= 2027;

    } catch (NumberFormatException e) {
        return false;
    }
}
public static boolean istDatumGueltig(String datum) {
if (datum == null || datum.isEmpty()) {
    return false;
    }

    try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.uuuu").withResolverStyle(java.time.format.ResolverStyle.STRICT);
        LocalDate.parse(datum, formatter);
        return true;

    } catch (DateTimeParseException e) {
        return false;
    }
}

public static boolean istTextGueltig(String text) {
return text != null && !text.trim().isEmpty();
}

public static boolean istURLGueltig(String url) {
if (url == null || url.trim().isEmpty()) {
    return false;
    }

    try {
    URI uri = new URI(url.trim());

    return ("http".equalsIgnoreCase(uri.getScheme())|| "https".equalsIgnoreCase(uri.getScheme()))&& uri.getHost() != null;

    } catch (Exception e) {
        return false;
    }
}

}

